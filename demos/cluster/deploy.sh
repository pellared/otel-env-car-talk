#!/usr/bin/env bash
#
# Bring up the cluster demos 2-4 run on: k3d, MinIO for artifacts, Jaeger, the
# OpenTelemetry operator and collector, and Argo Workflows wired to emit traces.
#
# Component versions live next to the thing that installs them:
#   k3s                    k3d.conf
#   argo-workflows         kustomization.yaml + argo-workflow-controller-cm.yaml
#   otel collector         opentelemetry-collector.yaml
#   jaeger                 jaeger.yaml
#   cert-manager / otel operator   below

set -euo pipefail

CERT_MANAGER_VERSION=v1.21.2
OTEL_OPERATOR_VERSION=v0.159.0

cd "$(dirname "$0")"

# Use a dedicated kubeconfig rather than inheriting whatever KUBECONFIG points
# at. A multi-entry KUBECONFIG stops k3d writing the new context at all, which
# would otherwise leave every kubectl below aimed at an unrelated cluster.
KUBECONFIG_FILE="${KUBECONFIG_FILE:-$HOME/.kube/configs/k3d-otel.yaml}"
mkdir -p "$(dirname "$KUBECONFIG_FILE")"
export KUBECONFIG="$KUBECONFIG_FILE"

if k3d cluster list otel >/dev/null 2>&1; then
	echo "k3d cluster 'otel' already exists. Remove it first:" >&2
	echo "    k3d cluster delete otel" >&2
	exit 1
fi

k3d cluster create --config k3d.conf

# Refuse to touch anything unless we are pointed at the cluster we just made.
context="$(kubectl config current-context)"
if [[ "$context" != "k3d-otel" ]]; then
	echo "Expected kubectl context 'k3d-otel', got '$context'. Refusing to continue." >&2
	exit 1
fi

# Keep workloads off the control plane.
kubectl taint node k3d-otel-server-0 k3s-controlplane=true:NoSchedule

for ns in argo minio jaeger; do
	kubectl create namespace "$ns" --dry-run=client -o yaml | kubectl apply -f -
done

kubectl apply -f minio-deploy.yaml
kubectl apply -f jaeger.yaml

# The operator's webhooks need cert-manager.
kubectl apply -f "https://github.com/cert-manager/cert-manager/releases/download/${CERT_MANAGER_VERSION}/cert-manager.yaml"
kubectl rollout status deployment cert-manager-webhook -n cert-manager --timeout=300s

kubectl apply --server-side -f "https://github.com/open-telemetry/opentelemetry-operator/releases/download/${OTEL_OPERATOR_VERSION}/opentelemetry-operator.yaml"
kubectl rollout status deployment opentelemetry-operator-controller-manager \
	-n opentelemetry-operator-system --timeout=300s

kubectl apply -n default -f minio-secret.yaml
kubectl apply -n argo -f minio-secret.yaml

# These must exist BEFORE any pod that wants SDK injection is admitted. kubectl
# orders a single apply by kind and creates Deployments before custom resources,
# so leaving these inside the kustomization makes the pod mutating webhook fail
# with "no OpenTelemetry Instrumentation instances available" on a fresh cluster.
kubectl apply -f opentelemetry-instrumentation.yaml
kubectl apply -f opentelemetry-instrumentation-default.yaml

# Argo Workflows and the collector. The collector CR needs the operator's CRDs
# and webhook, both ready by now.
kubectl apply --server-side -k .
kubectl apply -f executor-rbac.yaml

kubectl rollout status deployment workflow-controller -n argo --timeout=300s
kubectl rollout status deployment argo-server -n argo --timeout=300s
kubectl rollout status deployment workflows-collector -n argo --timeout=300s
kubectl rollout status deployment jaeger -n jaeger --timeout=300s
kubectl rollout status deployment minio -n minio --timeout=300s

cat <<'MSG'

Deployed. The k3d loadbalancer maps these to localhost:

  Jaeger          http://localhost:16686
  Argo Server     https://localhost:2746       (token from ./argo-token.sh)
  MinIO console   http://localhost:9001        (admin/password)
  Registry        localhost:5000               (image: k3d-registry.localhost:5000/...)

MSG
cat <<MSG
The cluster's kubeconfig was written to:

  $KUBECONFIG_FILE

Context name is k3d-otel. To use it from your shell:

  export KUBECONFIG=$KUBECONFIG_FILE

Then run the demos in ../2-argo-to-otel-cli, ../3-argo-to-buildkit and
../4-argo-to-python-sql.

MSG

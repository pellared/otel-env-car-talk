# The Demo Cluster

Demos 2, 3 and 4 run on a local Kubernetes cluster with Argo Workflows already
emitting traces. This directory builds that cluster and nothing else. The demos
bring their own workflows.

```text
workflow-controller, argoexec, workloads --OTLP--> collector --OTLP--> Jaeger
```

It is trimmed down from [Joibel/otel-deploy](https://github.com/Joibel/otel-deploy),
which also adds Grafana, Tempo and span metrics.

## What you get

| Component | Why the demos need it |
|---|---|
| k3d (k3s v1.37), 1 server + 3 agents, with a registry | Somewhere to run pods, and to push images the pods can pull |
| Argo Workflows v4.1.3, released images | The launcher: its controller and executor inject `TRACEPARENT` |
| OpenTelemetry operator | Injects `OTEL_EXPORTER_OTLP_ENDPOINT` into Argo and workload pods, and auto-instruments Python for demo 4 |
| OpenTelemetry Collector | Receives OTLP on 4317 (gRPC) and 4318 (HTTP) and forwards traces to Jaeger |
| Jaeger v2, in-memory | Where you look at the traces |
| MinIO | Argo's artifact repository |
| cert-manager | Required by the operator's webhooks |

## Prerequisites

- Docker
- [k3d](https://k3d.io) v5
- `kubectl`
- Free host ports 16686, 2746, 9001 and 5000

## Bring it up

```sh
./deploy.sh
export KUBECONFIG=~/.kube/configs/k3d-otel.yaml
```

It takes a few minutes. The script writes its own kubeconfig rather than editing
yours, and refuses to continue if the context it ends up on is not `k3d-otel`.

| URL | What |
|---|---|
| <http://localhost:16686> | Jaeger |
| <https://localhost:2746> | Argo Workflows UI. Log in with the token from `./argo-token.sh` |
| <http://localhost:9001> | MinIO console (admin/password) |
| `localhost:5000` | Registry. Push here from your machine. Image references in a workflow use `k3d-registry.localhost:5000`; code running inside a pod (BuildKit, in demo 3) reaches it as `host.k3d.internal:5000` |

Workflows go in the `default` namespace. Each workflow in the Argo UI has a
**Trace in Jaeger** link, and each pod a **Pod trace in Jaeger** link.

## How tracing is switched on

There is no tracing flag. Argo starts tracing as soon as
`OTEL_EXPORTER_OTLP_ENDPOINT` is set, and the operator sets it:

- `argo-workflow-controller-deployment.yaml` annotates the controller with
  `instrumentation.opentelemetry.io/inject-sdk`.
- `argo-workflow-controller-cm.yaml` puts the same annotation on every workflow
  pod through `workflowDefaults`, for the `init`, `wait` and `main` containers.
- `opentelemetry-instrumentation*.yaml` say where the endpoint points, one per
  namespace. In `default`, Python is sent to the collector's HTTP port instead,
  because the operator's Python distro has no gRPC exporter.

None of this carries trace context. It only tells each process where to send its
spans. The context itself travels in `TRACEPARENT`, which is what the demos are
about.

## Tear it down

```sh
k3d cluster delete otel
```

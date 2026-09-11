# Trace Context Beyond HTTP: Environment Variables as OpenTelemetry Propagation Carriers

## Description

Distributed tracing usually assumes context travels in HTTP headers or message metadata. Yet, many workflows cross process boundaries instead: CI jobs launch scripts, workflow engines start containers, and build tools fork subprocesses, losing trace context.

This talk presents the use of OpenTelemetry environment variables propagation carriers, showing how traces can flow across CLI apps, CI/CD systems, build pipelines, and subprocesses without relying on network transports. The carrier model supports multiple propagation formats, enabling interoperability without reliance on a single ecosystem-specific encoding.

Through two demos it demonstrates how injected TRACEPARENT enables end-to-end tracing, linking stages, subprocesses, and even data lineage in a single span tree. Attendees will gain a practical model for maintaining trace continuity beyond network protocols without custom formats.

## Benefits to the ecosystem

OpenTelemetry has made trace context propagation routine for HTTP and other networked services, but many developer and operations workflows still lose context when work moves through processes instead of network requests. Standardizing environment variables as propagation carriers gives CI systems, workflow engines, build tools, CLIs, and OpenTelemetry instrumentation a shared way to preserve trace continuity without inventing incompatible conventions.

This talk helps practitioners and maintainers understand the OpenTelemetry specification while real-world adoption can still inform implementation guidance, examples, and future refinements. By explaining format-agnostic carriers, environment variable name normalization, process-spawning responsibilities, and security constraints, the session gives the community a common model for adopting environment-based propagation safely and consistently.

The broader benefit is better interoperability between the tools that create work and the tools that observe it. A shared propagation model reduces custom glue code, prevents fragmented trace conventions from taking root in CI/CD and batch systems, and gives OpenTelemetry contributors clearer practitioner input as language implementations and operational guidance mature.

Two demos to make it concrete.

The first is a Docker image build inside Argo Workflows. The controller injects TRACEPARENT into every step container, and clone, build, scan, and push all hang off one trace. When something gets slow or breaks, the trace points at the stage to blame, including subprocesses the build tool spawned itself.

The second uses the same plumbing for data lineage. A batch pipeline spawns extract, transform, and load processes, each inheriting trace context through the environment. The span tree records which job read which dataset and produced which output. Lineage falls out of tracing rather than needing its own metadata system bolted on. 

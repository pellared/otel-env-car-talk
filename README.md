# Trace Context Beyond HTTP: Environment Variables as OpenTelemetry Propagation Carriers

## Description

Distributed tracing commonly carries context in HTTP headers or message metadata. That path ends when a service launches a CLI, a workflow engine starts a container, or a build tool starts a subprocess. This talk shows how an environment variable can carry trace context across those process boundaries.

A Java client, Go API, and Python CLI first make the problem visible: the HTTP request belongs to one trace, while the CLI starts another. The talk introduces traces, spans, parentage, propagators, and carriers through that example. The Go launcher then injects the active context as `TRACEPARENT` into a copy of the child environment, without another network request. Python extracts it at startup, and its instrumentation creates spans with the extracted parent. The connected trace shows the CLI work under the original request.

The same model continues through three captured Argo examples. An `otel-cli` workload shows context moving from the workflow controller into a pod and from its executor into the user's command. A two-step Docker image pipeline shows an uninstrumented git clone beside an instrumented BuildKit build; BuildKit also pushes the image within its build command. The trace separates workflow waiting from command runtime and exposes slow operations inside the build. A batch data pipeline shows setup, parallel derived-table jobs, a summary, a report, and a final lineage artifact. Python auto-instrumentation wraps psycopg calls and records SQL statements as span metadata. The final step derives table-level inputs and outputs from those statements in the shared trace.

The environment is the carrier, while the configured propagator chooses the field names and format. The talk uses W3C `traceparent` as its main example and shows that the carrier can also support baggage and B3. It covers environment-variable name normalization, launcher injection, child extraction, inherited-environment trust boundaries, log exposure, baggage allow-listing, and feedback on the evolving specification. Environment variables carry context; instrumentation creates spans. Trace-derived lineage is useful here without replacing every dedicated lineage capability.

Robert Pająk (Theoretician) explains the model and constraints; Alan Clucas (Practitioner) walks through the workflow evidence and operational consequences. The 28-slide presentation plans 18:25 of content plus a 01:35 transition and recovery buffer. The final five minutes of the 25-minute slot are reserved for questions and troubleshooting.

## Benefits to the ecosystem

A shared environment carrier helps CI systems, workflow engines, build tools, CLIs, and OpenTelemetry instrumentation continue a trace when work moves between processes. Those tools can use their configured propagator instead of inventing a trace format or a custom adapter for each handoff.

The captured examples show both sides of that contract. Launchers inject context into child environments; instrumented children extract it and create spans. An uninstrumented command still has useful timing from its surrounding workflow spans, while an instrumented build exposes its internal operations. In the batch example, a platform-owned Python wrapper extracts context before auto-instrumentation records SQL; the trace then connects the jobs whose statements provide the lineage evidence.

The session gives implementers a concrete basis for consistent variable names, safe handling of inherited fields and baggage, and practical feedback on the environment-carrier specification as language support and operational guidance mature.

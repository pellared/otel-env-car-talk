# Trace Context Beyond HTTP: Environment Variables as OpenTelemetry Propagation Carriers

## Description

Distributed traces usually carry context in HTTP headers or message metadata, but CI jobs, workflow engines, and build tools also cross process boundaries. This talk shows how OpenTelemetry propagators use environment variables as carriers so launched CLIs, containers, and subprocesses can continue the same trace without a network handoff or custom format.

Starting from a broken HTTP-to-CLI trace, we show how injecting `TRACEPARENT` and extracting it in the child preserves parentage. Captured Argo examples connect a workflow to its workload, reveal timing inside a Docker build, and derive table-level lineage from instrumented SQL spans in a batch pipeline. The carrier supports multiple propagation formats; instrumentation creates spans and records the evidence for lineage. Attendees leave with a practical model for consistent names, safe inherited context, and interoperable process launches. Trace-derived lineage does not replace dedicated lineage tools.

## Benefits to the ecosystem

A shared environment carrier helps CI systems, workflow engines, build tools, CLIs, and OpenTelemetry instrumentation continue a trace when work moves between processes. Those tools can use their configured propagator instead of inventing a trace format or a custom adapter for each handoff.

The captured examples show both sides of that contract. Launchers inject context into child environments; instrumented children extract it and create spans. An uninstrumented command still has useful timing from its surrounding workflow spans, while an instrumented build exposes its internal operations. In the batch example, a platform-owned Python wrapper extracts context before auto-instrumentation records SQL; the trace then connects the jobs whose statements provide the lineage evidence.

The session gives implementers a concrete basis for consistent variable names, safe handling of inherited fields and baggage, and practical feedback on the environment-carrier specification as language support and operational guidance mature.

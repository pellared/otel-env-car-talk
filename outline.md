# S01: Trace Context Beyond HTTP

Theoretician: Welcome. We are Robert Pająk and Alan Clucas. This talk follows trace context across process boundaries, from one process into the next.

Practitioner: We will connect the model to the realities of launching containers, build steps, and batch jobs.

# S02: Robert Pająk

Theoretician: I’m Robert Pająk, pellared on GitHub. I work at Splunk and maintain OpenTelemetry Go and the OpenTelemetry Specification.

Theoretician: Where was this picture taken? Take a guess.

Theoretician: Prague. I’ll cover the model, terminology, and constraints behind safe context propagation.

# S03: Alan Clucas

Practitioner: I’m Alan Clucas, Joibel on GitHub. I work at Pipekit and lead Argo Workflows.

Practitioner: Where was this picture taken? Take a guess.

Practitioner: Prague. I’ll show what it means for real workflows, operators, and failures.

# S04: Learning through demos

Practitioner: Rather than front-load a glossary, we will meet each concept inside a working system. First, an HTTP service launches a CLI. We will read the normal HTTP trace, find the break, change the carrier, and compare the result.

# S05: HTTP service starts a CLI

Practitioner: Our Java client asks a Go API to build a report. The API starts a Python CLI in its own process. All three programs export spans to Jaeger. The network boundary is Java to Go. The process boundary is Go to Python. Those boundaries need different places to carry the same trace identity.

# S06: Trace, spans, and parentage

Theoretician: A span records one timed operation, such as the POST or fetching data. A trace is the complete causal story, built from spans that name their parent. One trace ID groups the story. Each span has its own span ID. If the parent-child chain survives both boundaries, Jaeger can show the request, service, and CLI work as one tree.

# S07: Context propagation over HTTP

Theoretician: To cross HTTP, the currently active span contributes trace context: the trace ID, parent span ID, and flags that the next program needs. A propagator translates that in-memory context into a standard format and back again. Here the W3C Trace Context propagator injects a `traceparent` field. The HTTP headers are the carrier, the key-value container that transports it. Go extracts that field, then its instrumentation starts a child span.

Practitioner: This is the normal OpenTelemetry HTTP pattern. Java injects and Go’s HTTP instrumentation extracts, so the request reaches Go in one connected trace.

# S08: HTTP headers stop at the process boundary

Practitioner: The Go API can see the active trace when it starts `run report.py`. But starting Python is not an HTTP request, so no request headers cross that boundary. With no other carrier, Python extracts no parent and starts `build report` as a separate root trace. The screenshots show both halves: the request trace ends at `run report.py`; the Python trace starts at `build report`. Different trace IDs prove the parent-child link was lost.

# S09: The child environment carries the handoff

Theoretician: We keep the W3C Trace Context propagator and change the carrier. Just before the spawn, Go injects the active context into a copy of the child environment. The environment carrier normalizes `traceparent` to `TRACEPARENT`. At startup, Python extracts from its environment.

Practitioner: Python instrumentation then creates `build report` using that extracted parent. The launcher owns injection. Child instrumentation owns extraction and span creation. The carrier transports opaque propagation fields, so the same carrier model can support other configured formats.

# S10: The CLI joins the original trace

Practitioner: In the captured fixed run, Jaeger shows seven spans from three services under one trace. We can now see the CLI’s `build report`, `fetch data`, and `generate pdf` work under `run report.py`. That visibility comes from preserving parentage across the spawn, not from the environment variable creating spans.

Theoretician: Same trace context, same propagator, and a carrier suited to the boundary.

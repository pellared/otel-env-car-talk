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

# S11: Tracing a workflow

Practitioner: Here is the dream. A workflow is a graph of steps: A runs first, B and C run in parallel once A finishes, and D waits for both. If the workflow were a trace, it would look like this. One span for the workflow itself, from submission to completion. One span per step, each a child of the workflow, sitting exactly where it ran. You can read the graph straight off the bars: B and C overlap because they ran together, and D starts when the longer of them finishes. That is the promise. The orchestrator’s view and the timing view become one picture.

# S12: Inside the pods

Practitioner: Every step runs in a Kubernetes pod. The bars from the last slide are the controller’s view of each step: from the moment it created the pod to the moment it noticed the pod had finished. Inside each pod something actually ran, and that deserves a span of its own. It starts later, because the pod had to be scheduled and its image pulled. It ends earlier, because the controller only notices completion on its next reconcile. On a quiet cluster the two bars almost coincide. On a stressed cluster they drift apart markedly, and the gap is exactly the time the workflow spent waiting on Kubernetes rather than doing work. I want to see both.

Theoretician: Which means the trace context has to reach the inside of the pod.

# S13: What the workload did

Practitioner: Zoom into one step. The controller’s view, the pod’s view, and now, inside the pod, what the user’s workload actually did: three spans it emitted itself, named observability, summit, and prague. This is the layer that matters to the person who wrote the workflow, and the platform knows nothing about it. It only appears if the workload’s own instrumentation joins the same trace. Three layers, three owners: the controller, the executor in the pod, and the user’s code. One trace.

Theoretician: Three process boundaries, and not one of them is an HTTP request.

# S14: One step, three spans

Practitioner: This is demo 2 as Argo sees it: a workflow with a single step, so a single pod. And this is everything that pod runs. Three otel-cli commands, each emitting one span, named after this conference. And an echo of TRACEPARENT, purely so you can see the variable is there. Notice what is missing. Nothing in this workflow mentions tracing. Nobody named a propagator or configured an SDK. otel-cli is an off-the-shelf tool that reads its environment, and that is all it needs.

# S15: The workload joins the trace

Practitioner: Here is the trace. The workflow span at the top belongs to the controller. Under it, the node, then creating the pod. Then argoexec, the executor inside the pod: runInitContainer, runWaitContainer, and runMainContainer. And under runMainContainer, the three spans the workload emitted: observability, summit, prague, three seconds, five, four. This is the third dream slide, for real. Notice where the workload spans hang. Not off the node, off runMainContainer. That tells you there were two injections, not one. The controller injected its context into the pod’s environment. Then the executor started its own span and injected again, into the environment of the process it launched. Two carrier hops, and the workflow author wrote neither of them.

Theoretician: The propagator never changed. Only the carrier did, and it was the same carrier both times.

# S16: Two injections

Practitioner: Two handoffs, and here they are, working outward from the workload. The nearer one is inside the pod. argoexec starts the runMainContainer span, then injects again with the OpenTelemetry environment carrier, the same envcar package demo 1’s Go launcher used, into a copy of the environment it hands only to the user’s command. Its own environment stays exactly as the pod spec set it. That is why the workload’s spans hang off runMainContainer. Now one level out: where did argoexec’s environment come from? From the workflow-controller, when it built the pod spec. It ran the same W3C propagator against a carrier whose Set method appends a Kubernetes environment variable, so every container in the pod is born with TRACEPARENT. That is the entire mechanism. Same propagator in both places. The carrier is the environment both times.

Theoretician: Note what Argo did not do. It did not invent a format or parse a value. It reused W3C Trace Context and changed only where the fields travel. Both sides now use the same SetEnvFunc shape, and argoexec uses the very carrier package demo 1 did.

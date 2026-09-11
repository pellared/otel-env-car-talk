# S01: Trace context beyond HTTP

Practitioner: I’m Alan Clucas, Joibel on GitHub. I work at Pipekit and contribute to Argo. We’ll follow one trace through shells, containers, build tools, and batch jobs.

Theoretician: I’m Robert Pająk, pellared on GitHub. I work at Splunk and contribute to OpenTelemetry. I’ll frame the model and constraints. Alan will show the operational consequences.

# S02: Process launches leave gaps in the trace

Practitioner: Picture a delivery pipeline. We expect one trace, a connected record of the run. The workflow runner clones a repository, starts a container build, launches a scanner, and finally pushes the image. An instrumented process can emit a span, a timed record of one operation. Without incoming parent coordinates, each span begins a separate top-level trace. The backend shows unrelated results, so the operator has to reconstruct one run from timestamps and labels.

Practitioner: Network services carry parent coordinates in HTTP headers, and queues can use message metadata. A shell or container launch uses neither. This boundary is common. CI jobs start scripts. Workflow engines start containers. Build tools fork compilers and test processes. Batch schedulers launch programs.

Theoretician: To keep the trace connected, the child needs a small description of where its work belongs. First we need a precise picture of that family relationship.

# S03: A trace is a family tree of work

Theoretician: A span is a timed record of one operation, such as the whole workflow, one build step, or one compiler invocation. A trace is the set of spans that describe one end-to-end operation. Each span has at most one parent. A child span represents work caused by its parent, and siblings share the same parent.

Theoretician: In this tree, workflow is the root because it has no parent. Build and scan are children of workflow. Compiler is a child of build. Every span has its own span identifier, while every span in the tree shares one trace identifier. That shared trace ID and the parent links let a backend assemble the tree even when the spans came from different processes.

# S04: Context connects, telemetry reports

Theoretician: Trace context is the small set of identifiers and settings that describes the current position in a trace. It includes the trace ID, the current span ID, and small tracing flags. Propagation means moving that context across a boundary so the receiver can choose the right parent. The carrier is the medium that holds the propagation fields while they move.

Theoretician: The context is different from the span data that a backend receives. An exporter is the component that sends completed spans to that backend. Injection writes context into a carrier. Extraction reads it into the child process. Then instrumentation creates a child span and uses the extracted context as its parent.

Practitioner: That separation gives us a useful diagnostic. If spans exist as separate roots, instrumentation probably ran but propagation failed. If no child span exists at all, carrying context would not create one. We still need instrumentation around the work.

# S05: Carrier, propagator, format, and instrumentation

Theoretician: Four terms describe three responsibilities. Instrumentation creates and ends spans. A propagator is code that injects and extracts propagation fields. The propagation format defines those field names and values. W3C Trace Context uses traceparent and optionally tracestate, while B3 uses different fields. The carrier holds the resulting strings across a boundary. HTTP headers are one carrier, and a child-process environment is another.

Theoretician: The environment carrier treats keys and values as strings. It does not parse a trace ID or enforce a W3C rule. The configured propagator owns that work. This is why the carrier can support several formats without binding every launcher to one tracing ecosystem.

Practitioner: Instrumentation remains the part that creates and ends spans, records status and attributes, and asks the propagator to inject before a launch. Keeping those jobs separate lets a workflow engine interoperate with different OpenTelemetry language implementations instead of learning every format itself.

# S06: Only the carrier changes at a process boundary

Theoretician: Across HTTP, a W3C propagator writes a lowercase traceparent field into request headers. At a process launch, the same configured propagator can write the same value through an environment carrier. The normalized environment key becomes uppercase TRACEPARENT.

Theoretician: The launcher passes that environment to the child at startup. No socket and no side channel are required. Containers need explicit injection because one Pod or step does not inherit another container's environment. Ordinary subprocesses inherit only the environment the parent gives them.

# S07: The process-spawn contract

Practitioner: The safe launch pattern begins with the current context. The parent copies its environment for this child, asks the configured propagator to inject into that copy, and passes the copy to the process API. A separate copy matters when concurrent children belong to different parent spans. Changing the parent's global environment can race and can leak the wrong parent to later work. At child startup, instrumentation extracts the incoming fields and creates a span using that context. If this child starts more work, it repeats the pattern with its current span context.

Practitioner: When the tree breaks, check the contract in order: the launcher injected the context it intended, the process received that environment, the child extracted it before creating the span, and instrumentation actually created the span.

Theoretician: OpenTelemetry helpers may provide a carrier, getter, setter, or another language-specific API. The application or instrumentation still owns the actual process launch and passes the prepared environment to the process mechanism.

# S08: Names normalize, values stay opaque

Theoretician: Environment names have stricter rules than header names. Whichever component performs Set, Get, or Keys applies the same normalization. An empty name becomes an underscore. ASCII letters become uppercase, unsupported characters become underscores, and a leading digit gets an underscore prefix. Set and Get normalize the requested key, while Keys returns normalized names. Traceparent becomes TRACEPARENT. The B3 key x-b3-traceid becomes X_B3_TRACEID.

Theoretician: Only the key changes. The value stays an opaque string until the configured propagator validates and parses it. Consistent normalization keeps the same format usable across platforms, including systems with case-insensitive environment lookup.

# S09: The environment is a trust boundary

Theoretician: A child inherits more than trace context, and every inherited value is input. Treat incoming propagation fields as untrusted. Let the propagator validate them before using them as a parent. At a strong trust boundary, policy may drop the incoming context or start a new trace. Environment variables can be visible to any code in the process and, on some systems, to other users or processes with enough permission.

Practitioner: Baggage means optional application-defined key-value context that can travel with trace context. We scrub it before crossing a trust boundary, avoid secrets entirely, and review tools that print environments in logs, crash reports, or diagnostics. In a multi-tenant runner, a deliberate allow-list should include only the propagation and operating fields the child needs. Context can connect traces without turning an inherited environment into an accidental data channel.

# S10: Demo 1 setup: Argo/Docker build

Practitioner: The first demo uses a pre-staged Argo workflow: clone, build, scan, and push. Our custom Argo launcher integration creates the workflow span and injects its context into each container; stock Argo does not do this. Inside build, a custom Docker/BuildKit bridge forwards context into the build execution environment before instrumented commands start subprocesses; Docker does not do this automatically. Watch both boundaries.

# S11: Demo 1 result: one build trace

Practitioner: Opening the pre-staged result shows clone, build, scan, and push. All four spans share the workflow trace ID. They are siblings because the demo integration injected the workflow span context and each step's instrumentation created its own child span. Opening build reveals the BuildKit execution branch. Our custom bridge explicitly forwarded context into that environment; then instrumented build commands copied and injected their current context for local subprocesses. Without the bridge, the Docker client's environment would not automatically become a Dockerfile RUN environment.

Practitioner: The duration bars show where this run spent its time. Calling a stage abnormally slow needs a baseline, but this view immediately focuses the comparison. In a failed run, span status identifies the failed stage, and nested spans narrow the search to a command or subprocess. A separate build root points to the Argo integration's injection or the build container's startup extraction. A connected build with no BuildKit branch points to the custom bridge or its extraction. A connected BuildKit span with no compiler child points to the command's child launch or compiler instrumentation. If the backend is unavailable, this prepared trace preserves the expected shape and those diagnostic checkpoints.

Theoretician: The environment variable carried parent coordinates; it did not create this tree. Instrumentation created the spans and assigned parentage from extracted context. Each Argo step received a separate injected environment because sibling containers do not inherit environments from one another.

# S12: Demo 2 setup: batch pipeline

Practitioner: The second demo uses a pre-run batch pipeline with extract, transform, and load processes. We will follow it from a source object through a cleaned dataset to a warehouse table. Watch for two kinds of evidence. Propagated context connects the stage spans, while job instrumentation records dataset inputs and outputs on those spans.

# S13: Demo 2 result: connected stages and dataset evidence

Practitioner: The scheduler starts each stage with an environment copy containing the appropriate trace context. Each job extracts that context, creates its stage span, and records the datasets it reads and writes. The trace tree now groups extract, transform, and load under one pipeline run. Extract records the source object and raw output. Transform records that raw dataset as input and the cleaned dataset as output. Load records the cleaned dataset as input and the warehouse table as output.

Practitioner: The failure shapes differ. A stage that starts a new root indicates a propagation or parent-selection problem. A connected stage with no dataset fields points to missing or incomplete job instrumentation. A failed transform span links the operational error to the input and partial output recorded for that run. If the backend is unavailable, this prepared view preserves the span relationships and dataset flow. It also shows the two diagnostic shapes.

Theoretician: TRACEPARENT carries trace coordinates. It carries no dataset graph. The lineage evidence exists because the instrumented jobs add dataset metadata, while propagation makes those records part of one causal run. That gives operators useful run-specific lineage without claiming that tracing supplies catalog history, ownership, impact analysis, or every governance feature of a dedicated lineage system.

# S14: A shared carrier contract reduces custom adapters

Practitioner: Without a shared carrier model, a workflow engine, build tool, CLI, and language library tend to invent pairwise adapters and slightly different variable names. Those conventions become glue that every integration must understand.

Theoretician: A format-agnostic environment carrier gives them one contract for moving string fields. Configured propagators still choose W3C Trace Context, B3, or another supported format. Launchers can focus on process boundaries, and instrumentation can focus on spans. The result is broader interoperability with less custom glue and fewer fragmented trace conventions across CI/CD and batch systems.

# S15: Release Candidate: feedback can shape Stable guidance

Theoretician: As checked on September 11, 2026, the OpenTelemetry environment-variable carrier document is a Release Candidate. That means it has substantial review and implementations in several languages, while remaining open to change. Stabilization waits until at least November 2 and until fourteen days pass without a new related issue. A significant specification update restarts that fourteen-day period.

Practitioner: Useful reports include the operating system, runtime, configured propagator, expected and actual behavior, and whether the problem involves concurrent children, normalization, or a trust boundary. Real workflows can still improve the guidance and examples.

# S16: The process-boundary checklist

Theoretician: Keep the layers separate: instrumentation creates spans, a propagator encodes context, and the environment carries its fields.

Practitioner: At every launch, copy and filter the environment, inject the current context, extract at child startup, and verify the resulting parent. That contract turns process chains into one trace without a custom format.

# S17: Questions and troubleshooting

Practitioner: Thank you. We have five minutes for questions and troubleshooting.

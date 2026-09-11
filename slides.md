---
theme: default
id: S01
title: "Trace Context Beyond HTTP: Environment Variables as OpenTelemetry Propagation Carriers"
info: |
  A 20-minute, two-presenter conference talk about OpenTelemetry trace-context
  propagation through child-process environments.
class: otel-deck
mdc: true
colorSchema: dark
aspectRatio: 16/9
canvasWidth: 1280
fonts:
  sans: Inter
  mono: JetBrains Mono
---

<div class="cover-layout">
  <div class="cover-copy">
    <h1>Trace context<br><span>beyond HTTP</span></h1>
    <p class="cover-subtitle">Environment variables as OpenTelemetry propagation carriers</p>
  </div>
  <div class="speaker-line" aria-label="Presenters">
    <div class="speaker practice">
      <img class="speaker-photo" src="/speakers/alan-clucas-github.jpg" alt="Alan Clucas">
      <div class="speaker-copy">
        <span>Practitioner</span>
        <b>Alan Clucas</b>
        <small><span class="speaker-handle">@Joibel</span><br>Pipekit, Argo contributor</small>
      </div>
    </div>
    <div class="speaker theory">
      <img class="speaker-photo" src="/speakers/robert-pajak-github.jpg" alt="Robert Pająk">
      <div class="speaker-copy">
        <span>Theoretician</span>
        <b>Robert Pająk</b>
        <small><span class="speaker-handle">@pellared</span><br>Splunk, OpenTelemetry contributor</small>
      </div>
    </div>
  </div>
</div>

<!--
Spoken outline:
Practitioner: I’m Alan Clucas, Joibel on GitHub. I work at Pipekit and contribute to Argo. We’ll follow one trace through shells, containers, build tools, and batch jobs.

Theoretician: I’m Robert Pająk, pellared on GitHub. I work at Splunk and contribute to OpenTelemetry. I’ll frame the model and constraints. Alan will show the operational consequences.

Delivery notes:
- Time: 00:25
- Handoff: Practitioner to Theoretician, 00:05.
- Sources: README.md; Alan profile and portrait: https://github.com/Joibel, https://avatars.githubusercontent.com/u/1827156?s=512&v=4; Robert profile and portrait: https://github.com/pellared, https://avatars.githubusercontent.com/u/5067549?s=512&v=4.
-->

---
layout: default
id: S02
---

<h2 class="slide-title">Process launches leave gaps in the trace</h2>

<div class="broken-workflow" aria-label="One workflow split into unrelated traces">
  <div class="workflow-label">one workflow run</div>
  <div class="stage stage-1"><b>clone</b><small>shell</small></div>
  <div class="stage stage-2"><b>build</b><small>container</small></div>
  <div class="stage stage-3"><b>scan</b><small>CLI</small></div>
  <div class="stage stage-4"><b>push</b><small>process</small></div>
  <div class="stage-link link-1 broken"></div>
  <div class="stage-link link-2 broken"></div>
  <div class="stage-link link-3 broken"></div>
  <div class="process-cut cut-0"><span>new process</span></div>
  <div class="process-cut cut-1"><span>new process</span></div>
  <div class="process-cut cut-2"><span>new process</span></div>
</div>

<div class="fragmented-traces">
  <div class="root-span cyan"><span>trace A</span><b>clone</b></div>
  <div class="root-span amber"><span>trace B</span><b>build</b></div>
  <div class="root-span coral"><span>trace C</span><b>scan</b></div>
  <div class="root-span violet"><span>trace D</span><b>push</b></div>
</div>

<p class="takeaway">The backend sees four roots. The operator must reconstruct one run.</p>

<!--
Spoken outline:
Practitioner: Picture a delivery pipeline. We expect one trace, a connected record of the run. The workflow runner clones a repository, starts a container build, launches a scanner, and finally pushes the image. An instrumented process can emit a span, a timed record of one operation. Without incoming parent coordinates, each span begins a separate top-level trace. The backend shows unrelated results, so the operator has to reconstruct one run from timestamps and labels.

Practitioner: Network services carry parent coordinates in HTTP headers, and queues can use message metadata. A shell or container launch uses neither. This boundary is common. CI jobs start scripts. Workflow engines start containers. Build tools fork compilers and test processes. Batch schedulers launch programs.

Theoretician: To keep the trace connected, the child needs a small description of where its work belongs. First we need a precise picture of that family relationship.

Delivery notes:
- Time: 01:25
- Handoff: Practitioner to Theoretician for the model bridge, 00:08.
- Sources: README.md; https://opentelemetry.io/blog/2026/environment-variable-context-propagation/
-->

---
layout: default
id: S03
---

<h2 class="slide-title">A trace is a family tree of work</h2>

<div class="family-layout">
  <div class="trace-tree" aria-label="Workflow trace with parent and child spans">
    <div class="tree-node root">
      <span class="node-kind">root span</span>
      <b>workflow</b>
      <code>span 18a4</code>
    </div>
    <div class="tree-trunk"></div>
    <div class="tree-fork"></div>
    <div class="tree-node child build">
      <span class="node-kind">child span</span>
      <b>build</b>
      <code>span 4c21<br>parent 18a4</code>
    </div>
    <div class="tree-node child scan">
      <span class="node-kind">child span</span>
      <b>scan</b>
      <code>span 7b90<br>parent 18a4</code>
    </div>
    <div class="tree-drop"></div>
    <div class="tree-node grandchild">
      <span class="node-kind">child span</span>
      <b>compiler</b>
      <code>parent 4c21</code>
    </div>
  </div>
  <div class="trace-definition">
    <div class="big-label">TRACE</div>
    <p>All spans share</p>
    <code class="trace-id">trace_id<br>4bf92f…4736</code>
    <div class="definition-rule"></div>
    <p><b>span</b> = one timed operation</p>
    <p><b>parent</b> = work that caused it</p>
  </div>
</div>

<!--
Spoken outline:
Theoretician: A span is a timed record of one operation, such as the whole workflow, one build step, or one compiler invocation. A trace is the set of spans that describe one end-to-end operation. Each span has at most one parent. A child span represents work caused by its parent, and siblings share the same parent.

Theoretician: In this tree, workflow is the root because it has no parent. Build and scan are children of workflow. Compiler is a child of build. Every span has its own span identifier, while every span in the tree shares one trace identifier. That shared trace ID and the parent links let a backend assemble the tree even when the spans came from different processes.

Delivery notes:
- Time: 01:15
- Handoff: None.
- Sources: https://opentelemetry.io/docs/concepts/signals/traces/; https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/trace/api.md#span-creation
-->

---
layout: default
id: S04
---

<h2 class="slide-title">Context connects, telemetry reports</h2>

<div class="context-scene" aria-label="Trace context flowing between processes while spans export separately">
  <div class="process parent-process">
    <span>parent process</span>
    <b>build span</b>
  </div>
  <div class="context-transfer">
    <span class="verb inject">inject</span>
    <div class="context-token">
      <small>CARRIER: CHILD ENVIRONMENT</small>
      <b>trace context</b>
      <code>trace id<br>span id<br>flags</code>
    </div>
    <span class="verb extract">extract</span>
  </div>
  <div class="process child-process">
    <span>child process</span>
    <b>compiler span</b>
    <small>parent = build</small>
  </div>
  <div class="context-connector left"></div>
  <div class="context-connector right"></div>
  <div class="export-path path-parent"></div>
  <div class="export-path path-child"></div>
  <div class="backend"><b>trace backend</b><span>completed spans</span></div>
</div>

<div class="split-statement">
  <span><b>Propagation</b> moves parent coordinates</span>
  <span><b>Instrumentation</b> creates the next span</span>
</div>

<!--
Spoken outline:
Theoretician: Trace context is the small set of identifiers and settings that describes the current position in a trace. It includes the trace ID, the current span ID, and small tracing flags. Propagation means moving that context across a boundary so the receiver can choose the right parent. The carrier is the medium that holds the propagation fields while they move.

Theoretician: The context is different from the span data that a backend receives. An exporter is the component that sends completed spans to that backend. Injection writes context into a carrier. Extraction reads it into the child process. Then instrumentation creates a child span and uses the extracted context as its parent.

Practitioner: That separation gives us a useful diagnostic. If spans exist as separate roots, instrumentation probably ran but propagation failed. If no child span exists at all, carrying context would not create one. We still need instrumentation around the work.

Delivery notes:
- Time: 01:15
- Handoff: Theoretician to Practitioner for the diagnostic consequence, 00:08.
- Sources: https://opentelemetry.io/docs/concepts/context-propagation/; https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/api-propagators.md
-->

---
layout: default
id: S05
---

<h2 class="slide-title">Carrier, propagator, format, and instrumentation</h2>

<div class="layer-scene" aria-label="Four terms separated across three responsibilities in context propagation">
  <div class="layer-row instrumentation-row">
    <div class="layer-name"><span>creates work records</span><b>INSTRUMENTATION</b></div>
    <div class="mini-span parent-span"><b>build</b><small>current span</small></div>
    <div class="layer-line"></div>
    <div class="mini-span child-span"><b>compiler</b><small>new child span</small></div>
  </div>
  <div class="layer-row propagator-row">
    <div class="layer-name"><span>encodes and validates</span><b>PROPAGATOR</b></div>
    <span class="operation">inject</span>
    <div class="format-block">
      <span class="format-label">FORMAT</span>
      <b>W3C Trace Context</b>
      <small>or B3, or another configured format</small>
    </div>
    <span class="operation">extract</span>
  </div>
  <div class="layer-row carrier-row">
    <div class="layer-name"><span>moves opaque strings</span><b>CARRIER</b></div>
    <code>TRACEPARENT=[opaque W3C value]</code>
    <span class="carrier-caption">child environment</span>
  </div>
</div>

<p class="takeaway accent">The launcher can stay format-agnostic.</p>

<!--
Spoken outline:
Theoretician: Four terms describe three responsibilities. Instrumentation creates and ends spans. A propagator is code that injects and extracts propagation fields. The propagation format defines those field names and values. W3C Trace Context uses traceparent and optionally tracestate, while B3 uses different fields. The carrier holds the resulting strings across a boundary. HTTP headers are one carrier, and a child-process environment is another.

Theoretician: The environment carrier treats keys and values as strings. It does not parse a trace ID or enforce a W3C rule. The configured propagator owns that work. This is why the carrier can support several formats without binding every launcher to one tracing ecosystem.

Practitioner: Instrumentation remains the part that creates and ends spans, records status and attributes, and asks the propagator to inject before a launch. Keeping those jobs separate lets a workflow engine interoperate with different OpenTelemetry language implementations instead of learning every format itself.

Delivery notes:
- Time: 01:25
- Handoff: Theoretician to Practitioner for the implementation consequence, 00:10.
- Sources: https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md; https://www.w3.org/TR/trace-context/
-->

---
layout: default
id: S06
---

<h2 class="slide-title">Only the carrier changes at a process boundary</h2>

<div class="carrier-comparison" aria-label="HTTP header carrier compared with an environment variable carrier">
  <div class="shared-chain">
    <div class="shared-context"><span>current Context</span><code>trace 4bf92f…4736</code></div>
    <div class="chain-link"></div>
    <div class="shared-propagator"><span>configured propagator</span><b>W3C Trace Context</b></div>
  </div>
  <div class="carrier-lanes">
    <div class="carrier-lane http-lane">
      <span class="lane-label">HTTP request<br><small>request headers</small></span>
      <code><i>traceparent:</i> 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01</code>
    </div>
    <div class="carrier-lane env-lane">
      <span class="lane-label">process launch<br><small>child environment</small></span>
      <code><i>TRACEPARENT=</i>00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01</code>
    </div>
  </div>
</div>

<p class="takeaway">Same context value. Different carrier and key normalization.</p>

<!--
Spoken outline:
Theoretician: Across HTTP, a W3C propagator writes a lowercase traceparent field into request headers. At a process launch, the same configured propagator can write the same value through an environment carrier. The normalized environment key becomes uppercase TRACEPARENT.

Theoretician: The launcher passes that environment to the child at startup. No socket and no side channel are required. Containers need explicit injection because one Pod or step does not inherit another container's environment. Ordinary subprocesses inherit only the environment the parent gives them.

Delivery notes:
- Time: 00:55
- Handoff: None.
- Sources: https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md; https://www.w3.org/TR/trace-context/#traceparent-header
-->

---
layout: default
id: S07
---

<h2 class="slide-title">The process-spawn contract</h2>

<div class="spawn-sequence" aria-label="Six-step process launch sequence">
  <div class="ownership parent-owner">parent responsibility</div>
  <div class="ownership child-owner">child responsibility</div>
  <div class="spawn-boundary"><span>process starts</span></div>
  <div class="spawn-step step-1"><i>1</i><b>current<br>Context</b></div>
  <div class="spawn-step step-2"><i>2</i><b>copy + filter<br>environment</b><small>one copy per child</small></div>
  <div class="spawn-step step-3"><i>3</i><b>inject<br>fields</b></div>
  <div class="spawn-step step-4"><i>4</i><b>spawn with<br>that copy</b></div>
  <div class="spawn-step step-5"><i>5</i><b>extract at<br>startup</b></div>
  <div class="spawn-step step-6"><i>6</i><b>create<br>child span</b></div>
  <div class="sequence-line"></div>
</div>

<div class="diagnostic-order">
  <b>debug in this order</b>
  <span>inject</span><i></i><span>receive</span><i></i><span>extract</span><i></i><span>instrument</span>
</div>

<!--
Spoken outline:
Practitioner: The safe launch pattern begins with the current context. The parent copies its environment for this child, asks the configured propagator to inject into that copy, and passes the copy to the process API. A separate copy matters when concurrent children belong to different parent spans. Changing the parent's global environment can race and can leak the wrong parent to later work. At child startup, instrumentation extracts the incoming fields and creates a span using that context. If this child starts more work, it repeats the pattern with its current span context.

Practitioner: When the tree breaks, check the contract in order: the launcher injected the context it intended, the process received that environment, the child extracted it before creating the span, and instrumentation actually created the span.

Theoretician: OpenTelemetry helpers may provide a carrier, getter, setter, or another language-specific API. The application or instrumentation still owns the actual process launch and passes the prepared environment to the process mechanism.

Delivery notes:
- Time: 01:20
- Handoff: Practitioner to Theoretician once for the closing specification boundary, 00:08.
- Sources: https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md#operational-guidance; https://opentelemetry.io/blog/2026/environment-variable-context-propagation/
-->

---
layout: default
id: S08
---

<h2 class="slide-title">Names normalize, values stay opaque</h2>

<div class="normalization-scene" aria-label="Examples of environment variable key normalization">
  <div class="normalization-example">
    <code class="source-key">traceparent</code>
    <div class="normalize-line"><span>normalize key</span></div>
    <code class="target-key">TRACEPARENT</code>
  </div>
  <div class="normalization-example second">
    <code class="source-key">x-b3-traceid</code>
    <div class="normalize-line"><span>normalize key</span></div>
    <code class="target-key">X_B3_TRACEID</code>
  </div>
  <div class="normalization-rules">
    <span>ASCII letters become uppercase</span>
    <span>unsupported characters become <code>_</code></span>
    <span>a leading digit gets a <code>_</code> prefix</span>
  </div>
</div>

<p class="opaque-value">VALUE <code>[opaque string]</code> <b>unchanged until the propagator parses it</b></p>

<!--
Spoken outline:
Theoretician: Environment names have stricter rules than header names. Whichever component performs Set, Get, or Keys applies the same normalization. An empty name becomes an underscore. ASCII letters become uppercase, unsupported characters become underscores, and a leading digit gets an underscore prefix. Set and Get normalize the requested key, while Keys returns normalized names. Traceparent becomes TRACEPARENT. The B3 key x-b3-traceid becomes X_B3_TRACEID.

Theoretician: Only the key changes. The value stays an opaque string until the configured propagator validates and parses it. Consistent normalization keeps the same format usable across platforms, including systems with case-insensitive environment lookup.

Delivery notes:
- Time: 00:50
- Handoff: None.
- Sources: https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md#key-name-normalization
-->

---
layout: default
id: S09
---

<h2 class="slide-title">The environment is a trust boundary</h2>

<div class="trust-scene" aria-label="Filtering an inherited environment at a trust boundary">
  <div class="env-list parent-env">
    <span class="env-title">parent environment</span>
    <code class="safe">TRACEPARENT</code>
    <code class="review">BAGGAGE</code><small>extra key-value context</small>
    <code class="blocked">DEPLOY_TOKEN</code>
  </div>
  <div class="trust-gate">
    <span>trust boundary</span>
    <b>allow-list<br>+ scrub</b>
    <i class="block-mark">×</i>
  </div>
  <div class="trust-flow flow-in"></div>
  <div class="trust-flow flow-out"></div>
  <div class="env-list child-env">
    <span class="env-title">child environment</span>
    <code class="safe">TRACEPARENT</code>
    <code class="review">BAGGAGE</code><small>reviewed fields only</small>
    <small>validate as untrusted input</small>
  </div>
  <div class="leak-lines">
    <div class="leak-line l1"></div><span class="leak-label logs">logs</span>
    <div class="leak-line l2"></div><span class="leak-label crash">crash reports</span>
    <div class="leak-line l3"></div><span class="leak-label code">other code</span>
  </div>
</div>

<p class="takeaway warning">Propagation fields are context, never a secret channel.</p>

<!--
Spoken outline:
Theoretician: A child inherits more than trace context, and every inherited value is input. Treat incoming propagation fields as untrusted. Let the propagator validate them before using them as a parent. At a strong trust boundary, policy may drop the incoming context or start a new trace. Environment variables can be visible to any code in the process and, on some systems, to other users or processes with enough permission.

Practitioner: Baggage means optional application-defined key-value context that can travel with trace context. We scrub it before crossing a trust boundary, avoid secrets entirely, and review tools that print environments in logs, crash reports, or diagnostics. In a multi-tenant runner, a deliberate allow-list should include only the propagation and operating fields the child needs. Context can connect traces without turning an inherited environment into an accidental data channel.

Delivery notes:
- Time: 01:10
- Handoff: Theoretician to Practitioner for safe deployment choices, 00:10.
- Sources: https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md#security; https://www.w3.org/TR/trace-context/#privacy-considerations
-->

---
layout: default
id: S10
---

<h2 class="slide-title">Demo 1 setup: Argo/Docker build</h2>

<div class="demo-kicker"><span>DEMO WALKTHROUGH</span><b>one workflow, two kinds of process boundary</b></div>

<div class="argo-scene" aria-label="A custom Argo workflow integration injecting context into four step containers">
  <div class="argo-controller"><b>Argo workflow integration</b><small>demo hook injects workflow context</small></div>
  <div class="argo-rail"><i class="argo-stem"></i><i class="argo-drop d1"></i><i class="argo-drop d2"></i><i class="argo-drop d3"></i><i class="argo-drop d4"></i></div>
  <div class="container c1"><b>clone</b><code>TRACEPARENT</code></div>
  <div class="container c2"><b>build</b><code>TRACEPARENT</code><small>BuildKit bridge</small></div>
  <div class="container c3"><b>scan</b><code>TRACEPARENT</code></div>
  <div class="container c4"><b>push</b><code>TRACEPARENT</code></div>
  <div class="empty-trace"><span>pre-staged trace</span><b>run selected<br>spans collapsed</b></div>
</div>

<p class="watch-line">Watch the Argo injection, then the custom BuildKit bridge.</p>

<!--
Spoken outline:
Practitioner: The first demo uses a pre-staged Argo workflow: clone, build, scan, and push. Our custom Argo launcher integration creates the workflow span and injects its context into each container; stock Argo does not do this. Inside build, a custom Docker/BuildKit bridge forwards context into the build execution environment before instrumented commands start subprocesses; Docker does not do this automatically. Watch both boundaries.

Delivery notes:
- Time: 00:35, including setup and transition to the live view.
- Handoff: None.
- Before action: Keep the pre-run workflow graph and its collapsed trace result visible.
- Demo cue: Open the pre-staged trace and expand the build branch. Do not wait for a cluster run inside this timebox.
- Fallback: Continue directly to the prepared S11 reconstruction.
- Sources: README.md; https://opentelemetry.io/blog/2026/environment-variable-context-propagation/#argo-workflows; https://docs.docker.com/build/concepts/overview/
-->

---
layout: default
id: S11
---

<h2 class="slide-title">Demo 1 result: one build trace</h2>

<div class="waterfall" aria-label="Prepared trace waterfall for an Argo image build">
  <div class="waterfall-head"><span>prepared trace shape</span><small>slow-run example, relative timing</small></div>
  <div class="wf-row level-0"><span class="wf-name">argo.workflow</span><div class="wf-track"><i class="bar cyan-bar" style="--start:0%;--width:100%"></i></div></div>
  <div class="wf-row level-1"><span class="wf-name">clone</span><div class="wf-track"><i class="bar cyan-bar" style="--start:3%;--width:18%"></i></div></div>
  <div class="wf-row level-1 focus-slow"><span class="wf-name">build</span><div class="wf-track"><i class="bar amber-bar" style="--start:22%;--width:55%"></i></div><b>slow branch</b></div>
  <div class="wf-row level-2 bridge-row"><span class="wf-name">BuildKit RUN</span><div class="wf-track"><i class="bar amber-soft" style="--start:26%;--width:48%"></i></div><b>custom bridge</b></div>
  <div class="wf-row level-3"><span class="wf-name">compiler</span><div class="wf-track"><i class="bar amber-soft" style="--start:32%;--width:36%"></i></div></div>
  <div class="wf-row level-1"><span class="wf-name">scan</span><div class="wf-track"><i class="bar cyan-bar" style="--start:78%;--width:13%"></i></div></div>
  <div class="wf-row level-1"><span class="wf-name">push</span><div class="wf-track"><i class="bar violet-bar" style="--start:92%;--width:8%"></i></div></div>
</div>

<div class="demo-diagnostic">
  <span><b>slow run</b> compare build duration with a baseline</span>
  <span class="error-note"><b>failed run</b> follow <code>status=ERROR</code></span>
  <span><b>broken tree</b> inspect Argo inject, bridge, and child launch</span>
</div>

<!--
Spoken outline:
Practitioner: Opening the pre-staged result shows clone, build, scan, and push. All four spans share the workflow trace ID. They are siblings because the demo integration injected the workflow span context and each step's instrumentation created its own child span. Opening build reveals the BuildKit execution branch. Our custom bridge explicitly forwarded context into that environment; then instrumented build commands copied and injected their current context for local subprocesses. Without the bridge, the Docker client's environment would not automatically become a Dockerfile RUN environment.

Practitioner: The duration bars show where this run spent its time. Calling a stage abnormally slow needs a baseline, but this view immediately focuses the comparison. In a failed run, span status identifies the failed stage, and nested spans narrow the search to a command or subprocess. A separate build root points to the Argo integration's injection or the build container's startup extraction. A connected build with no BuildKit branch points to the custom bridge or its extraction. A connected BuildKit span with no compiler child points to the command's child launch or compiler instrumentation. If the backend is unavailable, this prepared trace preserves the expected shape and those diagnostic checkpoints.

Theoretician: The environment variable carried parent coordinates; it did not create this tree. Instrumentation created the spans and assigned parentage from extracted context. Each Argo step received a separate injected environment because sibling containers do not inherit environments from one another.

Delivery notes:
- Time: 02:45, including interaction, interpretation, and return from the live view.
- Handoff: Practitioner to Theoretician for the closing carrier and container distinction, 00:10.
- Expected result: One workflow trace with clone, build, scan, push, a bridged BuildKit execution branch, and the relevant subprocesses below build.
- Failure insight: A separate build root indicates Argo injection or startup extraction; a missing BuildKit branch indicates the custom bridge; a missing compiler child indicates child launch or instrumentation.
- Fallback: Present this waterfall. It is a schematic of expected relationships and relative timing, not a captured benchmark.
- Sources: README.md; https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md#process-spawning; https://docs.docker.com/build/concepts/overview/; https://docs.docker.com/reference/cli/docker/buildx/build/#set-build-time-variables---build-arg
-->

---
layout: default
id: S12
---

<h2 class="slide-title">Demo 2 setup: batch pipeline</h2>

<div class="demo-kicker"><span>DEMO WALKTHROUGH</span><b>one run, three stage processes, four dataset states</b></div>

<div class="batch-scene" aria-label="Batch scheduler launching extract, transform, and load processes">
  <div class="scheduler"><b>batch scheduler</b><small>injects context per launch</small></div>
  <div class="batch-rail"></div>
  <div class="batch-stage extract"><b>extract</b><small>process</small></div>
  <div class="batch-stage transform"><b>transform</b><small>process</small></div>
  <div class="batch-stage load"><b>load</b><small>process</small></div>
  <div class="data-node source">source object</div>
  <div class="data-node raw">orders_raw</div>
  <div class="data-node clean">orders_clean</div>
  <div class="data-node warehouse">warehouse.orders</div>
  <div class="data-link d1"></div><div class="data-link d2"></div><div class="data-link d3"></div>
</div>

<div class="evidence-key"><span class="context-dot"></span>context connects spans <span class="metadata-dot"></span>job instrumentation records datasets</div>

<!--
Spoken outline:
Practitioner: The second demo uses a pre-run batch pipeline with extract, transform, and load processes. We will follow it from a source object through a cleaned dataset to a warehouse table. Watch for two kinds of evidence. Propagated context connects the stage spans, while job instrumentation records dataset inputs and outputs on those spans.

Delivery notes:
- Time: 00:35, including setup and transition to the live view.
- Handoff: None.
- Before action: Keep the pre-run batch definition and its collapsed or filtered trace result visible.
- Demo cue: Open the pre-staged trace by run identifier. Do not wait for a batch run inside this timebox.
- Fallback: Continue directly to the prepared S13 reconstruction.
- Sources: README.md; https://opentelemetry.io/blog/2026/environment-variable-context-propagation/
-->

---
layout: default
id: S13
---

<h2 class="slide-title">Demo 2 result: connected stages and dataset evidence</h2>

<div class="lineage-trace" aria-label="Trace tree with application-recorded dataset inputs and outputs">
  <div class="lineage-row pipeline-row">
    <div class="lineage-span"><span>root span</span><b>pipeline.run</b></div>
    <div class="lineage-meta"><small>run</small><code>batch-2026-09-11</code></div>
  </div>
  <div class="lineage-row extract-row">
    <div class="lineage-branch"></div>
    <div class="lineage-span"><span>child span</span><b>extract</b></div>
    <div class="lineage-meta"><small>input</small><code class="dataset source-data">source object</code><small>output</small><code class="dataset raw-data">orders_raw</code></div>
  </div>
  <div class="lineage-row transform-row">
    <div class="lineage-branch"></div>
    <div class="lineage-span"><span>child span</span><b>transform</b></div>
    <div class="lineage-meta"><small>input</small><code class="dataset raw-data">orders_raw</code><small>output</small><code class="dataset clean-data">orders_clean</code></div>
  </div>
  <div class="lineage-row load-row">
    <div class="lineage-branch last"></div>
    <div class="lineage-span"><span>child span</span><b>load</b></div>
    <div class="lineage-meta"><small>input</small><code class="dataset clean-data">orders_clean</code><small>output</small><code class="dataset warehouse-data">warehouse.orders</code></div>
  </div>
</div>

<div class="lineage-scope">
  <span><b>Propagation</b> connects this run</span>
  <span><b>Job instrumentation</b> records the dataset fields</span>
  <small>Scope: run-specific operational causality. Catalog and governance capabilities remain separate.</small>
</div>

<div class="lineage-diagnostics">
  <span><b>new root</b> propagation or parent selection</span>
  <span><b>connected span, fields missing</b> job instrumentation</span>
</div>

<!--
Spoken outline:
Practitioner: The scheduler starts each stage with an environment copy containing the appropriate trace context. Each job extracts that context, creates its stage span, and records the datasets it reads and writes. The trace tree now groups extract, transform, and load under one pipeline run. Extract records the source object and raw output. Transform records that raw dataset as input and the cleaned dataset as output. Load records the cleaned dataset as input and the warehouse table as output.

Practitioner: The failure shapes differ. A stage that starts a new root indicates a propagation or parent-selection problem. A connected stage with no dataset fields points to missing or incomplete job instrumentation. A failed transform span links the operational error to the input and partial output recorded for that run. If the backend is unavailable, this prepared view preserves the span relationships and dataset flow. It also shows the two diagnostic shapes.

Theoretician: TRACEPARENT carries trace coordinates. It carries no dataset graph. The lineage evidence exists because the instrumented jobs add dataset metadata, while propagation makes those records part of one causal run. That gives operators useful run-specific lineage without claiming that tracing supplies catalog history, ownership, impact analysis, or every governance feature of a dedicated lineage system.

Delivery notes:
- Time: 02:45, including interaction, interpretation, and return from the live view.
- Handoff: Practitioner to Theoretician for the lineage boundary, 00:12.
- Expected result: One pipeline trace with stage spans and job-recorded input and output metadata.
- Failure insight: A separate root indicates propagation or parent selection. Missing fields on a connected span indicate instrumentation.
- Fallback: Present this reconstruction and its diagnostic strip. Dataset names are illustrative, and the diagram claims no standard semantic-convention key names.
- Sources: README.md; https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/trace/api.md#span
-->

---
layout: default
id: S14
---

<h2 class="slide-title">A shared carrier contract reduces custom adapters</h2>

<div class="interop-scene" aria-label="Custom pairwise adapters compared with a shared environment carrier contract">
  <div class="interop-side tangled">
    <span class="side-title">pairwise conventions</span>
    <div class="tool t1">workflow</div><div class="tool t2">build tool</div><div class="tool t3">CLI</div><div class="tool t4">OTel library</div>
    <div class="adapter a1">CUSTOM_TRACE</div><div class="adapter a2">BUILD_PARENT</div><div class="adapter a3">TRACE_ID</div>
    <div class="tangle-line tl1"></div><div class="tangle-line tl2"></div><div class="tangle-line tl3"></div><div class="tangle-line tl4"></div>
  </div>
  <div class="interop-divider"></div>
  <div class="interop-side shared">
    <span class="side-title">shared carrier contract</span>
    <div class="shared-tools"><span>CI/CD</span><span>workflow engine</span><span>build tool</span><span>CLI</span></div>
    <div class="tool-bus"></div>
    <div class="shared-carrier"><b>environment carrier</b><small>normalized keys, opaque values</small></div>
    <div class="carrier-propagator-link"></div>
    <div class="shared-propagator-box"><b>configured propagator</b><small>selects format: W3C · B3 · …</small></div>
    <div class="instrument-link"></div>
    <div class="otel-instrumentation">OpenTelemetry instrumentation</div>
  </div>
</div>

<p class="takeaway accent">Fewer custom encodings. More tools can preserve the same trace.</p>

<!--
Spoken outline:
Practitioner: Without a shared carrier model, a workflow engine, build tool, CLI, and language library tend to invent pairwise adapters and slightly different variable names. Those conventions become glue that every integration must understand.

Theoretician: A format-agnostic environment carrier gives them one contract for moving string fields. Configured propagators still choose W3C Trace Context, B3, or another supported format. Launchers can focus on process boundaries, and instrumentation can focus on spans. The result is broader interoperability with less custom glue and fewer fragmented trace conventions across CI/CD and batch systems.

Delivery notes:
- Time: 00:55
- Handoff: Practitioner to Theoretician for the shared contract, 00:10.
- Sources: README.md; https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md
-->

---
layout: default
id: S15
---

<h2 class="slide-title">Release Candidate: feedback can shape Stable guidance</h2>

<div class="status-scene" aria-label="Release Candidate feedback timeline">
  <div class="status-mark"><span>STATUS</span><b>RELEASE<br>CANDIDATE</b><small>reviewed, still open to change<br>checked 11 Sep 2026</small></div>
  <div class="status-timeline">
    <div class="timeline-line"></div>
    <div class="timeline-point now"><i></i><b>now</b><span>implementations in several languages</span></div>
    <div class="timeline-point feedback"><i></i><b>at least 2 Nov 2026</b><span>published feedback period</span></div>
    <div class="timeline-point stable"><i></i><b>Stable</b><span>next status after criteria and quiet period</span></div>
  </div>
</div>

<div class="feedback-call">
  <img class="feedback-qr" src="/env-carriers-qr.svg" alt="QR code for the OpenTelemetry feedback article">
  <b>Test a real process chain</b>
  <span>report portability, concurrency, normalization, and trust-boundary findings</span>
  <code>opentelemetry.io/blog/2026/<br>environment-variable-context-propagation/</code>
</div>

<!--
Spoken outline:
Theoretician: As checked on September 11, 2026, the OpenTelemetry environment-variable carrier document is a Release Candidate. That means it has substantial review and implementations in several languages, while remaining open to change. Stabilization waits until at least November 2 and until fourteen days pass without a new related issue. A significant specification update restarts that fourteen-day period.

Practitioner: Useful reports include the operating system, runtime, configured propagator, expected and actual behavior, and whether the problem involves concurrent children, normalization, or a trust boundary. Real workflows can still improve the guidance and examples.

Delivery notes:
- Time: 00:45
- Handoff: Theoretician to Practitioner for the useful feedback shape, 00:08.
- Status checked: 2026-09-11.
- Sources: https://github.com/open-telemetry/opentelemetry-specification/blob/eec6fadba46a5002f55ff88ce4405d58a1aa4aec/specification/context/env-carriers.md; https://opentelemetry.io/blog/2026/environment-variable-context-propagation/; https://github.com/open-telemetry/opentelemetry-specification/issues/5040
-->

---
layout: default
id: S16
---

<h2 class="slide-title">The process-boundary checklist</h2>

<div class="recap-scene" aria-label="Checklist for context propagation across a process boundary">
  <div class="recap-parent"><span>PARENT</span><b>current span</b></div>
  <div class="recap-step"><i>1</i><b>copy + filter</b><small>one environment per child</small></div>
  <div class="recap-step"><i>2</i><b>inject</b><small>configured propagator</small></div>
  <div class="recap-cut"><span>process boundary</span></div>
  <div class="recap-step"><i>3</i><b>extract</b><small>at child startup</small></div>
  <div class="recap-step"><i>4</i><b>create span</b><small>use extracted parent</small></div>
  <div class="recap-child"><span>CHILD</span><b>connected span</b></div>
  <div class="recap-line"></div>
</div>

<p class="three-layer-recap"><b>instrumentation creates</b><span>propagator encodes</span><span>environment carries</span></p>

<!--
Spoken outline:
Theoretician: Keep the layers separate: instrumentation creates spans, a propagator encodes context, and the environment carries its fields.

Practitioner: At every launch, copy and filter the environment, inject the current context, extract at child startup, and verify the resulting parent. That contract turns process chains into one trace without a custom format.

Delivery notes:
- Time: 00:30
- Handoff: Theoretician to Practitioner for the launch checklist, 00:06.
- Sources: https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md
-->

---
layout: default
id: S17
---

<div class="questions-slide">
  <div class="question-trace"><i></i><i></i><i></i><i></i></div>
  <h1>Questions</h1>
  <p>Five minutes for questions and troubleshooting</p>
  <div class="question-prompts">
    <span>Where does your trace break?</span>
    <span>Who owns the child launch?</span>
  </div>
</div>

<!--
Spoken outline:
Practitioner: Thank you. We have five minutes for questions and troubleshooting.

Delivery notes:
- Time: 00:05 presentation close. Keep this slide visible for the separate 05:00 Q&A window.
- Handoff: None.
- Sources: None.
-->

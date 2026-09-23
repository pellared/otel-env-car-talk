---
theme: default
id: S01
title: "Trace Context Beyond HTTP: Environment Variables as OpenTelemetry Propagation Carriers"
info: |
  A 20-minute, two-presenter conference talk about OpenTelemetry trace-context
  propagation through child-process environments.
class: intro-deck
transition: slide-left
mdc: true
colorSchema: auto
aspectRatio: 16/9
canvasWidth: 1280
---

<div class="title-slide">
  <div class="title-copy">
    <h1>Trace Context<br><span>Beyond HTTP</span></h1>
    <p class="title-subtitle">Environment variables as OpenTelemetry propagation carriers</p>
  </div>

  <div class="author-list" aria-label="Presenters">
    <a href="https://github.com/pellared/" class="author author-theory">
      <img class="profile-photo" src="https://avatars.githubusercontent.com/u/5067549?s=512&amp;v=4" alt="Robert Pająk’s GitHub profile photo">
      <span class="author-copy">
        <b>Robert Pająk</b>
        <span class="author-url">github.com/pellared</span>
      </span>
    </a>
    <a href="https://github.com/Joibel" class="author author-practice">
      <img class="profile-photo" src="https://avatars.githubusercontent.com/u/1827156?s=512&amp;v=4" alt="Alan Clucas’s GitHub profile photo">
      <span class="author-copy">
        <b>Alan Clucas</b>
        <span class="author-url">github.com/Joibel</span>
      </span>
    </a>
  </div>
</div>

<!--
Spoken outline:
Theoretician: Welcome. We are Robert Pająk and Alan Clucas. This talk follows trace context across process boundaries, from one process into the next.

Practitioner: We will connect the model to the realities of launching containers, build steps, and batch jobs.

Delivery notes:
- Time: 00:20.
- Handoff: Theoretician to Practitioner, 00:05.
- Display: Follow the viewer’s color preference. In Slidev, use the built-in mode control or press `D` to toggle light and dark.
- Sources: README.md; presenter profiles and profile photos: https://github.com/pellared/, https://avatars.githubusercontent.com/u/5067549?s=512&v=4, https://github.com/Joibel, https://avatars.githubusercontent.com/u/1827156?s=512&v=4.
-->

---
layout: default
id: S02
---

<div class="presenter-slide theory-slide">
  <div class="presenter-copy">
    <p class="presenter-role">Splunk</p>
    <h1>Robert Pająk</h1>
    <a href="https://github.com/pellared/" class="presenter-handle">@pellared <span>github.com/pellared</span></a>
    <p class="presenter-focus"><span>Open source</span>OpenTelemetry maintainer</p>
  </div>

  <figure class="portrait-slot theory-portrait" aria-label="Portrait photo for Robert Pająk">
    <div class="portrait-placeholder">
      <span>Presenter photo</span>
      <small>insert photo</small>
    </div>
    <figcaption class="photo-quiz">
      <span class="photo-quiz-label">Audience quiz</span>
      <strong>Where was this picture taken?</strong>
      <span v-click class="photo-quiz-answer">Answer: Prague</span>
    </figcaption>
  </figure>
</div>

<!--
Spoken outline:
Theoretician: I’m Robert Pająk, pellared on GitHub. I work at Splunk and maintain OpenTelemetry Go and the OpenTelemetry Specification.

Theoretician: Where was this picture taken? Take a guess.

Theoretician: Prague. I’ll cover the model, terminology, and constraints behind safe context propagation.

Delivery notes:
- Time: 01:15, including a 01:00 quiz.
- Handoff: None.
- Quiz: Ask the audience where the picture was taken and take guesses for one minute. Then press next to reveal the answer, Prague.
- Portrait: Replace the marked area with Robert’s supplied portrait. Keep a portrait crop and do not add a visible location caption.
- Sources: https://github.com/pellared/; OpenTelemetry community roles: https://opentelemetry.io/community/members/.
-->

---
layout: default
id: S03
---

<div class="presenter-slide practitioner-slide">
  <div class="presenter-copy">
    <p class="presenter-role">Pipekit</p>
    <h1>Alan Clucas</h1>
    <a href="https://github.com/Joibel" class="presenter-handle">@Joibel <span>github.com/Joibel</span></a>
    <p class="presenter-focus"><span>Open source</span>Argo Workflows lead</p>
  </div>

  <figure class="portrait-slot practitioner-portrait" aria-label="Portrait photo for Alan Clucas">
    <div class="portrait-placeholder">
      <span>Presenter photo</span>
      <small>insert photo</small>
    </div>
    <figcaption class="photo-quiz">
      <span class="photo-quiz-label">Audience quiz</span>
      <strong>Where was this picture taken?</strong>
      <span v-click class="photo-quiz-answer">Answer: Prague</span>
    </figcaption>
  </figure>
</div>

<!--
Spoken outline:
Practitioner: I’m Alan Clucas, Joibel on GitHub. I work at Pipekit and lead Argo Workflows.

Practitioner: Where was this picture taken? Take a guess.

Practitioner: Prague. I’ll show what it means for real workflows, operators, and failures.

Delivery notes:
- Time: 01:15, including a 01:00 quiz.
- Handoff: None.
- Quiz: Ask the audience where the picture was taken and take guesses for one minute. Then press next to reveal the answer, Prague.
- Portrait: Replace the marked area with Alan’s supplied portrait. Keep a portrait crop and do not add a visible location caption.
- Sources: https://github.com/Joibel; Argo Project maintainer list: https://github.com/argoproj/argoproj/blob/main/MAINTAINERS.md.
-->

---
layout: default
id: S04
---

<div class="demo-section-slide">
  <p class="section-label">Concepts through trace evidence</p>
  <h1>Learning through demos</h1>
  <p class="section-statement">We’ll introduce each concept inside a working trace, then show what changes at the boundary.</p>
  <div class="section-sequence" aria-label="Teaching sequence">
    <span>System</span>
    <i></i>
    <span>Trace</span>
    <i></i>
    <span>Boundary</span>
    <i></i>
    <span>Result</span>
  </div>
</div>

<!--
Spoken outline:
Practitioner: Rather than front-load a glossary, we will meet each concept inside a working system. First, an HTTP service launches a CLI. We will read the normal HTTP trace, find the break, change the carrier, and compare the result.

Delivery notes:
- Time: 00:25.
- Handoff: None.
- Demo mode: Both demos use diagrams and captured evidence. No live interaction is planned.
- Sources: README.md.
-->

---
layout: default
id: S05
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Demo 1</p>
    <h1>HTTP service starts a CLI</h1>
  </header>

  <div class="architecture-diagram" role="img" aria-label="Java client calls a Go API over HTTP. The Go API starts a Python CLI as a child process. All three export spans to Jaeger.">
    <div class="runtime-boundary">
      <span>report-api container</span>
    </div>
    <div class="architecture-node node-java">
      <span>Java process</span>
      <strong>report-client</strong>
      <code>request report</code>
    </div>
    <div class="architecture-connector connector-http">
      <span>HTTP boundary</span>
      <code>POST /report</code>
      <i></i>
    </div>
    <div class="architecture-node node-go">
      <span>Go process</span>
      <strong>report-api</strong>
      <code>run report.py</code>
    </div>
    <div class="architecture-connector connector-process">
      <span>Process boundary</span>
      <code>exec python3</code>
      <i></i>
    </div>
    <div class="architecture-node node-python">
      <span>Python process</span>
      <strong>report-cli</strong>
      <code>build report</code>
    </div>
    <svg class="telemetry-bus" viewBox="0 0 1128 430" aria-hidden="true">
      <path class="telemetry-trunk" d="M140 296 H989" />
      <path class="service-client-line" d="M140 215 V296" />
      <path class="service-api-line" d="M551 215 V296" />
      <path class="service-cli-line" d="M989 215 V296" />
      <path d="M564 296 V330" class="bus-drop" />
    </svg>
    <span class="telemetry-label">OTLP spans</span>
    <div class="architecture-sink">
      <span>Trace backend</span>
      <strong>Jaeger</strong>
      <small>one place to inspect every span</small>
    </div>
  </div>
</div>

<!--
Spoken outline:
Practitioner: Our Java client asks a Go API to build a report. The API starts a Python CLI in its own process. All three programs export spans to Jaeger. The network boundary is Java to Go. The process boundary is Go to Python. Those boundaries need different places to carry the same trace identity.

Delivery notes:
- Time: 00:40.
- Handoff: Practitioner to Theoretician during the final 00:05.
- Before the demo evidence: Let the audience locate both labeled boundaries before advancing.
- Visual key: Teal identifies `report-client`, indigo identifies `report-api`, and orange identifies `report-cli`.
- Sources: demos/1-http-to-cli/README.md; demos/1-http-to-cli/compose.yaml; demos/1-http-to-cli/cmd/report-api/main.go.
-->

---
layout: default
id: S06
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>OpenTelemetry model</p>
    <h1>Trace, spans, and parentage</h1>
  </header>

  <div class="trace-model">
    <div class="trace-visual">
      <div class="trace-identity">
        <span>One trace</span>
        <code>d2264e9b…4308af96</code>
      </div>
      <div class="span-tree" aria-label="Parent-child span tree">
        <div class="span-axis" aria-hidden="true">
          <span>Trace time</span>
          <div><b>0</b><b>1.17 s</b></div>
        </div>
        <div class="span-row service-client depth-0"><b>request report</b><span>report-client</span><i style="--start: 0%; --duration: 100%"></i></div>
        <div class="span-row service-client depth-1"><em>└─</em><b>HTTP POST</b><span>report-client</span><i style="--start: 1%; --duration: 97%"></i></div>
        <div class="span-row service-api depth-2"><em>└─</em><b>POST /report</b><span>report-api</span><i style="--start: 14%; --duration: 84%"></i></div>
        <div class="span-row service-api depth-3"><em>└─</em><b>run report.py</b><span>report-api</span><i style="--start: 15%; --duration: 83%"></i></div>
        <div class="span-row service-cli depth-4"><em>└─</em><b>build report</b><span>report-cli</span><i style="--start: 51%; --duration: 43%"></i></div>
        <div class="span-row service-cli depth-5"><em>├─</em><b>fetch data</b><span>report-cli</span><i style="--start: 52%; --duration: 30%"></i></div>
        <div class="span-row service-cli depth-5"><em>└─</em><b>generate pdf</b><span>report-cli</span><i style="--start: 79%; --duration: 13%"></i></div>
      </div>
    </div>
    <dl class="trace-terms">
      <div><dt>Trace</dt><dd>The complete causal story</dd></div>
      <div><dt>Span</dt><dd>One timed operation</dd></div>
      <div><dt>Parent</dt><dd>The work that caused the next span</dd></div>
    </dl>
  </div>
</div>

<!--
Spoken outline:
Theoretician: A span records one timed operation, such as the POST or fetching data. A trace is the complete causal story, built from spans that name their parent. One trace ID groups the story. Each span has its own span ID. If the parent-child chain survives both boundaries, Jaeger can show the request, service, and CLI work as one tree.

Delivery notes:
- Time: 00:40.
- Handoff: None.
- Visual key: Service color matches S05. Every bar uses one trace timeline; horizontal position shows start time and length shows duration.
- Fallback: This native span tree also serves as the evidence fallback if a later screenshot does not render.
- Sources: OpenTelemetry Tracing API, https://opentelemetry.io/docs/specs/otel/trace/api/.
-->

---
layout: default
id: S07
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Network boundary</p>
    <h1>Context propagation over HTTP</h1>
  </header>

  <div class="propagation-diagram" role="img" aria-label="A W3C Trace Context propagator injects active trace context into an HTTP traceparent header and extracts it as a remote parent for a child span.">
    <div class="prop-context context-source">
      <span>Context</span>
      <strong>Active span</strong>
      <code>trace=T&nbsp;&nbsp;span=A</code>
      <small>identity held while work runs</small>
    </div>
    <div class="propagator-step step-inject">
      <span>Propagator</span>
      <strong>W3C Trace Context</strong>
      <code>inject</code>
      <i></i>
    </div>
    <div class="prop-carrier">
      <span>Carrier</span>
      <strong>HTTP headers</strong>
      <code>traceparent: 00-4bf92…-00f067…-01</code>
      <small>key-value fields cross the boundary</small>
    </div>
    <div class="propagator-step step-extract">
      <span>Propagator</span>
      <strong>W3C Trace Context</strong>
      <code>extract</code>
      <i></i>
    </div>
    <div class="prop-context context-target">
      <span>Context</span>
      <strong>Remote parent</strong>
      <code>trace=T&nbsp;&nbsp;parent=A</code>
      <small>instrumentation starts child B</small>
    </div>
  </div>
</div>

<!--
Spoken outline:
Theoretician: To cross HTTP, the currently active span contributes trace context: the trace ID, parent span ID, and flags that the next program needs. A propagator translates that in-memory context into a standard format and back again. Here the W3C Trace Context propagator injects a `traceparent` field. The HTTP headers are the carrier, the key-value container that transports it. Go extracts that field, then its instrumentation starts a child span.

Practitioner: This is the normal OpenTelemetry HTTP pattern. Java injects and Go’s HTTP instrumentation extracts, so the request reaches Go in one connected trace.

Delivery notes:
- Time: 00:55.
- Handoff: Theoretician to Practitioner for the final 00:10.
- Sources: OpenTelemetry context propagation, https://opentelemetry.io/docs/concepts/context-propagation/; OpenTelemetry Propagators API, https://opentelemetry.io/docs/specs/otel/context/api-propagators/; W3C Trace Context, https://www.w3.org/TR/trace-context/.
-->

---
layout: default
id: S08
---

<div class="demo-slide evidence-slide">
  <header class="demo-heading">
    <p>Missing handoff</p>
    <h1>HTTP headers stop at the process boundary</h1>
  </header>

  <div class="broken-trace-pair" role="group" aria-label="Two Jaeger traces from the same broken run have different trace IDs">
    <figure class="trace-screenshot broken-trace request-trace">
      <div class="trace-card-heading">
        <span>Request trace</span>
        <code>7dc2cf4…</code>
      </div>
      <img src="/demo-1/trace-broken.png" alt="Jaeger request trace with four spans ending at run report.py">
    <figcaption>
      <strong>Stops at <code>run report.py</code></strong>
      <small>4 spans · Java → Go</small>
    </figcaption>
  </figure>
  <div class="trace-split-marker" aria-hidden="true">
    <span>≠</span>
    <strong>different<br>trace IDs</strong>
  </div>
  <figure class="trace-screenshot broken-trace python-root-trace">
      <div class="trace-card-heading">
        <span>Python root trace</span>
        <code>662ebaa…</code>
      </div>
      <img src="/demo-1/trace-python-root.png" alt="Separate Jaeger trace rooted at the Python build report span with fetch data and generate pdf children">
      <figcaption>
        <strong>Starts at <code>build report</code></strong>
        <small>3 spans · Python only</small>
      </figcaption>
    </figure>
  </div>
</div>

<!--
Spoken outline:
Practitioner: The Go API can see the active trace when it starts `run report.py`. But starting Python is not an HTTP request, so no request headers cross that boundary. With no other carrier, Python extracts no parent and starts `build report` as a separate root trace. The screenshots show both halves: the request trace ends at `run report.py`; the Python trace starts at `build report`. Different trace IDs prove the parent-child link was lost.

Delivery notes:
- Time: 00:50.
- Handoff: None.
- Evidence: Playwright captures of local Jaeger traces `7dc2cf4ed9e5e29887bee1aafa31c53c` and `662ebaa1ca31466b2827be86bdda8748`, generated by the same `make run` execution.
- Diagnostic point: The CLI still creates spans. Missing parent context places them in a different trace.
- Fallback: Use the S05 architecture and remove the Go-to-Python parent link verbally, then point back to the expected S06 tree.
- Sources: demos/1-http-to-cli/README.md; demos/1-http-to-cli/cmd/report-api/main.go; demos/1-http-to-cli/python/report.py.
-->

---
layout: default
id: S09
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Process boundary</p>
    <h1>The child environment carries the handoff</h1>
  </header>

  <div class="environment-diagram" role="img" aria-label="The Go launcher injects W3C Trace Context into TRACEPARENT in a copied child environment. Python extracts it at startup and its instrumentation creates a child span.">
    <div class="environment-process env-parent">
      <span>Parent process: Go</span>
      <strong>active context</strong>
      <code>trace=T&nbsp;&nbsp;span=R</code>
      <small>copy the child environment</small>
    </div>
    <div class="environment-step env-inject">
      <strong>inject</strong>
      <span>W3C Trace Context</span>
      <i></i>
    </div>
    <div class="environment-carrier">
      <span>Child environment carrier</span>
      <code>TRACEPARENT=<br>00-4bf92…-00f067…-01</code>
      <small><b>traceparent</b> normalizes to <b>TRACEPARENT</b></small>
    </div>
    <div class="environment-step env-extract">
      <strong>extract</strong>
      <span>W3C Trace Context</span>
      <i></i>
    </div>
    <div class="environment-process env-child">
      <span>Child process: Python</span>
      <strong>extracted parent</strong>
      <code>trace=T&nbsp;&nbsp;parent=R</code>
      <small>instrumentation creates <b>build report</b></small>
    </div>
    <p class="environment-principle">The propagator and format stay the same. The carrier matches the boundary.</p>
  </div>
</div>

<!--
Spoken outline:
Theoretician: We keep the W3C Trace Context propagator and change the carrier. Just before the spawn, Go injects the active context into a copy of the child environment. The environment carrier normalizes `traceparent` to `TRACEPARENT`. At startup, Python extracts from its environment.

Practitioner: Python instrumentation then creates `build report` using that extracted parent. The launcher owns injection. Child instrumentation owns extraction and span creation. The carrier transports opaque propagation fields, so the same carrier model can support other configured formats.

Delivery notes:
- Time: 00:55.
- Handoff: Theoretician to Practitioner for the final 00:15.
- Implementation: Go injects into `cmd.Env`; Python extracts from `os.environ` once at startup.
- Specification status checked 2026-09-22: Environment Variables as Context Propagation Carriers is Release Candidate.
- Sources: OpenTelemetry Environment Variables as Context Propagation Carriers, https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md; OpenTelemetry Propagators API, https://opentelemetry.io/docs/specs/otel/context/api-propagators/; demos/1-http-to-cli/cmd/report-api/main.go; demos/1-http-to-cli/python/report.py.
-->

---
layout: default
id: S10
---

<div class="demo-slide evidence-slide connected-slide">
  <header class="demo-heading">
    <p>Connected result</p>
    <h1>The CLI joins the original trace</h1>
  </header>

  <figure class="trace-screenshot connected-trace">
    <img src="/demo-1/trace-connected.png" alt="Jaeger trace with seven spans across report-client, report-api, and report-cli">
    <figcaption>
      <strong>1 trace</strong>
      <span>7 spans across 3 services</span>
      <small>Environment carried parentage. Instrumentation created the spans.</small>
    </figcaption>
  </figure>
</div>

<!--
Spoken outline:
Practitioner: In the captured fixed run, Jaeger shows seven spans from three services under one trace. We can now see the CLI’s `build report`, `fetch data`, and `generate pdf` work under `run report.py`. That visibility comes from preserving parentage across the spawn, not from the environment variable creating spans.

Theoretician: Same trace context, same propagator, and a carrier suited to the boundary.

Delivery notes:
- Time: 00:30.
- Handoff: Practitioner to Theoretician for the final 00:05.
- Expected result: Seven spans from `report-client`, `report-api`, and `report-cli` share trace `d2264e9b82e5cfbab0bf724d4308af96`.
- Evidence: Playwright capture of the local Jaeger trace generated by `make fixed`.
- Fallback: Use the complete native span tree on S06 and the process handoff on S09.
- Sources: demos/1-http-to-cli/README.md; demos/1-http-to-cli/cmd/report-api/main.go; demos/1-http-to-cli/python/report.py.
-->

---
layout: default
id: S11
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>The dream</p>
    <h1>Tracing a workflow</h1>
  </header>
  <div class="dream-model">
    <div class="dream-dag">
      <svg class="dag-figure" viewBox="0 0 300 340" role="img" aria-label="Diamond DAG: A, then B and C in parallel, then D">
        <defs>
          <marker id="dag-arrow" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
            <path class="dag-arrowhead" d="M 0 0 L 10 5 L 0 10 z"></path>
          </marker>
        </defs>
        <line class="dag-edge" x1="130" y1="68"  x2="72"  y2="132"></line>
        <line class="dag-edge" x1="170" y1="68"  x2="228" y2="132"></line>
        <line class="dag-edge" x1="72"  y1="208" x2="130" y2="272"></line>
        <line class="dag-edge" x1="228" y1="208" x2="170" y2="272"></line>
        <circle class="dag-node" cx="150" cy="40"  r="32"></circle>
        <circle class="dag-node" cx="50"  cy="170" r="32"></circle>
        <circle class="dag-node" cx="250" cy="170" r="32"></circle>
        <circle class="dag-node" cx="150" cy="300" r="32"></circle>
        <text class="dag-label" x="150" y="40">A</text>
        <text class="dag-label" x="50"  y="170">B</text>
        <text class="dag-label" x="250" y="170">C</text>
        <text class="dag-label" x="150" y="300">D</text>
      </svg>
    </div>
    <div class="dream-trace">
      <div class="span-tree" aria-label="Workflow span with four child step spans">
        <div class="span-axis" aria-hidden="true">
          <span>Trace time</span>
          <div><b>submit</b><b>done</b></div>
        </div>
        <div class="span-row service-workflow depth-0"><b>workflow</b><span>orchestrator</span><i style="--start: 0%; --duration: 100%"></i></div>
        <div class="span-row service-node depth-1"><em>├─</em><b>A</b><span>step</span><i style="--start: 3%; --duration: 20%"></i></div>
        <div class="span-row service-node depth-1"><em>├─</em><b>B</b><span>step</span><i style="--start: 25%; --duration: 38%"></i></div>
        <div class="span-row service-node depth-1"><em>├─</em><b>C</b><span>step</span><i style="--start: 25%; --duration: 19%"></i></div>
        <div class="span-row service-node depth-1"><em>└─</em><b>D</b><span>step</span><i style="--start: 65%; --duration: 32%"></i></div>
      </div>
    </div>
  </div>
</div>

<!--
Spoken outline:
Practitioner: Here is the dream. A workflow is a graph of steps: A runs first, B and C run in parallel once A finishes, and D waits for both. If the workflow were a trace, it would look like this. One span for the workflow itself, from submission to completion. One span per step, each a child of the workflow, sitting exactly where it ran. You can read the graph straight off the bars: B and C overlap because they ran together, and D starts when the longer of them finishes. That is the promise. The orchestrator’s view and the timing view become one picture.

Delivery notes:
- Time: 00:40.
- Handoff: None.
- Visual key: The orchestrator’s span uses the API color; step spans use the client color. Same trace timeline conventions as S06: position is start time, length is duration.
- Sources: Argo Workflows DAG templates, https://argo-workflows.readthedocs.io/en/latest/walk-through/dag/.
-->

---
layout: default
id: S12
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>The dream</p>
    <h1>Inside the pods</h1>
  </header>
  <div class="dream-model">
    <div class="dream-dag">
      <svg class="dag-figure" viewBox="0 0 300 340" role="img" aria-label="Diamond DAG: A, then B and C in parallel, then D, each step a pod">
        <defs>
          <marker id="dag-arrow-2" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
            <path class="dag-arrowhead" d="M 0 0 L 10 5 L 0 10 z"></path>
          </marker>
        </defs>
        <line class="dag-edge" style="marker-end: url(#dag-arrow-2)" x1="130" y1="68"  x2="72"  y2="132"></line>
        <line class="dag-edge" style="marker-end: url(#dag-arrow-2)" x1="170" y1="68"  x2="228" y2="132"></line>
        <line class="dag-edge" style="marker-end: url(#dag-arrow-2)" x1="72"  y1="208" x2="130" y2="272"></line>
        <line class="dag-edge" style="marker-end: url(#dag-arrow-2)" x1="228" y1="208" x2="170" y2="272"></line>
        <circle class="dag-node" style="stroke: var(--pod-a)" cx="150" cy="40"  r="32"></circle>
        <circle class="dag-node" style="stroke: var(--pod-b)" cx="50"  cy="170" r="32"></circle>
        <circle class="dag-node" style="stroke: var(--pod-c)" cx="250" cy="170" r="32"></circle>
        <circle class="dag-node" style="stroke: var(--pod-d)" cx="150" cy="300" r="32"></circle>
        <text class="dag-label" x="150" y="40">A</text>
        <text class="dag-label" x="50"  y="170">B</text>
        <text class="dag-label" x="250" y="170">C</text>
        <text class="dag-label" x="150" y="300">D</text>
      </svg>
    </div>
    <div class="dream-trace dream-pods">
      <div class="span-tree" aria-label="Workflow span, four controller step spans, and a pod span nested under each">
        <div class="span-axis" aria-hidden="true">
          <span>Trace time</span>
          <div><b>submit</b><b>done</b></div>
        </div>
        <div class="span-row service-workflow depth-0"><b>workflow</b><span>orchestrator</span><i style="--start: 0%; --duration: 100%"></i></div>
        <div class="span-row service-node depth-1"><em>├─</em><b>A</b><span>controller</span><i style="--start: 3%; --duration: 20%"></i></div>
        <div class="span-row service-pod-a depth-2"><em>└─</em><b>A</b><span>pod</span><i style="--start: 7%; --duration: 14%"></i></div>
        <div class="span-row service-node depth-1"><em>├─</em><b>B</b><span>controller</span><i style="--start: 25%; --duration: 38%"></i></div>
        <div class="span-row service-pod-b depth-2"><em>└─</em><b>B</b><span>pod</span><i style="--start: 29%; --duration: 31%"></i></div>
        <div class="span-row service-node depth-1"><em>├─</em><b>C</b><span>controller</span><i style="--start: 25%; --duration: 19%"></i></div>
        <div class="span-row service-pod-c depth-2"><em>└─</em><b>C</b><span>pod</span><i style="--start: 30%; --duration: 12%"></i></div>
        <div class="span-row service-node depth-1"><em>└─</em><b>D</b><span>controller</span><i style="--start: 65%; --duration: 32%"></i></div>
        <div class="span-row service-pod-d depth-2"><em>└─</em><b>D</b><span>pod</span><i style="--start: 70%; --duration: 25%"></i></div>
      </div>
    </div>
  </div>
</div>

<!--
Spoken outline:
Practitioner: Every step runs in a Kubernetes pod. The bars from the last slide are the controller’s view of each step: from the moment it created the pod to the moment it noticed the pod had finished. Inside each pod something actually ran, and that deserves a span of its own. It starts later, because the pod had to be scheduled and its image pulled. It ends earlier, because the controller only notices completion on its next reconcile. On a quiet cluster the two bars almost coincide. On a stressed cluster they drift apart markedly, and the gap is exactly the time the workflow spent waiting on Kubernetes rather than doing work. I want to see both.

Theoretician: Which means the trace context has to reach the inside of the pod.

Delivery notes:
- Time: 00:45.
- Handoff: Practitioner to Theoretician for the final 00:05, which sets up the carrier question.
- Visual key: The step bars keep the S11 color and are relabelled "controller". Each pod span has its own color, because in Jaeger each pod is its own service. The DAG nodes take the pod colors to tie the two halves together.
- Sources: Argo Workflows architecture, https://argo-workflows.readthedocs.io/en/latest/architecture/.
-->

---
layout: default
id: S13
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>The dream</p>
    <h1>What the workload did</h1>
  </header>
  <div class="dream-model">
    <div class="dream-dag">
      <svg class="dag-figure" viewBox="0 0 300 340" role="img" aria-label="Diamond DAG with step A highlighted and B, C, D dimmed">
        <defs>
          <marker id="dag-arrow-3" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto-start-reverse">
            <path class="dag-arrowhead" d="M 0 0 L 10 5 L 0 10 z"></path>
          </marker>
        </defs>
        <g class="dag-dim">
          <line class="dag-edge" style="marker-end: url(#dag-arrow-3)" x1="130" y1="68"  x2="72"  y2="132"></line>
          <line class="dag-edge" style="marker-end: url(#dag-arrow-3)" x1="170" y1="68"  x2="228" y2="132"></line>
          <line class="dag-edge" style="marker-end: url(#dag-arrow-3)" x1="72"  y1="208" x2="130" y2="272"></line>
          <line class="dag-edge" style="marker-end: url(#dag-arrow-3)" x1="228" y1="208" x2="170" y2="272"></line>
          <circle class="dag-node" style="stroke: var(--pod-b)" cx="50"  cy="170" r="32"></circle>
          <circle class="dag-node" style="stroke: var(--pod-c)" cx="250" cy="170" r="32"></circle>
          <circle class="dag-node" style="stroke: var(--pod-d)" cx="150" cy="300" r="32"></circle>
          <text class="dag-label" x="50"  y="170">B</text>
          <text class="dag-label" x="250" y="170">C</text>
          <text class="dag-label" x="150" y="300">D</text>
        </g>
        <circle class="dag-node" style="stroke: var(--pod-a)" cx="150" cy="40" r="32"></circle>
        <text class="dag-label" x="150" y="40">A</text>
      </svg>
    </div>
    <div class="dream-trace dream-pods">
      <div class="span-tree" aria-label="Step A: controller span, pod span, and three workload spans nested inside">
        <div class="span-axis" aria-hidden="true">
          <span>Trace time</span>
          <div><b>pod created</b><b>pod finished</b></div>
        </div>
        <div class="span-row service-node depth-0"><b>A</b><span>controller</span><i style="--start: 0%; --duration: 100%"></i></div>
        <div class="span-row service-pod-a depth-1"><em>└─</em><b>A</b><span>pod</span><i style="--start: 20%; --duration: 70%"></i></div>
        <div class="span-row service-workload depth-2"><em>├─</em><b>observability</b><span>workload</span><i style="--start: 22%; --duration: 16.5%"></i></div>
        <div class="span-row service-workload depth-2"><em>├─</em><b>summit</b><span>workload</span><i style="--start: 39%; --duration: 27.5%"></i></div>
        <div class="span-row service-workload depth-2"><em>└─</em><b>prague</b><span>workload</span><i style="--start: 67%; --duration: 22%"></i></div>
      </div>
    </div>
  </div>
</div>

<!--
Spoken outline:
Practitioner: Zoom into one step. The controller’s view, the pod’s view, and now, inside the pod, what the user’s workload actually did: three spans it emitted itself, named observability, summit, and prague. This is the layer that matters to the person who wrote the workflow, and the platform knows nothing about it. It only appears if the workload’s own instrumentation joins the same trace. Three layers, three owners: the controller, the executor in the pod, and the user’s code. One trace.

Theoretician: Three process boundaries, and not one of them is an HTTP request.

Delivery notes:
- Time: 00:40.
- Handoff: Practitioner to Theoretician for the final 00:05, which lands the carrier problem.
- Visual key: The controller and pod colors carry over from S12. Workload spans use the ink color to mark them as the user’s own code rather than a platform layer. In Jaeger they would share the pod’s service color; the distinction here is a teaching device.
- Sources: demos/2-argo-to-otel-cli/README.md.
-->

---
layout: default
id: S14
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Demo 2</p>
    <h1>One step, three spans</h1>
  </header>
  <div class="demo2-model">
    <figure class="trace-screenshot">
      <img src="/demo-2/argo-dag.png" alt="Argo Workflows UI showing the otel-cli workflow: a single succeeded step">
    </figure>
    <div>
      <pre class="code-snippet">echo "TRACEPARENT: <span class="tok-env">${TRACEPARENT}</span>"
/otel-cli exec --name <span class="tok-name">observability</span> -- sleep 3
/otel-cli exec --name <span class="tok-name">summit</span>        -- sleep 5
/otel-cli exec --name <span class="tok-name">prague</span>        -- sleep 4</pre>
    </div>
  </div>
</div>

<!--
Spoken outline:
Practitioner: This is demo 2 as Argo sees it: a workflow with a single step, so a single pod. And this is everything that pod runs. Three otel-cli commands, each emitting one span, named after this conference. And an echo of TRACEPARENT, purely so you can see the variable is there. Notice what is missing. Nothing in this workflow mentions tracing. Nobody named a propagator or configured an SDK. otel-cli is an off-the-shelf tool that reads its environment, and that is all it needs.

Delivery notes:
- Time: 00:40.
- Handoff: None.
- Evidence: Playwright capture of the local Argo Workflows UI for the demo 2 workflow.
- Fallback: Read the snippet aloud; the DAG is a single node and needs no picture to be understood.
- Sources: demos/2-argo-to-otel-cli/workflow.yaml; demos/2-argo-to-otel-cli/README.md.
-->

---
layout: default
id: S15
---

<div class="demo-slide evidence-slide connected-slide demo2-result">
  <header class="demo-heading">
    <p>Demo 2</p>
    <h1>The workload joins the trace</h1>
  </header>
  <figure class="trace-screenshot connected-trace">
    <img src="/demo-2/trace-jaeger.png" alt="Jaeger trace showing observability, summit and prague spans nested under runMainContainer inside the workflow trace">
    <figcaption>
      <strong>1 trace</strong>
      <span>49 spans across 2 services</span>
    </figcaption>
  </figure>
</div>

<!--
Spoken outline:
Practitioner: Here is the trace. The workflow span at the top belongs to the controller. Under it, the node, then creating the pod. Then argoexec, the executor inside the pod: runInitContainer, runWaitContainer, and runMainContainer. And under runMainContainer, the three spans the workload emitted: observability, summit, prague, three seconds, five, four. This is the third dream slide, for real. Notice where the workload spans hang. Not off the node, off runMainContainer. That tells you there were two injections, not one. The controller injected its context into the pod’s environment. Then the executor started its own span and injected again, into the environment of the process it launched. Two carrier hops, and the workflow author wrote neither of them.

Theoretician: The propagator never changed. Only the carrier did, and it was the same carrier both times.

Delivery notes:
- Time: 00:45.
- Handoff: Practitioner to Theoretician for the final 00:10.
- Expected result: 49 spans, one root `workflow` span from `workflow-controller`, and `observability`, `summit`, `prague` as children of `runMainContainer`.
- Evidence: Playwright capture of the local Jaeger UI, scrolled to the runMainContainer subtree.
- Fallback: The S13 dream slide is the same tree drawn by hand.
- Sources: demos/2-argo-to-otel-cli/README.md.
-->

---
layout: default
id: S16
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Demo 2</p>
    <h1>Two injections</h1>
  </header>
  <div>
    <div class="inject-model">
      <div class="inject-step">
        <strong>argoexec → workload</strong>
        <pre class="code-snippet">ctx, span := tracer.<span class="tok-fn">StartRunMainContainer</span>(ctx, …)
<span class="tok-kw">defer</span> span.End()
env := os.Environ()
carrier := &amp;envcar.Carrier{
    SetEnvFunc: <span class="tok-kw">func</span>(key, value string) {
        env = setEnvVar(env, key, value)
    },
}
propagation.TraceContext{}.<span class="tok-fn">Inject</span>(ctx, carrier)
cmd := exec.CommandContext(ctx, name, args...)
cmd.Env = env <span class="tok-muted">// the child’s env, not argoexec’s</span></pre>
      </div>
      <div class="inject-step">
        <strong>controller → pod</strong>
        <pre class="code-snippet">carrier := telemetry.Carrier{
    SetEnvFunc: <span class="tok-kw">func</span>(key, value string) {
        envVars = append(envVars,
            apiv1.EnvVar{Name: key, Value: value})
    },
}
propagation.TraceContext{}.<span class="tok-fn">Inject</span>(ctx, carrier)
<span class="tok-muted">// TRACEPARENT=00-&lt;trace&gt;-&lt;span&gt;-01</span></pre>
      </div>
    </div>
  </div>
</div>

<!--
Spoken outline:
Practitioner: Two handoffs, and here they are, working outward from the workload. The nearer one is inside the pod. argoexec starts the runMainContainer span, then injects again with the OpenTelemetry environment carrier, the same envcar package demo 1’s Go launcher used, into a copy of the environment it hands only to the user’s command. Its own environment stays exactly as the pod spec set it. That is why the workload’s spans hang off runMainContainer. Now one level out: where did argoexec’s environment come from? From the workflow-controller, when it built the pod spec. It ran the same W3C propagator against a carrier whose Set method appends a Kubernetes environment variable, so every container in the pod is born with TRACEPARENT. That is the entire mechanism. Same propagator in both places. The carrier is the environment both times.

Theoretician: Note what Argo did not do. It did not invent a format or parse a value. It reused W3C Trace Context and changed only where the fields travel. Both sides now use the same SetEnvFunc shape, and argoexec uses the very carrier package demo 1 did.

Delivery notes:
- Time: 00:50.
- Handoff: Practitioner to Theoretician for the final 00:15.
- Visual key: The left snippet is condensed from argo-workflows PR #17016 (open at the time of writing), which replaces the v4.1.3 os.Setenv approach with the contrib environment carrier and a child-only environment; in the PR it spans injectTraceParent and startCommand. The right snippet is abridged from v4.1.3. Function names in the accent color are the propagator and tracer calls; keywords use the API color.
- Order: presented nearest-first. The left column (argoexec) is chronologically the later of the two injections; the right column (the controller building the pod spec) happened first. Say so if asked.
- Sources: argo-workflows v4.1.3 workflow/controller/workflowpod.go; https://github.com/argoproj/argo-workflows/pull/17016 for cmd/argoexec/commands/emissary.go; go.opentelemetry.io/contrib/propagators/envcar.
-->

---
layout: default
id: S17
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Demo 3</p>
    <h1>Let’s fake CI</h1>
  </header>
  <figure class="trace-screenshot dag-shot">
    <img src="/demo-3/argo-dag.png" alt="Argo Workflows UI: the buildkit workflow, a clone step followed by a build step">
  </figure>
</div>

<!--
Spoken outline:
Practitioner: Let’s fake CI. Two steps: clone a repository, then build a container image from it. The same shape as almost every pipeline you have ever run.

Delivery notes:
- Time: 00:20.
- Handoff: None.
- Evidence: Playwright capture of the local Argo Workflows UI for the demo 3 workflow.
- Sources: demos/3-argo-to-buildkit/workflow.yaml.
-->

---
layout: default
id: S18
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Demo 3</p>
    <h1>git says nothing</h1>
  </header>
  <div>
    <div class="trace-crop crop-clone">
      <img src="/demo-3/trace-clone.png" alt="Jaeger: the clone step's node span lasts 10.8 seconds; its runMainContainer span lasts 2.0 seconds and has no child spans">
    </div>
    <pre class="code-snippet" style="margin-top: 26px">git clone --depth 1 https://github.com/Joibel/otel-deploy /src</pre>
  </div>
</div>

<!--
Spoken outline:
Practitioner: Demo 3 is a two-step build. The first step clones a repository, and git has never heard of OpenTelemetry. It gets TRACEPARENT in its environment, exactly like otel-cli did, and throws it away. So under runMainContainer there is nothing. But look at what we still know. The executor wrapped git in a span, so git ran for two seconds. And the controller’s node span says the step took almost eleven. That is S12’s gap, for real: most of this step was Kubernetes, not git. An uninstrumented process is not a hole in the trace. You lose its insides. You keep its edges.

Delivery notes:
- Time: 00:45.
- Handoff: None.
- Point at: the clone `node` bar (10.8 s) and the clone `runMainContainer` bar (2.0 s), which has no child-count badge because nothing is nested inside it.
- Evidence: Playwright capture of the local Jaeger trace with rows below `createWorkflowPod` collapsed and the name column widened, cropped on the slide to the clone node's subtree.
- Sources: demos/3-argo-to-buildkit/workflow.yaml; demos/3-argo-to-buildkit/README.md.
-->

---
layout: default
id: S19
---

<div class="demo-slide evidence-slide connected-slide">
  <header class="demo-heading">
    <p>Demo 3</p>
    <h1>BuildKit tells us everything</h1>
  </header>
  <figure class="trace-screenshot connected-trace">
    <div class="trace-crop crop-buildkit">
      <img src="/demo-3/trace-buildkit.png" alt="Jaeger zoomed to the build: three FROM steps start together, the gobuild and webbuild steps run side by side, and the stage-2 COPY --from steps land last">
    </div>
    <figcaption>
      <strong>Same carrier</strong>
      <span>~250 spans inside one step</span>
    </figcaption>
  </figure>
</div>

<!--
Spoken outline:
Practitioner: The second step runs BuildKit, which is instrumented. It reads the same TRACEPARENT from the same place, and this is what comes back: about two hundred and fifty spans, all under the step’s runMainContainer. You can read the Dockerfile off it. Three base images resolve and pull at the same moment, because nothing makes them wait for each other. The Go and Node builders run side by side. The three-second Go build is the long bar. And the final stage waits for both, then copies their output in.

Theoretician: Same carrier as the git step. The difference is entirely on the reading side: BuildKit extracts, git doesn’t.

Delivery notes:
- Time: 00:50.
- Handoff: Practitioner to Theoretician for the final 00:10.
- Point at: the three `FROM` rows starting together, the 3.1 s `go build` bar, and the `stage-2` `COPY --from` rows at the end.
- Evidence: Playwright capture of the same Jaeger trace, collapsed to the path down to BuildKit’s `Solve` span and zoomed to 14.0–27.7 s with the minimap range selection; `cache request` rows are BuildKit’s own and left in.
- Sources: demos/3-argo-to-buildkit/README.md.
-->

---
layout: default
id: S20
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Demo 4</p>
    <h1>Lineage, hands off</h1>
  </header>
  <div class="lineage-intro">
    <div class="lineage-roles">
      <div><span>Platform team</span>wants data lineage</div>
      <div><span>Data scientists</span>own the code</div>
    </div>
    <figure class="trace-screenshot dag-wide">
      <img src="/demo-4/argo-dag.png" alt="Argo Workflows UI: setup, then customer-ltv and daily-revenue in parallel, then exec-summary, report and lineage">
    </figure>
  </div>
</div>

<!--
Spoken outline:
Practitioner: Demo 4 is a real one. A platform team wanted data lineage: which tables fed which. But the pipelines belonged to their data scientists, ordinary Python against Postgres, and the platform team could not rewrite that code. This is a stand-in for their pipeline: set up the tables, build two derived tables in parallel, join them into a summary, report on it. And one extra step at the end we’ll come back to.

Delivery notes:
- Time: 00:30.
- Handoff: None.
- Evidence: Playwright capture of the local Argo Workflows UI for the demo 4 workflow, horizontal layout, artifact nodes hidden.
- Sources: demos/4-argo-to-python-sql/README.md; demos/4-argo-to-python-sql/workflow.yaml.
-->

---
layout: default
id: S21
---

<div class="demo-slide evidence-slide connected-slide">
  <header class="demo-heading">
    <p>Demo 4</p>
    <h1>Every query is a span</h1>
  </header>
  <figure class="trace-screenshot connected-trace">
    <div class="trace-crop crop-sql">
      <img src="/demo-4/trace-sql.png" alt="Jaeger: customer_ltv and exec_summary INSERT spans under their runMainContainer spans, with the exec_summary span expanded to show its db.statement and otel.scope.name opentelemetry.instrumentation.psycopg">
    </div>
    <figcaption>
      <strong>No tracing code</strong>
      <span>psycopg, auto-instrumented</span>
    </figcaption>
  </figure>
</div>

<!--
Spoken outline:
Practitioner: The data scientists’ code has no OpenTelemetry in it at all. The operator auto-instruments the Python and its Postgres driver, so every statement becomes a span, and the SQL rides along as db.statement. Each INSERT sits under its stage’s runMainContainer, in the workflow’s trace, because a small platform-owned wrapper reads TRACEPARENT from the environment. And look at the SQL: exec_summary reads from daily_revenue and customer_ltv. That is the lineage, sitting in the trace.

Delivery notes:
- Time: 00:45.
- Handoff: None.
- Point at: the highlighted INSERT rows, then `db.statement` on `exec_summary` naming both upstream tables, then `otel.scope.name: opentelemetry.instrumentation.psycopg`.
- Evidence: Playwright capture of the local Jaeger trace with Jaeger’s own service filter pruning the pod-level executor services and the setup, report and lineage stages (the “spans pruned” rows are Jaeger’s), rows collapsed to the stage path, INSERT highlighted with Find, and the exec_summary INSERT expanded.
- The wrapper: Python auto-instrumentation does not extract TRACEPARENT from the environment, so demos/4-argo-to-python-sql/datasci/tracectx.py does it with EnvironmentGetter. Mention only if asked, or keep for the next slide.
- Sources: demos/4-argo-to-python-sql/README.md.
-->

---
layout: default
id: S22
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Demo 4</p>
    <h1>The lineage, from the trace</h1>
  </header>
  <div class="lineage-result">
    <figure class="trace-screenshot artifact-shot">
      <img src="/demo-4/argo-artifacts.png" alt="Argo Workflows UI artifact panel rendering the lineage-report artifact, lineage.html, with its trace id and lineage graph">
    </figure>
    <figure class="trace-screenshot lineage-graph">
      <img src="/demo-4/lineage.svg" alt="Lineage graph: orders and order_items feed daily_revenue; customers, orders and order_items feed customer_ltv; both feed exec_summary, which report reads">
    </figure>
  </div>
</div>

<!--
Spoken outline:
Practitioner: The last step reads this workflow’s own trace back out of Jaeger, parses the SQL on every span, and draws the lineage. It is published as an ordinary workflow artifact, so it sits right there in the Argo UI. Orders and order items feed daily revenue. Customers, orders and order items feed customer lifetime value. Both feed the summary, and the report reads it. The data scientists changed nothing. And notice what is missing: the warehouse has a products table, and it is not here, because nothing read it. This is what the pipeline actually did, not what the schema says it might.

Theoretician: And all of it hangs together only because one environment variable carried the trace into each pod.

Delivery notes:
- Time: 00:45.
- Handoff: Practitioner to Theoretician for the final 00:05.
- Point at: the lineage-report artifact panel in Argo (the trace id under the heading), then the enlarged graph; call out that `products` is absent.
- Evidence: Left, Playwright capture of the local Argo Workflows UI artifact panel rendering lineage.html. Right, the SVG extracted verbatim from the workflow’s lineage-report artifact.
- If asked about DDL: the setup step’s DROP and CREATE statements are in the trace but produce no edges, because DDL moves no data between tables.
- Sources: demos/4-argo-to-python-sql/README.md; demos/4-argo-to-python-sql/datasci/lineage.py.
-->

---
layout: default
id: S23
---

<div class="demo-slide">
  <header class="demo-heading">
    <p>Demo 4</p>
    <h1>Python reads the carrier too</h1>
  </header>
  <div class="py-carrier">
    <pre class="code-snippet"><span class="tok-kw">from</span> opentelemetry.propagators._envcarrier <span class="tok-kw">import</span> EnvironmentGetter
ctx = <span class="tok-fn">extract</span>(os.environ, getter=EnvironmentGetter())
context.<span class="tok-fn">attach</span>(ctx) <span class="tok-muted"># <span class="tok-env">TRACEPARENT</span> becomes the parent</span>
runpy.<span class="tok-fn">run_path</span>(script, run_name=<span class="tok-name">"__main__"</span>)</pre>
    <pre class="code-snippet py-command">command: [python, -m, <span class="tok-name">tracectx</span>]  <span class="tok-muted"># wraps the untouched pipeline</span></pre>
  </div>
</div>

<!--
Spoken outline:
Theoretician: Here is the catch. The operator's Python auto-instrumentation installs the SDK and instruments psycopg, but it never looks at the environment for a parent. Without help, every query would start a new trace. The fix is a few lines the platform owns: extract the context from the environment with the SDK's own environment getter, attach it, then run the data scientist's script unchanged. Same carrier as the Go side, just read in Python.

Delivery notes:
- Time: 00:40.
- Handoff: none; Theoretician continues from the S22 handoff.
- Point at: `os.environ` in the extract call, then the `command` line, to show the pipeline itself is untouched.
- Condensed: the real file adds a fallback if `_envcarrier` moves, and argv handling; this shows only the propagation.
- If asked why a private module: `_envcarrier` ships in opentelemetry-api and its docstring names child-process initialisation as its use; nothing in auto-instrumentation calls it yet.
- If asked about uppercase keys: EnvironmentGetter maps `traceparent` to `TRACEPARENT`, matching Argo's Go carrier.
- Sources: demos/4-argo-to-python-sql/datasci/tracectx.py; demos/4-argo-to-python-sql/datasci-lineage-trace.yaml.
-->

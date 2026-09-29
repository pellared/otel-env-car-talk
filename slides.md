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
Presenter cues:
Robert (Theoretician):
- Introduce the talk as a trace-context journey across process boundaries.
- Name both presenters and the two perspectives.

Alan (Practitioner):
- Preview containers, build steps, and batch jobs as the concrete cases.

Delivery notes:
- Expected start time: 00:00.
- Duration: 00:20.
- Handoff: Robert to Alan for the final 00:05.
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
Presenter cues:
Robert (Theoretician):
- Introduce yourself, your Splunk role, and OpenTelemetry Go and Specification work.
- Ask the audience to guess where the portrait was taken; reveal Prague.
- Preview your focus on the model and its constraints.

Delivery notes:
- Expected start time: 00:20.
- Duration: 01:15, including a 01:00 quiz.
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
Presenter cues:
Alan (Practitioner):
- Introduce yourself, Pipekit, and your Argo Workflows role.
- Ask the audience to guess where the portrait was taken; reveal Prague.
- Preview your focus on workflows, operators, and failures.

Delivery notes:
- Expected start time: 01:35.
- Duration: 01:15, including a 01:00 quiz.
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
Presenter cues:
Alan (Practitioner):
- Explain that each concept will appear beside working trace evidence.
- Preview the HTTP-to-CLI boundary, the break, the carrier change, and the connected result.

Delivery notes:
- Expected start time: 02:50.
- Duration: 00:25.
- Demo mode: All examples use diagrams and captured evidence. No live interaction is planned.
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
Presenter cues:
Robert (Theoretician):
- Orient the audience to Java client, Go API, Python CLI, and Jaeger.
- Distinguish the Java-to-Go HTTP hop from the Go-to-Python process launch.
- Point out that the second boundary has no HTTP headers to carry context.

Delivery notes:
- Expected start time: 03:15.
- Duration: 00:40.
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
Presenter cues:
Robert (Theoretician):
- Define a span as one timed operation and a trace as the connected causal story.
- Explain trace ID, span ID, and parent-child links using the displayed tree.
- Show where the CLI work should appear if parentage survives.

Delivery notes:
- Expected start time: 03:55.
- Duration: 00:40.
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
Presenter cues:
Robert (Theoretician):
- Define trace context as the identity and parent information passed to the next operation.
- Define propagation as moving that context; a propagator encodes and decodes the fields.
- Identify W3C `traceparent` as the format field and HTTP headers as the carrier.
- Explain that Go extracts the field before its instrumentation starts a child span.

Delivery notes:
- Expected start time: 04:35.
- Duration: 00:55.
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
Presenter cues:
Robert (Theoretician):
- Compare the captured request trace with the Python CLI root trace.
- Point to `run report.py` ending one trace and `build report` starting another.
- Explain that the process launch did not carry HTTP headers, so Python had no parent context.

Delivery notes:
- Expected start time: 05:30.
- Duration: 00:50.
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
Presenter cues:
Robert (Theoretician):
- Keep the W3C propagator and change the carrier to a copied child environment.
- Explain why the Go launcher injects just before spawn and Python extracts at startup.
- Show `traceparent` normalizing to `TRACEPARENT`; consistent key rules make both sides agree.
- Separate context transport from instrumentation creating the child span.

Delivery notes:
- Expected start time: 06:20.
- Duration: 00:55.
- Implementation: Go injects into `cmd.Env`; Python extracts from `os.environ` once at startup.
- Specification status checked 2026-09-29: Environment Variables as Context Propagation Carriers is Release Candidate.
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
Presenter cues:
Robert (Theoretician):
- Point to the seven captured spans from three services in one trace.
- Show the CLI spans under `run report.py` and name restored parentage as the cause.
- Reinforce that the variable carried context; instrumentation created the spans.

Delivery notes:
- Expected start time: 07:15.
- Duration: 00:30.
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
Presenter cues:
Alan (Practitioner):
- Read the workflow graph: A, parallel B and C, then D.
- Map the graph to a workflow span with child step spans.
- Explain how overlap and waiting become visible as timing bars.

Delivery notes:
- Expected start time: 07:45.
- Duration: 00:40.
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
Presenter cues:
Alan (Practitioner):
- Distinguish each controller step span from the shorter work inside its pod.
- Point to scheduling, image pull, and reconcile time in the gap.
- Explain why the parent context must reach the pod to compare both layers.

Delivery notes:
- Expected start time: 08:25.
- Duration: 00:45.
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
Presenter cues:
Alan (Practitioner):
- Add spans emitted by the user workload inside one pod span.
- Name the three owners: controller, pod executor, and user code.
- Emphasize that the workload must be instrumented to contribute its own spans.

Delivery notes:
- Expected start time: 09:10.
- Duration: 00:40.
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
Presenter cues:
Alan (Practitioner):
- Show the single-step Argo workflow before opening its trace.
- Point to three `otel-cli` commands and the visible `TRACEPARENT`.
- Explain that the workflow author did not configure a propagator in this step.

Delivery notes:
- Expected start time: 09:50.
- Duration: 00:40.
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
Presenter cues:
Alan (Practitioner):
- Read the captured tree from controller workflow span through pod executor to workload spans.
- Explain the controller-to-pod injection and executor-to-command injection.
- Use the parentage under `runMainContainer` as evidence of the second hop.

Delivery notes:
- Expected start time: 10:30.
- Duration: 00:45.
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
Presenter cues:
Alan (Practitioner):
- Start with `argoexec` injecting into a copied environment for the launched command.
- Move outward to the controller injecting into the pod specification.
- Explain that both use the configured W3C propagator with environment carriers; Argo need not invent a trace format.

Delivery notes:
- Expected start time: 11:15.
- Duration: 00:50.
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
    <img src="/demo-3/argo-dag.png" alt="Argo Workflows UI: the captured buildkit workflow, a clone step followed by a build step">
  </figure>
  <p class="build-pipeline-caption">Captured: clone → build <span>Expected in one trace: scan → push</span></p>
</div>

<!--
Presenter cues:
Alan (Practitioner):
- Introduce the captured two-step CI example: clone, then Docker image build.
- In a full workflow, scan and push would be child stages of the same trace; the displayed capture covers clone and build.

Delivery notes:
- Expected start time: 12:05.
- Duration: 00:20.
- Evidence: Playwright capture of the local Argo Workflows UI for the demo 3 workflow.
- Sources: demos/3-argo-to-buildkit/workflow.yaml.
- Scope: The screenshot is the captured clone/build run. Scan and push are the expected extension, not captured results.
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
Presenter cues:
Alan (Practitioner):
- Show that git ignores inherited `TRACEPARENT`, so it emits no nested spans.
- Compare the two-second command span with the roughly eleven-second controller step span.
- Use the gap to distinguish Kubernetes waiting from process runtime.

Delivery notes:
- Expected start time: 12:25.
- Duration: 00:45.
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
Presenter cues:
Alan (Practitioner):
- Show BuildKit extracting the same inherited context and emitting its internal spans.
- Point to parallel base-image pulls and builders, then the slower Go build.
- Explain how the trace identifies the slow operation inside the build step.
- Keep scan and push as an expected extension, not part of this captured trace.

Delivery notes:
- Expected start time: 13:10.
- Duration: 00:50.
- Point at: the three `FROM` rows starting together, the 3.1 s `go build` bar, and the `stage-2` `COPY --from` rows at the end.
- Evidence: Playwright capture of the same Jaeger trace, collapsed to the path down to BuildKit’s `Solve` span and zoomed to 14.0–27.7 s with the minimap range selection; `cache request` rows are BuildKit’s own and left in.
- Sources: demos/3-argo-to-buildkit/README.md.
- Scope: The capture contains BuildKit spans only; do not describe scan or push as observed.
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
Presenter cues:
Alan (Practitioner):
- Introduce the platform team’s lineage question and explain that data scientists own the Python code.
- Read the batch DAG as setup, parallel derived tables, summary, report, and lineage artifact.
- Explain that the captured workflow stands in for extract, transform, and load stages.

Delivery notes:
- Expected start time: 14:00.
- Duration: 00:30.
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
Presenter cues:
Alan (Practitioner):
- Show the SQL spans created by Python and psycopg auto-instrumentation.
- Point to `db.statement` naming input and output tables; those fields provide the dataset evidence.
- Explain that the platform wrapper extracts the parent first, so the SQL spans join the workflow trace.

Delivery notes:
- Expected start time: 14:30.
- Duration: 00:45.
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
Presenter cues:
Alan (Practitioner):
- Show the final step reading its workflow trace and deriving edges from SQL span metadata.
- Walk through upstream tables, derived tables, summary, and report.
- Note the unread `products` table is absent; the graph reflects observed work.
- Clarify that this trace-derived view does not replace every dedicated lineage capability.

Delivery notes:
- Expected start time: 15:15.
- Duration: 00:45.
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
Presenter cues:
Alan (Practitioner):
- Explain that this Python auto-instrumentation does not extract an environment parent by itself.
- Point to the platform-owned wrapper extracting and attaching context before running unchanged scientist code.
- Describe the failure mode: without extraction, jobs start separate traces and the lineage view fragments.

Delivery notes:
- Expected start time: 16:00.
- Duration: 00:40.
- Point at: `os.environ` in the extract call, then the `command` line, to show the pipeline itself is untouched.
- Condensed: the real file adds a fallback if `_envcarrier` moves, and argv handling; this shows only the propagation.
- If asked why a private module: `_envcarrier` ships in opentelemetry-api and its docstring names child-process initialisation as its use; nothing in auto-instrumentation calls it yet.
- If asked about uppercase keys: EnvironmentGetter maps `traceparent` to `TRACEPARENT`, matching Argo's Go carrier.
- Sources: demos/4-argo-to-python-sql/datasci/tracectx.py; demos/4-argo-to-python-sql/datasci-lineage-trace.yaml.
-->

---
layout: default
id: S24
---

<div class="closing-slide">
  <header class="demo-heading">
    <p>Adoption</p>
    <h1>Tools already use TRACEPARENT</h1>
  </header>

  <div class="adoption-list" role="list" aria-label="Tools using TRACEPARENT in child environments">
    <div role="listitem"><strong>Argo Workflows</strong><span>injects context into pods and commands</span></div>
    <div role="listitem"><strong>Docker BuildKit</strong><span>reads and forwards process context</span></div>
    <div role="listitem"><strong>Claude Code</strong><span>links headless sessions and subprocesses</span></div>
    <div role="listitem"><strong>otel-cli</strong><span>wraps a command in a span</span></div>
    <div role="listitem"><strong>Thoth</strong><span>instruments shells and GitHub Actions</span></div>
    <div role="listitem"><strong>Jenkins OpenTelemetry plugin</strong><span>exposes context to build steps</span></div>
  </div>

  <p class="adoption-payoff">Shared field names let instrumented tools join without another custom adapter.</p>
</div>

<!--
Presenter cues:
Robert (Theoretician):
- Group existing examples by workflow engine, build tool, CLI wrapper, and CI integration.
- Explain that shared field names connect independently instrumented tools and reduce custom adapters.
- Tie practitioner adoption to useful implementation guidance and future refinement.

Delivery notes:
- Expected start time: 16:40.
- Duration: 00:25.
- Sources: OpenTelemetry feedback article and linked implementations, https://opentelemetry.io/blog/2026/environment-variable-context-propagation/; otel-cli, https://github.com/equinix-labs/otel-cli; Thoth, https://github.com/liatrio-labs/thoth; Argo Workflows injection, https://github.com/argoproj/argo-workflows/blob/main/workflow/controller/workflowpod.go and https://github.com/argoproj/argo-workflows/blob/main/cmd/argoexec/commands/emissary.go; Docker BuildKit extraction and injection, https://github.com/moby/buildkit/blob/master/util/tracing/childprocess/traceenv.go and https://github.com/moby/buildkit/blob/master/util/tracing/childprocess/traceexec.go; Claude Code tracing, https://code.claude.com/docs/en/monitoring-usage#traces-beta; Jenkins OpenTelemetry plugin, https://github.com/jenkinsci/opentelemetry-plugin.
-->

---
layout: default
id: S25
---

<div class="closing-slide">
  <header class="demo-heading">
    <p>Beyond TRACEPARENT</p>
    <h1>One carrier, several formats</h1>
  </header>

  <div class="format-ledger" role="list" aria-label="Propagation fields carried through environment variables">
    <div class="format-row" role="listitem">
      <span>W3C Trace Context</span>
      <code>TRACEPARENT=00-4bf92…-00f067…-01</code>
      <small>trace and parent identity</small>
    </div>
    <div class="format-row format-baggage" role="listitem">
      <span>W3C Baggage</span>
      <code>BAGGAGE=build.id=42,repository.name=example</code>
      <small>application context for downstream work</small>
    </div>
    <div class="format-row format-b3" role="listitem">
      <span>B3</span>
      <code>X_B3_TRACEID=463ac35c9f6413ad</code>
      <small>normalized from <b>x-b3-traceid</b></small>
    </div>
  </div>

  <p class="format-principle"><strong>Carrier:</strong> opaque strings <span></span> <strong>Propagator:</strong> field names, format, and validation</p>
</div>

<!--
Presenter cues:
Robert (Theoretician):
- Separate opaque environment carrier strings from the fields, format, and validation chosen by the propagator.
- Use `TRACEPARENT`, `BAGGAGE`, and normalized B3 field names as examples.
- Treat inherited fields as untrusted; they may leak into logs or diagnostics.
- Allow-list baggage, scrub fields when continuity should stop, and never propagate secrets.

Delivery notes:
- Expected start time: 17:05.
- Duration: 00:30.
- Point at: `BAGGAGE`, then `X_B3_TRACEID`, then the carrier-versus-propagator line.
- Safety reminder: Deliver the trust-boundary warning after explaining the visual; it intentionally has no separate slide.
- Sources: OpenTelemetry Environment Variables as Context Propagation Carriers, https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md; OpenTelemetry feedback article, https://opentelemetry.io/blog/2026/environment-variable-context-propagation/; W3C Baggage, https://www.w3.org/TR/baggage/; B3 propagation, https://github.com/openzipkin/b3-propagation.
-->

---
layout: default
id: S26
---

<div class="closing-slide resource-slide">
  <header class="demo-heading">
    <p>Release Candidate</p>
    <h1>Feedback before stabilization</h1>
  </header>

  <div class="feedback-layout">
    <div class="feedback-date">
      <span>Earliest planned stabilization</span>
      <strong>2 Nov 2026</strong>
      <p>After at least 14 days without a new related issue, and only if no blocker remains.</p>
      <small>Normalization, portability, concurrency, security</small>
    </div>
    <a class="qr-link" href="https://opentelemetry.io/blog/2026/environment-variable-context-propagation/" aria-label="Open the OpenTelemetry environment variable context propagation feedback article">
      <QrCode value="https://opentelemetry.io/blog/2026/environment-variable-context-propagation/" label="QR code for the OpenTelemetry environment variable context propagation feedback article" />
      <span>Read the proposal<br>Report what breaks</span>
      <small class="qr-url">opentelemetry.io/blog/2026/<br>environment-variable-context-propagation</small>
    </a>
  </div>
</div>

<!--
Presenter cues:
Robert (Theoretician):
- State that the environment carrier specification is a Release Candidate as verified on 29 September 2026.
- Explain that 2 November 2026 is the earliest stabilization date, subject to a 14-day quiet period and no blocker.
- Invite concrete feedback on normalization, portability, concurrency, and security.

Delivery notes:
- Expected start time: 17:35.
- Duration: 00:25.
- Status verified: 2026-09-29. The specification is Release Candidate. November 2, 2026 is the earliest stabilization date, not a guaranteed release date; a new related issue or a significant update restarts the 14-day feedback period.
- QR target: https://opentelemetry.io/blog/2026/environment-variable-context-propagation/
- Sources: OpenTelemetry feedback article, https://opentelemetry.io/blog/2026/environment-variable-context-propagation/; environment carrier specification, https://github.com/open-telemetry/opentelemetry-specification/blob/main/specification/context/env-carriers.md; stabilization issue #5040, https://github.com/open-telemetry/opentelemetry-specification/issues/5040.
-->

---
layout: default
id: S27
---

<div class="closing-slide resource-slide">
  <header class="demo-heading">
    <p>Keep exploring</p>
    <h1>Slides and demo material</h1>
  </header>

  <div class="repo-layout">
    <div class="repo-copy">
      <strong>Presentation</strong>
      <strong>Demo source</strong>
      <a href="https://github.com/pellared/otel-env-car-talk/">github.com/pellared/otel-env-car-talk</a>
    </div>
    <a class="qr-link" href="https://github.com/pellared/otel-env-car-talk/" aria-label="Open the presentation and demos repository">
      <QrCode value="https://github.com/pellared/otel-env-car-talk/" label="QR code for the presentation and demos repository" />
      <span>Open the repository</span>
    </a>
  </div>
</div>

<!--
Presenter cues:
Robert (Theoretician):
- Point to the repository for the slides and demo source.
- Invite attendees to reproduce the examples or adapt the carrier pattern to their tools.

Delivery notes:
- Expected start time: 18:00.
- Duration: 00:15.
- QR target: https://github.com/pellared/otel-env-car-talk/
- Sources: https://github.com/pellared/otel-env-car-talk/.
-->

---
layout: default
id: S28
---

<div class="thanks-slide">
  <div class="thanks-heading">
    <p>Thank you</p>
    <h1>Questions?</h1>
  </div>

  <div class="contact-list" aria-label="Presenter and community contact links">
    <a href="https://github.com/pellared/"><strong>Robert Pająk</strong><span>github.com/pellared</span></a>
    <a href="https://github.com/Joibel"><strong>Alan Clucas</strong><span>github.com/Joibel</span></a>
    <a href="https://cloud-native.slack.com/archives/C0598R66XAP"><strong>CNCF Slack</strong><span>#otel-cicd</span></a>
    <a href="https://github.com/open-telemetry/community/blob/main/sigs.md#semantic-conventions-cicd"><strong>OpenTelemetry CI/CD SIG</strong><span>Open community meeting</span></a>
  </div>
</div>

<!--
Presenter cues:
Alan (Practitioner):
- Thank the audience and open the five-minute questions and troubleshooting window.
- Point to presenter GitHub links, CNCF Slack `#otel-cicd`, and the OpenTelemetry CI/CD SIG.

Delivery notes:
- Expected start time: 18:15.
- Duration: 00:10 for the presentation transition. The reserved 05:00 Q&A and troubleshooting window begins here and remains outside the 20-minute talk.
- Sources: presenter profiles, https://github.com/pellared/ and https://github.com/Joibel; CNCF Slack signup, https://slack.cncf.io/; `#otel-cicd`, https://cloud-native.slack.com/archives/C0598R66XAP; OpenTelemetry CI/CD SIG directory entry, https://github.com/open-telemetry/community/blob/main/sigs.md#semantic-conventions-cicd.
-->

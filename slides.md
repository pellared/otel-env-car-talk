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

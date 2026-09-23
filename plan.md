# Presentation plan

## Current delivery

The authored material now includes the opening, a screenshot-led HTTP-to-CLI teaching demo, the three "dream" slides that turn from the CLI example towards workflows, Demo 2, which realises the dream with a single Argo step running otel-cli, Demo 3, which contrasts an uninstrumented git step with an instrumented BuildKit step, Demo 4, a real-world data-lineage case, and a closing sequence about existing adoption, formats, baggage, a brief security reminder, specification feedback, resources, and community contact. It uses `18:25` of the 20-minute presentation allowance. The closing sequence uses `01:45`, leaving a `01:35` transition and recovery buffer. No live-demo setup is required. The final 5 minutes remain reserved for questions and troubleshooting.

## Audience outcome

By S10, attendees can distinguish a trace, span, parent relationship, trace context, propagator, and carrier. They can explain why HTTP propagation stops at a spawned process and how an environment carrier lets child instrumentation continue the same trace. By S29, they can also explain why the carrier is format-agnostic, decide what may safely cross a process trust boundary, and identify where to test the release candidate or ask for help.

## Narrative arc

1. S01 names the subject and establishes the two presenters.
2. S02 introduces Robert Pająk and his OpenTelemetry roles, then asks the audience where his portrait was taken.
3. S03 introduces Alan Clucas and his Argo Workflows role, then asks the audience where his portrait was taken.
4. S04 establishes demos as the teaching structure for the technical material.
5. S05 introduces a Java client, Go API, Python CLI, and Jaeger as one concrete system with an HTTP boundary and a process boundary.
6. S06 defines traces, spans, and parent-child relationships through the expected span tree.
7. S07 defines trace context, propagation, the W3C Trace Context propagator, and HTTP headers as a carrier.
8. S08 compares the captured request trace with the Python CLI's separate root trace and explains why the parent-child link is missing.
9. S09 keeps the propagator constant, changes the carrier to the child environment, and assigns injection and extraction responsibilities.
10. S10 shows the captured connected trace and separates context transport from span creation.
11. S11 states the dream for the second example: a workflow DAG rendered as one trace, with one span for the workflow and one child span per step, so the graph can be read off the bars.
12. S12 reveals that each step bar was only the controller's view, nests a shorter pod span under each, and names the gap between them as waiting on Kubernetes, which is why context must reach inside the pod.
13. S13 zooms into one step to add the third layer: spans the user's own workload emitted inside the pod, so the audience sees three owners in one trace and that none of the boundaries between them is HTTP.
14. S14 shows Demo 2 as Argo sees it, one step and one pod, beside the container's verbatim commands, and points out that nothing in them configures tracing.
15. S15 shows the captured Jaeger trace for Demo 2, with the workload's spans under runMainContainer, and reads the two environment-carrier injections off where those spans hang.
16. S16 shows the two injection sites in Argo's source, nearest the workload first: argoexec starting the user's process, then the controller building the pod spec, and makes the point that the propagator never changed, only the carrier.
17. S17 introduces Demo 3 as a faked CI pipeline, clone then build, through its Argo DAG.
18. S18 shows an uninstrumented git step: nothing nested under its runMainContainer, yet its duration and the controller's much longer node span are both visible, making S12's gap concrete.
19. S19 shows the instrumented BuildKit step reading the same carrier and returning the Dockerfile's structure as spans, with parallel base-image pulls and builders.
20. S20 introduces Demo 4 as a real case: a platform team wanting data lineage over pipelines its data scientists own, shown through the pipeline's Argo DAG.
21. S21 shows the auto-instrumented SQL INSERT spans in the workflow trace, with db.statement already naming the upstream tables.
22. S22 shows the lineage graph the pipeline's final step derived from its own trace, published as an Argo artifact, and points out the unread products table as proof it reflects what ran.
23. S23 shows the few lines of platform-owned Python that read the same environment carrier, because auto-instrumentation does not extract it.
24. S26 shows that otel-cli, Thoth, Argo Workflows, Docker BuildKit, the Jenkins OpenTelemetry plugin, and Claude Code already use `TRACEPARENT` across child-process boundaries.
25. S24 separates the environment carrier from its payload and propagation format, using W3C trace context, W3C baggage, and B3 as concrete examples, then gives a concise trust-boundary warning.
26. S27 states the verified Release Candidate status, the earliest planned stabilization date and quiet-period condition, and points to the feedback article with a QR code.
27. S28 points to the presentation repository and demo material with a QR code.
28. S29 thanks the audience, opens the 5-minute Q&A, and gives GitHub and CNCF Slack contact paths, including `#otel-cicd` and the OpenTelemetry CI/CD SIG.

## Sections and timing

| Section | Slides | Duration |
| --- | --- | ---: |
| Opening: title, presenter roles, and photo quizzes | S01-S03 | 02:50 |
| Learning through demos and Demo 1: HTTP to CLI | S04-S10 | 04:55 |
| The dream: tracing a workflow | S11-S13 | 02:05 |
| Demo 2: Argo to otel-cli | S14-S16 | 02:15 |
| Demo 3: git and BuildKit | S17-S19 | 01:55 |
| Demo 4: data lineage | S20-S23 | 02:40 |
| Implications, feedback, resources, and thanks | S26, S24, S27-S29 | 01:45 |
| Transition and recovery buffer | Between sections as needed | 01:35 |
| Questions and troubleshooting | After the presentation | 05:00 outside the 20:00 presentation |

## Slide purposes, presenter ownership, and handoffs

| ID | Purpose | Lead and handoff | Duration |
| --- | --- | --- | ---: |
| S01 | Name the talk and both authors. | Theoretician leads; `00:05` handoff to Practitioner within the slide. | 00:20 |
| S02 | Introduce Robert Pająk, Splunk, and his OpenTelemetry roles, then run a photo-location quiz. | Theoretician leads; no handoff. | 01:15, including 01:00 quiz |
| S03 | Introduce Alan Clucas, Pipekit, and his Argo Workflows role, then run a photo-location quiz. | Practitioner leads; no handoff. | 01:15, including 01:00 quiz |
| S04 | Establish that concepts will arrive through concrete demos and trace evidence. | Practitioner leads; no handoff. | 00:25 |
| S05 | Orient the audience to the programs, boundaries, process launch, and trace backend in Demo 1. | Practitioner leads; `00:05` transition to Theoretician after the architecture is clear. | 00:40 |
| S06 | Define a trace, span, and parent relationship through one expected span tree. | Theoretician leads; no handoff. | 00:40 |
| S07 | Explain context propagation, the propagator, and HTTP headers as the carrier. | Theoretician leads; `00:10` handoff to Practitioner for the normal instrumentation consequence. | 00:55 |
| S08 | Diagnose the process-boundary break by comparing the captured request trace with the Python CLI's separate root trace. | Practitioner leads; no handoff. | 00:50 |
| S09 | Explain environment injection and extraction, key normalization, format independence, and ownership at process startup. | Theoretician leads; `00:15` handoff to Practitioner for the implementation consequence. | 00:55 |
| S10 | Confirm one connected trace and reinforce that instrumentation creates spans. | Practitioner leads; `00:05` handoff to Theoretician for the closing contrast. | 00:30 |
| S11 | Set up the workflow example by drawing a diamond DAG beside the trace it should produce: one workflow span and four step spans of unequal length. | Practitioner leads; no handoff. | 00:40 |
| S12 | Nest a pod span under each step span to separate the controller's view from the pod's view, and motivate carrying context into the pod. | Practitioner leads; `00:05` handoff to Theoretician to pose the carrier question. | 00:45 |
| S13 | Zoom into step A and nest the workload's own spans under the pod span, establishing three owners in one trace and that no boundary between them is HTTP. | Practitioner leads; `00:05` handoff to Theoretician to land the carrier problem. | 00:40 |
| S14 | Present Demo 2's single-step workflow in the Argo UI beside its verbatim container commands, and establish that the workflow configures no tracing. | Practitioner leads; no handoff. | 00:40 |
| S15 | Confirm the captured Demo 2 trace and read the two carrier injections off the span parentage. | Practitioner leads; `00:10` handoff to Theoretician for the closing contrast. | 00:45 |
| S16 | Show both injection sites in Argo's source, working outward from the workload: argoexec for the process environment, then the controller for the pod spec. | Practitioner leads; `00:15` handoff to Theoretician for the format-independence point. | 00:50 |
| S17 | Introduce Demo 3 as a faked CI pipeline through its two-step Argo DAG. | Practitioner leads; no handoff. | 00:20 |
| S18 | Show the uninstrumented git step: no child spans, but its duration and the controller's longer node span are visible. | Practitioner leads; no handoff. | 00:45 |
| S19 | Show the instrumented BuildKit step reading the same carrier and exposing the Dockerfile's parallel structure. | Practitioner leads; `00:10` handoff to Theoretician to locate the difference on the reading side. | 00:50 |
| S20 | Introduce Demo 4 as a real case, platform team versus code owned by data scientists, through the pipeline's Argo DAG. | Practitioner leads; no handoff. | 00:30 |
| S21 | Show the auto-instrumented SQL INSERT spans in the workflow trace and point at db.statement naming the upstream tables. | Practitioner leads; no handoff. | 00:45 |
| S22 | Show the derived lineage graph as an Argo artifact and note the unread products table. | Practitioner leads; `00:05` handoff to Theoretician to tie it back to the carrier. | 00:45 |
| S23 | Show the small platform-owned wrapper that extracts TRACEPARENT from the environment in Python, since auto-instrumentation does not. | Theoretician leads; `00:05` handoff to Practitioner at the transition to S26. | 00:40 |
| S26 | Establish existing adoption through command wrappers, workflow and build tools, CI integrations, and coding agents that already inject or extract `TRACEPARENT`. | Practitioner leads; `00:05` handoff to Theoretician at the transition to S24. | 00:25 |
| S24 | Show that one environment carrier can transport fields from W3C Trace Context, W3C Baggage, B3, or another configured propagation format, then state the trust-boundary rule for inherited fields. | Theoretician leads and continues into S27; no handoff. | 00:30 |
| S27 | Ask for blocking feedback on the Release Candidate and show the verified stabilization conditions and article QR code. | Theoretician leads; no handoff. | 00:25 |
| S28 | Give the audience the repository for the presentation and demo material. | Practitioner leads; no handoff. | 00:15 |
| S29 | Thank the audience, transition to Q&A, and give online contact routes. | Practitioner leads; `00:05` handoff to Theoretician for the community contact. | 00:10 presentation transition; 05:00 Q&A follows outside the talk |

## Demo 1 evidence and recovery plan

- Audience view before the evidence: S05 shows the complete architecture and names both boundaries before any trace screenshot appears.
- HTTP handoff: Java instrumentation injects W3C Trace Context into HTTP headers; Go HTTP instrumentation extracts it.
- Process handoff: the Go launcher injects into a copied child environment; Python instrumentation extracts at startup.
- Expected result: S10 shows one trace with seven spans across `report-client`, `report-api`, and `report-cli`.
- Failure insight: S08 pairs the API trace ending at `run report.py` with the Python trace starting at `build report`; their different trace IDs show that the child received no parent context.
- Planned path: no live demo. S08 uses two Playwright screenshots from the same broken run, and S10 uses a Playwright screenshot of the fixed run.
- Display fallback: the native architecture and span-tree diagrams on S05-S07 and S09 preserve the explanation if a screenshot asset does not render.
- Allotted time: S04-S10 use `04:55`, including the section transition and screenshot comparison. S05-S10 use `04:30` for Demo 1 itself.

## Argo and Docker build evidence and recovery plan

- Audience view before the evidence: S17 shows the two-stage Argo DAG before either trace crop appears.
- Injection and extraction: the Argo controller and executor inject the active context into pod and child-process environments; BuildKit extracts `TRACEPARENT`, while git does not.
- Expected result: S18 retains the wrapper spans around the uninstrumented clone, and S19 places BuildKit's Dockerfile spans under the build step in the workflow trace.
- Failure insight: S18 shows that an inherited variable alone creates no child spans. Missing extraction hides the process internals, while the surrounding workflow spans still expose scheduling and runtime boundaries.
- Planned path: captured Argo and Jaeger evidence only; no live interaction.
- Display fallback: S11-S13 retain the native workflow, pod, and workload span model if a capture does not render.
- Allotted time: S11-S19 use `06:15`, including the model and the preceding Argo carrier implementation; the build-specific evidence on S17-S19 uses `01:55`.

## Required batch lineage evidence and recovery plan

- Audience view before the evidence: S20 shows the complete Argo DAG and identifies the platform and data-scientist ownership boundary.
- Injection and extraction: Argo injects context into each pod; the platform-owned Python wrapper extracts it before auto-instrumentation creates SQL spans.
- Expected result: S21 connects the SQL spans into the workflow trace, and S22 derives dataset edges from the recorded SQL statements.
- Failure insight: S23 explains that auto-instrumentation alone does not extract the parent from the environment; without the wrapper, each job starts a separate trace and the trace-derived lineage graph fragments.
- Planned path: captured Argo and Jaeger evidence plus the captured lineage artifact; no live interaction.
- Display fallback: S20's DAG and S23's extraction snippet preserve the ownership and propagation explanation if an evidence image does not render.
- Allotted time: S20-S23 use `02:40`, including setup and transition.

## Photo placement

S01 uses the presenters’ current GitHub profile photos. S02 and S03 reserve portrait-oriented areas for photos taken in Prague. Each portrait supports a one-minute audience quiz asking where the photo was taken. The location remains hidden until the presenter reveals the answer. The placeholders should be replaced only after the presenters provide the corresponding photos.

## Source-brief coverage matrix

This coverage matrix maps the current authored deck to the source brief and records the remaining required-demo gap explicitly.

| Source brief item | Slide mapping | Status |
| --- | --- | --- |
| HTTP or message propagation does not automatically cross a process launch. | S05, S07-S08 | Covered for an HTTP-to-process example |
| Newcomer model of traces, spans, parentage, trace context, propagation, carriers, and propagators. | S06-S07 | Covered |
| Environment variables carry context into a launched CLI or subprocess. | S09-S10, S14-S15 | Covered for a child CLI and for a workload inside a Kubernetes pod |
| Injected `TRACEPARENT` preserves trace continuity across the process boundary. | S09-S10, S15 | Covered, including two successive injections in one pod |
| The carrier remains independent of a single propagation format. | S09, S16, S24 | Covered explicitly with W3C Trace Context, W3C Baggage, and B3 |
| Environment-variable name normalization. | S09, S16 | Covered with `traceparent` to `TRACEPARENT`, in demo 1's launcher and in argoexec |
| Launcher injection and child instrumentation extraction responsibilities. | S09, S16 | Covered, including a launcher that is itself a launched child |
| Environment propagation does not create spans; instrumentation creates spans and assigns parentage. | S09-S10 | Covered |
| Security, trust boundaries, inherited environments, logging leakage, allow-listing, and scrubbing. | S24 | Covered as a concise spoken constraint; the dedicated security slide was intentionally removed |
| Interoperability for CI/CD, workflow engines, build tools, CLIs, and instrumentation. | S05, S09, S14-S19, S26 | Covered through the demos and existing tool adoption |
| Shared propagation reduces custom glue and incompatible trace conventions. | S16 | Covered by contrast: Argo reused the W3C propagator rather than inventing a format |
| Current specification status, adoption feedback, and future refinements. | S26-S27 | Covered; Release Candidate status and stabilization conditions verified on 2026-09-23 |
| Required Argo/Docker build example. | S11-S19 | Partially covered: clone and build have captured evidence; scan and push do not yet appear in the workflow or trace evidence |
| Required batch data-lineage example and its instrumentation caveat. | S20-S23 | Covered: case, SQL spans, derived lineage, and the Python extraction caveat with its wrapper |
| Complementary Theoretician and Practitioner roles. | S01-S24, S26-S29 | Covered throughout the model, implementation, safety, adoption, and closing handoffs |

# Presentation plan

## Current delivery

The authored material now includes the opening, a screenshot-led HTTP-to-CLI teaching demo, the three "dream" slides that turn from the CLI example towards workflows, Demo 2, which realises the dream with a single Argo step running otel-cli, and Demo 3, which contrasts an uninstrumented git step with an instrumented BuildKit step. It uses `14:00` of the 20-minute presentation allowance. Demo 1, including its section transition, uses `04:55`; the dream slides use `02:05`; Demo 2 uses `02:15`; Demo 3 uses `01:55`; no live-demo setup is required. The final 5 minutes remain reserved for questions and troubleshooting.

## Audience outcome

By S10, attendees can distinguish a trace, span, parent relationship, trace context, propagator, and carrier. They can explain why HTTP propagation stops at a spawned process and how an environment carrier lets child instrumentation continue the same trace.

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

## Sections and timing

| Section | Slides | Duration |
| --- | --- | ---: |
| Opening: title, presenter roles, and photo quizzes | S01-S03 | 02:50 |
| Learning through demos and Demo 1: HTTP to CLI | S04-S10 | 04:55 |
| The dream: tracing a workflow | S11-S13 | 02:05 |
| Demo 2: Argo to otel-cli | S14-S16 | 02:15 |
| Demo 3: git and BuildKit | S17-S19 | 01:55 |
| Remaining presentation material | Not yet authored | Up to 06:00 |
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

## Demo 1 evidence and recovery plan

- Audience view before the evidence: S05 shows the complete architecture and names both boundaries before any trace screenshot appears.
- HTTP handoff: Java instrumentation injects W3C Trace Context into HTTP headers; Go HTTP instrumentation extracts it.
- Process handoff: the Go launcher injects into a copied child environment; Python instrumentation extracts at startup.
- Expected result: S10 shows one trace with seven spans across `report-client`, `report-api`, and `report-cli`.
- Failure insight: S08 pairs the API trace ending at `run report.py` with the Python trace starting at `build report`; their different trace IDs show that the child received no parent context.
- Planned path: no live demo. S08 uses two Playwright screenshots from the same broken run, and S10 uses a Playwright screenshot of the fixed run.
- Display fallback: the native architecture and span-tree diagrams on S05-S07 and S09 preserve the explanation if a screenshot asset does not render.
- Allotted time: S04-S10 use `04:55`, including the section transition and screenshot comparison. S05-S10 use `04:30` for Demo 1 itself.

## Photo placement

S01 uses the presenters’ current GitHub profile photos. S02 and S03 reserve portrait-oriented areas for photos taken in Prague. Each portrait supports a one-minute audience quiz asking where the photo was taken. The location remains hidden until the presenter reveals the answer. The placeholders should be replaced only after the presenters provide the corresponding photos.

## Source-brief coverage matrix

This incremental delivery adds the HTTP-to-CLI teaching demo. It does not replace either of the two examples required by the README.

| Source brief item | Slide mapping | Status |
| --- | --- | --- |
| HTTP or message propagation does not automatically cross a process launch. | S05, S07-S08 | Covered for an HTTP-to-process example |
| Newcomer model of traces, spans, parentage, trace context, propagation, carriers, and propagators. | S06-S07 | Covered |
| Environment variables carry context into a launched CLI or subprocess. | S09-S10, S14-S15 | Covered for a child CLI and for a workload inside a Kubernetes pod |
| Injected `TRACEPARENT` preserves trace continuity across the process boundary. | S09-S10, S15 | Covered, including two successive injections in one pod |
| The carrier remains independent of a single propagation format. | S09, S16 | Covered briefly, twice |
| Environment-variable name normalization. | S09, S16 | Covered with `traceparent` to `TRACEPARENT`, in demo 1's launcher and in argoexec |
| Launcher injection and child instrumentation extraction responsibilities. | S09, S16 | Covered, including a launcher that is itself a launched child |
| Environment propagation does not create spans; instrumentation creates spans and assigns parentage. | S09-S10 | Covered |
| Security, trust boundaries, inherited environments, logging leakage, allow-listing, and scrubbing. | None | Deferred |
| Interoperability for CI/CD, workflow engines, build tools, CLIs, and instrumentation. | S05, S09, S14-S15 | CLI and workflow-engine cases covered; build tools and broader implications deferred |
| Shared propagation reduces custom glue and incompatible trace conventions. | S16 | Covered by contrast: Argo reused the W3C propagator rather than inventing a format |
| Current specification status, adoption feedback, and future refinements. | None | Deferred; status checked for slide notes but not yet part of the spoken narrative |
| Required Argo/Docker build example. | S11-S19 | Covered: the target picture, an Argo step running otel-cli, and a Docker build with BuildKit beside an uninstrumented git step |
| Required batch data-lineage example and its instrumentation caveat. | None | Deferred |
| Complementary Theoretician and Practitioner roles. | S01-S10 | Covered |

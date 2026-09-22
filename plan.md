# Presentation plan

## Current delivery

The authored material now includes the opening and a screenshot-led HTTP-to-CLI teaching demo. It uses `07:45` of the 20-minute presentation allowance. The demo section, including its section transition, uses `04:55`; no live-demo setup is required. The final 5 minutes remain reserved for questions and troubleshooting.

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

## Sections and timing

| Section | Slides | Duration |
| --- | --- | ---: |
| Opening: title, presenter roles, and photo quizzes | S01-S03 | 02:50 |
| Learning through demos and Demo 1: HTTP to CLI | S04-S10 | 04:55 |
| Remaining presentation material | Not yet authored | Up to 12:15 |
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
| Environment variables carry context into a launched CLI or subprocess. | S09-S10 | Covered for a child CLI |
| Injected `TRACEPARENT` preserves trace continuity across the process boundary. | S09-S10 | Covered |
| The carrier remains independent of a single propagation format. | S09 | Covered briefly |
| Environment-variable name normalization. | S09 | Covered with `traceparent` to `TRACEPARENT` |
| Launcher injection and child instrumentation extraction responsibilities. | S09 | Covered |
| Environment propagation does not create spans; instrumentation creates spans and assigns parentage. | S09-S10 | Covered |
| Security, trust boundaries, inherited environments, logging leakage, allow-listing, and scrubbing. | None | Deferred |
| Interoperability for CI/CD, workflow engines, build tools, CLIs, and instrumentation. | S05, S09 | CLI case covered; broader ecosystem implications deferred |
| Shared propagation reduces custom glue and incompatible trace conventions. | None | Deferred |
| Current specification status, adoption feedback, and future refinements. | None | Deferred; status checked for slide notes but not yet part of the spoken narrative |
| Required Argo/Docker build example. | None | Deferred; not replaced by Demo 1 |
| Required batch data-lineage example and its instrumentation caveat. | None | Deferred |
| Complementary Theoretician and Practitioner roles. | S01-S10 | Covered |

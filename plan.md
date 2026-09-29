# Presentation plan

## Audience outcome and narrative

In at most 20 minutes, attendees should be able to explain how trace context crosses an HTTP boundary, why a process launch breaks the usual header path, and how a launcher and child instrumentation can preserve parentage through environment variables. The audience should leave with a useful distinction between carrier, propagation format, and instrumentation, plus a safe way to apply the pattern in workflow, build, and batch tools.

The story moves from a broken HTTP-to-CLI trace (S05-S10), through a workflow's three layers of work (S11-S16), to captured build and data-lineage examples (S17-S23). Robert closes with interoperability, formats, trust boundaries, specification feedback, and the repository (S24-S27). Alan invites questions (S28). S01 is shared; all other slides have one owner.

## Sections and timing

| Section | Slides | Duration |
| --- | --- | ---: |
| Opening and presenter introductions | S01-S03 | 02:50 |
| Teaching setup and HTTP-to-CLI example | S04-S10 | 04:55 |
| Workflow trace model | S11-S13 | 02:05 |
| Argo-to-CLI evidence | S14-S16 | 02:15 |
| Clone and BuildKit evidence | S17-S19 | 01:55 |
| Batch lineage evidence | S20-S23 | 02:40 |
| Adoption, formats, feedback, resources, Q&A transition | S24-S28 | 01:45 |
| Transition and recovery buffer | Between sections | 01:35 |
| **Presentation total** | | **20:00** |
| Questions and troubleshooting | After S28 | 05:00 outside the presentation |

The timed slide content is 18:25. Captured evidence avoids live setup. The 01:35 buffer covers transitions and recovery. Presenter cues are prompts, so rehearsal should confirm the actual pace.

## Slide purposes and ownership

Handoffs between slides are included in each slide's duration; S01 has a 00:05 within-slide handoff.

| ID | Purpose | Presenter and handoff | Duration |
| --- | --- | --- | ---: |
| S01 | Name the topic and introduce both presenters. | Robert and Alan; Robert hands to Alan for 00:05. | 00:20 |
| S02 | Introduce Robert and run the portrait-location quiz. | Robert; handoff to Alan on S03. | 01:15 |
| S03 | Introduce Alan and run the portrait-location quiz. | Alan; continues on S04. | 01:15 |
| S04 | Establish the learn-through-evidence sequence. | Alan; handoff to Robert on S05. | 00:25 |
| S05 | Show the Java-to-Go HTTP hop and Go-to-Python process hop. | Robert; continues. | 00:40 |
| S06 | Define spans, traces, and parent relationships with one tree. | Robert; continues. | 00:40 |
| S07 | Define trace context, propagation, propagator, and HTTP carrier. | Robert; continues. | 00:55 |
| S08 | Diagnose the disconnected CLI trace in captured evidence. | Robert; continues. | 00:50 |
| S09 | Explain injection, extraction, child environment, key normalization, and ownership. | Robert; continues. | 00:55 |
| S10 | Confirm the connected trace and distinguish context transport from span creation. | Robert; handoff to Alan on S11. | 00:30 |
| S11 | Map a workflow graph to one trace with step spans. | Alan; continues. | 00:40 |
| S12 | Separate controller time from pod runtime and identify the scheduling gap. | Alan; continues. | 00:45 |
| S13 | Add workload spans inside the pod and identify the three owners. | Alan; continues. | 00:40 |
| S14 | Show the one-step Argo workflow before its trace. | Alan; continues. | 00:40 |
| S15 | Read two environment injections from the captured span tree. | Alan; continues. | 00:45 |
| S16 | Show the controller and executor injection sites in Argo. | Alan; continues. | 00:50 |
| S17 | Introduce the captured clone/build DAG and the full clone/build/scan/push pattern. | Alan; continues. | 00:20 |
| S18 | Show what remains visible when git ignores the carrier. | Alan; continues. | 00:45 |
| S19 | Show BuildKit child spans and the operational payoff of finding slow work. | Alan; continues. | 00:50 |
| S20 | Show the batch DAG and platform/data-scientist ownership boundary. | Alan; continues. | 00:30 |
| S21 | Show instrumented SQL spans and dataset information recorded in span metadata. | Alan; continues. | 00:45 |
| S22 | Show the derived lineage artifact and what the trace actually observed. | Alan; continues. | 00:45 |
| S23 | Show the platform-owned Python extraction wrapper and its failure mode. | Alan; handoff to Robert on S24. | 00:40 |
| S24 | Show tool adoption and the value of shared propagation conventions. | Robert; continues. | 00:25 |
| S25 | Separate carrier from format; cover baggage, inherited input, and trust boundaries. | Robert; continues. | 00:30 |
| S26 | State the verified Release Candidate status and invite concrete feedback. | Robert; continues. | 00:25 |
| S27 | Point to slides and demo source for reproduction. | Robert; handoff to Alan on S28. | 00:15 |
| S28 | Thank the audience and open the reserved Q&A. | Alan; Q&A follows. | 00:10 |

## Demo placement, evidence, and recovery

| Demo | Before action | Injection and extraction | Expected result and diagnostic | Fallback | Allotted time |
| --- | --- | --- | --- | --- | ---: |
| HTTP-to-CLI teaching example, S05-S10 | S05 architecture names both boundaries. | Java HTTP instrumentation injects and Go extracts. Go injects into a copied child environment; Python instrumentation extracts at startup. | S08 shows two roots when the child has no parent. S10 shows seven spans from three services under one trace after extraction. | S05-S07 and S09 native diagrams explain the result if captures fail. | 04:30 including screenshot transition |
| Argo and Docker build, S11-S19 | S17 shows the captured two-step clone/build DAG; S11-S13 establish the expected parent tree. | Argo controller injects into pod environments; the executor injects into the launched workload; BuildKit extracts. Git does not extract. | S18 preserves wrapper timing around git. S19 shows BuildKit subprocess spans and the slow build operation. The full clone/build/scan/push pattern is an extension of the captured two-stage run, not an observed four-stage trace. | S11-S13 native model and S16 injection diagram support the explanation if captures fail. | 06:15 including model and Argo mechanism; S17-S19 use 01:55 |
| Batch lineage, S20-S23 | S20 shows the complete extract/transform/load-style DAG and ownership boundary. | Argo injects into each pod; the platform-owned Python wrapper extracts before auto-instrumentation creates SQL spans. | S21 connects SQL spans; dataset inputs and outputs are derived from recorded `db.statement` metadata. S22 shows the graph. Without extraction, jobs become separate traces and the graph fragments. | S20 DAG and S23 extraction snippet preserve the path if a capture fails. | 02:40 including setup and transition |

All demonstrations use captured evidence; there is no live interaction or setup. The BuildKit capture covers clone and build only. A future four-stage capture would be needed to claim that scan and push were observed in this demo.

## Source-brief coverage matrix

| Description promise or selected benefit | Slides | Treatment |
| --- | --- | --- |
| HTTP/message metadata do not cross process launches | S05, S07-S08 | Broken HTTP-to-CLI example |
| Newcomer model: trace, span, parent, context, propagation, carrier, propagator | S06-S09 | Terms introduced in sequence |
| Environment carrier for CLI, subprocess, container, build step, batch job | S09-S10, S14-S23 | Explanation and evidence |
| Injected `TRACEPARENT` preserves one parent chain | S09-S10, S15-S16, S21-S23 | Launcher injection and child extraction; instrumentation creates spans |
| Multiple formats and consistent variable-name normalization | S09, S25 | W3C Trace Context, baggage, B3; `traceparent` to `TRACEPARENT` |
| Security and trust boundary | S25 | Inherited input, validation, log exposure, baggage allow-listing, scrubbing |
| CI/CD, workflow, build-tool, CLI, and instrumentation interoperability | S14-S19, S24 | Shared conventions avoid custom adapters |
| Argo/Docker example | S11-S19 | Captured clone/build, subprocesses, slow-stage diagnosis; scan/push shown as the intended extension, not captured evidence |
| Batch lineage example | S20-S23 | Instrumented jobs record SQL metadata; propagation connects spans and does not replace a lineage system |
| Practitioner feedback and specification maturity | S26 | Current status and feedback route verified 2026-09-29 |

## Portraits and sources

S01 uses the presenters' current GitHub profile photos. S02 and S03 contain marked portrait placeholders for the Prague photo quizzes; replace them when supplied, keeping the location hidden until the reveal. Technical sources and evidence provenance remain in each slide's `Delivery notes`.

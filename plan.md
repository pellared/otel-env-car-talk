# Presentation plan

## Audience outcome

Attendees leave with a simple model for carrying trace context across process launches, a clear division of responsibility between launchers and child instrumentation, and a safe checklist they can apply to CI/CD, workflow, CLI, build, and batch tooling.

## Narrative arc

1. Begin with a familiar pipeline whose trace fragments when work leaves the network and enters new processes.
2. Build one parent-child mental model for spans, trace context, propagation, carriers, propagators, and instrumentation.
3. Reuse that model to show the process-spawn contract, normalization rules, and security constraints.
4. Apply the contract in the Argo/Docker build and batch data-lineage demos.
5. Close with the interoperability payoff, the current specification status, and an adoption checklist.

## Presenter roles

| Presenter | Talk role | Affiliation and community |
| --- | --- | --- |
| Robert Pająk (`@pellared`) | Theoretician | Splunk and OpenTelemetry contributor |
| Alan Clucas (`@Joibel`) | Practitioner | Pipekit and Argo contributor |

## Sections and timing

| Section | Slides | Duration |
| --- | --- | ---: |
| Opening and broken trace | S01-S02 | 01:50 |
| Essential OpenTelemetry model | S03-S05 | 03:55 |
| Environment carrier mechanics and constraints | S06-S09 | 04:15 |
| Demo 1: Argo/Docker build | S10-S11 | 03:20 |
| Demo 2: batch pipeline and data lineage | S12-S13 | 03:20 |
| Interoperability, status, and recap | S14-S17 | 02:15 |
| Transition and recovery buffer | Between S09-S15 as needed | 01:05 |
| **Presentation allocation** | **S01-S17 plus buffer** | **20:00** |
| Questions and troubleshooting | S17 remains on screen | 05:00 outside presentation |

## Slide purposes and presenter ownership

| ID | Purpose | Lead and handoff | Duration |
| --- | --- | --- | ---: |
| S01 | Name the subject, introduce both presenters, and establish their complementary roles. | Practitioner leads; handoff to Theoretician, 00:05. | 00:25 |
| S02 | Make the broken-trace problem concrete at a process boundary. | Practitioner leads; handoff to Theoretician, 00:08. | 01:25 |
| S03 | Define span, trace, parent, child, and trace tree with one reusable model. | Theoretician leads; no handoff, 00:00. | 01:15 |
| S04 | Define trace context, propagation, and carrier while separating context flow from telemetry export. | Theoretician leads; handoff to Practitioner, 00:08. | 01:15 |
| S05 | Separate carrier, propagation format, propagator, and instrumentation. | Theoretician leads; one handoff to Practitioner, 00:10. | 01:25 |
| S06 | Compare HTTP-header propagation with environment-variable propagation. | Theoretician leads; no handoff, 00:00. | 00:55 |
| S07 | Show the parent and child responsibilities for a safe process launch. | Practitioner leads; one closing handoff to Theoretician, 00:08. | 01:20 |
| S08 | Explain environment-variable key normalization and opaque values. | Theoretician leads; no handoff, 00:00. | 00:50 |
| S09 | Frame inherited environments as trust boundaries and give safe deployment choices. | Theoretician leads; one handoff to Practitioner, 00:10. | 01:10 |
| S10 | Set up the pre-staged Argo/Docker demo and tell the audience what to watch. | Practitioner leads; no handoff, 00:00. | 00:35 |
| S11 | Open the pre-staged build trace, inspect the connected tree, and diagnose a slow or failed stage. | Practitioner leads; one closing handoff to Theoretician, 00:10. | 02:45 |
| S12 | Set up the pre-staged batch demo and distinguish context transport from dataset metadata. | Practitioner leads; no handoff, 00:00. | 00:35 |
| S13 | Open the pre-staged batch trace, inspect stage parentage and dataset inputs and outputs, then bound the lineage claim. | Practitioner leads; one closing handoff to Theoretician, 00:12. | 02:45 |
| S14 | Show how a shared carrier contract reduces adapters and fragmented conventions. | Practitioner leads; handoff to Theoretician, 00:10. | 00:55 |
| S15 | State the verified Release Candidate status and direct practitioner feedback toward stabilization. | Theoretician leads; handoff to Practitioner, 00:08. | 00:45 |
| S16 | Give a concise adoption checklist that preserves the central distinctions. | Theoretician leads; handoff to Practitioner, 00:06. | 00:30 |
| S17 | Close the talk and open the five-minute question and troubleshooting window. | Practitioner leads; no handoff, 00:00. | 00:05 |

## Demo placement and recovery paths

### Demo 1: Argo/Docker image build, S10-S11, 03:20

- Before the action: the audience sees the graph for a pre-run Argo workflow with clone, build, scan, and push steps plus the matching trace result in a collapsed state. The timebox contains trace exploration, not cluster startup or telemetry wait time.
- Injection: the talk's workflow integration around the Argo launcher creates the workflow span and injects that span's context into each step container environment. A custom Docker/BuildKit bridge explicitly forwards context into the build execution environment, where instrumented commands inject their current context into copied environments for local subprocesses. Neither stock Argo nor Docker automatically performs those bridge steps.
- Extraction: instrumentation in each step container extracts at startup; the custom BuildKit-side integration extracts inside the build execution environment; instrumented subprocesses extract again before creating their spans.
- Expected result: one workflow trace contains clone, build, scan, and push, with a bridged BuildKit execution branch and relevant subprocesses nested below build.
- Diagnostic insight: span duration shows where this run spent time, while span status identifies a failed stage. A separate build root points to Argo-side injection or startup extraction; a missing BuildKit branch points to the custom bridge; a missing compiler child points to local child launch or instrumentation.
- Fallback: S11 contains a prepared, clearly labeled trace-tree reconstruction with the same expected relationships and diagnostic states. If the backend or cluster fails, narrate that visual without changing the timebox.
- Allotted time: S10 setup and transition, 00:35; S11 interaction, interpretation, and fallback allowance, 02:45.

### Demo 2: Batch pipeline and data lineage, S12-S13, 03:20

- Before the action: the audience sees the definition for a pre-run batch pipeline with extract, transform, and load processes plus its trace result in a collapsed state. The timebox contains trace exploration, not job runtime or telemetry wait time.
- Injection: the scheduler or launcher injects the appropriate current context into a copied environment for each stage process.
- Extraction: each job's OpenTelemetry instrumentation extracts the incoming context and creates the stage span with that parent.
- Dataset metadata: the job instrumentation, not `TRACEPARENT`, records dataset inputs and outputs as span attributes.
- Expected result: one pipeline trace connects the stage spans while their metadata shows the run-specific flow from source data through the transformed dataset to the warehouse target.
- Diagnostic insight: a connected stage with missing dataset fields indicates an instrumentation gap; a separate trace root indicates a propagation or parent-selection problem.
- Fallback: S13 contains a prepared trace tree and dataset-flow reconstruction. If the live run or backend fails, narrate the prepared visual and preserve the distinction between propagation and instrumentation.
- Allotted time: S12 setup and transition, 00:35; S13 interaction, interpretation, and fallback allowance, 02:45.

## Coverage matrix

| README promise or required topic | Slides |
| --- | --- |
| HTTP headers and message metadata do not cover process launches | S02, S06 |
| Traces across CLI apps, CI/CD systems, build pipelines, containers, scripts, jobs, and subprocesses | S02, S06-S07, S10-S14 |
| Newcomer model of spans, traces, parent-child relationships, trace context, propagation, carriers, and propagators | S03-S05 |
| Environment variables as a process-boundary carrier | S06-S07 |
| Injected `TRACEPARENT` preserves continuity when child instrumentation extracts it | S06-S07, S10-S13 |
| Carrier model supports W3C Trace Context, B3, and other propagation formats | S05, S08, S14 |
| Environment-variable name normalization | S08 |
| Launcher injects; child instrumentation extracts and creates spans | S05, S07, S10-S13 |
| Environment variables alone do not create spans | S04-S05, S07, S13, S16 |
| Security: trust boundaries, untrusted inherited input, log and diagnostic leakage, allow-listing, and scrubbing | S09 |
| Interoperability for CI/CD, workflow engines, build tools, CLIs, and instrumentation | S14 |
| Shared model reduces custom glue and incompatible conventions | S14 |
| Argo/Docker build demo with clone, build, scan, push, subprocesses, and failure diagnosis | S10-S11 |
| Batch data-lineage demo with extract, transform, load, inherited context, and job-recorded dataset metadata | S12-S13 |
| Tracing contributes run-specific lineage without replacing every dedicated lineage capability | S13 |
| Real adoption and feedback inform guidance while the specification matures | S15 |
| Verified current specification status | S15 |

## Selected ecosystem benefits

- A shared environment carrier lets tool authors reuse configured propagators instead of inventing transport-specific trace encodings.
- Consistent key normalization improves portability across operating systems and language implementations.
- Concrete CI/CD and batch feedback can expose portability, concurrency, and trust-boundary gaps before stabilization.

No README Description promise or required demo is intentionally omitted.

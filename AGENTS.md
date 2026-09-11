# Instructions for presentation work

## Purpose

Help build a clear, technically accurate conference talk titled **Trace Context Beyond HTTP: Environment Variables as OpenTelemetry Propagation Carriers**.

The talk is for people interested in observability. Assume they understand why observability matters, but do not assume they know OpenTelemetry concepts, specifications, or terminology. Introduce each essential term in plain language before relying on it.

Treat [README.md](./README.md) as the source brief. Every substantive promise in the README must appear in the talk or be explicitly marked as deferred in `plan.md`. Do not silently remove, weaken, or invent claims.

## Non-negotiable constraints

- The delivered talk must take no more than 20 minutes.
- The event slot is 25 minutes. Reserve the final 5 minutes for questions and troubleshooting; do not count that time as presentation time.
- Two presenters deliver the talk together:
  - **Theoretician** explains the model, terminology, specification intent, interoperability, and constraints.
  - **Practitioner** explains implementation choices, operational consequences, demos, failure modes, and lessons from use.
- Both presenters may speak on the same slide or use case. Prefer a useful exchange of perspectives over two separate mini-talks.
- Keep the presentation approachable for OpenTelemetry newcomers without making it superficial for experienced attendees.
- The talk must cover the README's two examples: the Argo/Docker build pipeline and the batch data-lineage pipeline.

## Repository artifacts

Maintain these files with distinct roles:

1. `README.md` is the source brief and coverage checklist.
2. `plan.md` contains only the high-level presentation plan: audience outcome, narrative arc, sections, slide purposes, presenter ownership, demo placement, and timing.
3. `outline.md` contains only what the presenters intend to say. Organize it by slide and speaker, but do not add design instructions, TODOs, citations, rehearsal commentary, or production notes.
4. `slides.md` is the Slidev presentation. It contains audience-facing slide content and speaker notes. The notes must include the matching spoken content from `outline.md`.

Create missing artifacts only when the requested task calls for them. Do not put slide copy into `plan.md`, and do not turn `outline.md` into a second deck.

## Source-of-truth and synchronization rules

- Develop in this order: `README.md` coverage check, `plan.md`, `outline.md`, then `slides.md`.
- Treat `outline.md` as the canonical spoken narrative.
- Give every planned slide a stable identifier such as `S01`, `S02`, and use the same identifier in all three presentation artifacts.
- Copy each slide's spoken content into that slide's speaker notes in `slides.md`. Keep speaker labels and wording synchronized.
- A change to the narrative must update `outline.md` and the corresponding notes in `slides.md` in the same task.
- A change that affects structure or timing must also update `plan.md`.
- Audience-facing slide text does not need to repeat the spoken outline. Slides should support the explanation visually and remain readable at a distance.
- Before finishing, compare the three artifacts and report any intentional discrepancy.

## Required content coverage

Use the README as the final authority, but make sure the narrative addresses all of the following:

- Why conventional propagation through HTTP headers or message metadata does not cover process boundaries.
- A simple newcomer-friendly model of traces, spans, parent-child relationships, trace context, propagation, carriers, and propagators.
- Environment variables as a carrier when one process launches another process, container, script, CLI, build step, or batch job.
- How an injected `TRACEPARENT` can preserve trace continuity across those boundaries.
- The fact that the carrier model can support multiple propagation formats rather than locking users into one ecosystem-specific encoding.
- Environment-variable name normalization and why implementations need consistent rules.
- The responsibility of the process that spawns work to inject context, and of the child instrumentation to extract it.
- Security constraints. Discuss trust boundaries, untrusted inherited environments, leakage into logs or diagnostics, and deliberate allow-listing or scrubbing where appropriate.
- Interoperability benefits for CI/CD systems, workflow engines, build tools, CLIs, and OpenTelemetry instrumentation.
- How a shared model reduces custom glue and incompatible trace conventions.
- How practitioner feedback can inform implementation guidance and future refinements while the relevant specification work matures. Verify current specification status before making a time-sensitive claim.

Do not imply that an environment variable creates spans by itself. Injection and extraction carry context; instrumentation creates spans and assigns parentage. Do not imply that `TRACEPARENT` alone provides data lineage. In the lineage example, the instrumented jobs record dataset inputs and outputs as span metadata while propagation connects the work into one trace.

## Demo requirements

For each demo, the artifacts must state:

- what the audience sees before the action begins;
- who injects and who extracts context;
- the expected trace or span-tree result;
- the failure or diagnostic insight to point out;
- a fallback visual or prerecorded path if the live demo fails;
- the allotted time, including setup and transition.

Keep setup details in `plan.md` or slide notes, not in `outline.md`, unless the presenters will say them aloud.

### Demo 1: Docker image build in Argo Workflows

Show a workflow in which the controller or launcher injects `TRACEPARENT` into step containers. Clone, build, scan, and push should appear under one trace, including relevant subprocesses spawned by build tooling. The operational payoff is locating the slow or failed stage.

### Demo 2: Batch pipeline and data lineage

Show extract, transform, and load processes inheriting trace context through their environments. Make clear which instrumentation records dataset inputs and outputs. The span tree should connect the job stages and expose useful lineage without claiming that tracing replaces every dedicated lineage capability.

## Narrative and teaching approach

- Start from a familiar broken-trace problem, then introduce only the concepts needed to solve it.
- Define a term immediately before its first use. Prefer one sentence and a concrete example.
- Use HTTP propagation as a familiar comparison, then show what changes at a process boundary.
- Separate the carrier from the propagation format and from the instrumentation. This distinction is central to the talk.
- Reuse one simple parent-child mental model through the explanation and both demos.
- Connect theory to practice on the same slide when it helps: the Theoretician explains the rule, then the Practitioner shows the operational consequence or implementation choice.
- End with a concise recap that attendees can apply to their own tools and workflows.
- Avoid long specification quotations, unexplained acronyms, and dense taxonomy slides.

## Two-presenter choreography

Label spoken sections as `Theoretician:` and `Practitioner:` in `outline.md` and speaker notes. Each slide in `plan.md` must identify a lead presenter and any handoff.

Use handoffs to add meaning. Good patterns include:

- model followed by implementation consequence;
- expected behavior followed by a failure seen in practice;
- specification constraint followed by a safe deployment choice;
- shared narration of one demo from trace-model and operator perspectives.

Do not alternate speakers mechanically on every slide. Avoid repeating the same explanation in different words. Keep handoffs short enough that they feel rehearsed rather than interruptive.

## Timing and scope control

Add an explicit duration to every section, slide, demo, and handoff in `plan.md`. The durations must total at most `20:00`.

Use this default budget unless the content supports a better allocation:

- opening and problem: about 2 minutes;
- essential OpenTelemetry model: about 4 minutes;
- environment carrier mechanics and constraints: about 4 minutes;
- two demos: about 7 minutes total;
- implications and recap: about 2 minutes;
- transition and recovery buffer: at least 1 minute.

Budget live-demo interaction as well as speech. Estimate spoken time at roughly 125 to 140 words per minute, then rehearse with a timer. Cut optional detail before reducing the recovery buffer. Never plan content for the 5-minute Q&A window.

## Slide and speaker-note conventions

- Keep using Slidev syntax in `slides.md` and preserve valid frontmatter and slide separators.
- Give each slide one clear job and a direct title that names the subject or supported takeaway.
- Keep visible text concise. Prefer a diagram, trace tree, code fragment, terminal excerpt, or demo frame when it communicates the idea faster than prose.
- Make code and environment-variable examples large enough to read from the back of a room.
- Avoid dashboard-like grids, decorative UI panels, filler slogans, and paragraphs on slides.
- Use plain, direct language and active voice. Avoid unexplained jargon and hype.
- In speaker notes, retain the `Theoretician:` and `Practitioner:` labels from `outline.md`.
- Put stage directions, demo controls, timing cues, fallbacks, and citations in clearly marked note subsections outside the synchronized spoken text.
- Cite external facts, specification claims, and borrowed visuals in the relevant slide notes. Prefer primary OpenTelemetry and W3C sources for technical claims.
- Never invent a specification status, API, demo result, benchmark, citation, or source.

A recommended Slidev note shape is:

```md
<!--
Spoken outline:
Theoretician: ...
Practitioner: ...

Delivery notes:
- Time: 01:15
- Handoff: ...
- Demo cue or fallback: ...
- Sources: ...
-->
```

Only the lines under `Spoken outline` belong in `outline.md`.

## Working method

When asked to build or revise the talk:

1. Read this file, `README.md`, and every existing presentation artifact before editing.
2. Make a small coverage matrix mapping README promises to slide IDs in `plan.md`.
3. Check the narrative arc, terminology order, presenter roles, and timing before polishing slide copy.
4. Update all affected artifacts according to the synchronization rules.
5. Run `npm run build` after changing `slides.md`.
6. Preview or export the deck when layout changed, and inspect every slide for clipping, unreadable text, bad contrast, and broken assets.
7. Rehearse or estimate the complete delivery, including demos and handoffs, and keep it at or below 20 minutes.

When reviewing rather than editing, identify exact slide IDs and distinguish blocking issues from optional improvements.

## Definition of done

Presentation work is complete only when:

- every README promise maps to at least one planned slide;
- the story works for an observability audience new to OpenTelemetry terminology;
- the Theoretician and Practitioner have distinct, complementary contributions;
- both demos have an expected result and a failure fallback;
- `outline.md` contains only spoken content;
- the spoken content in `outline.md` matches the notes in `slides.md`;
- all planned durations total no more than 20 minutes and leave the 5-minute Q&A untouched;
- technical and time-sensitive claims have appropriate primary sources;
- `npm run build` succeeds after slide changes;
- visual inspection finds no overflow, clipping, unreadable content, or broken assets.

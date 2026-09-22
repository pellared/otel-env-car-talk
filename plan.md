# Presentation plan

## Current delivery

This opening sequence establishes the talk and the presenters before the technical narrative begins. It uses `02:50` of the 20-minute presentation allowance, including a one-minute audience photo quiz on each presenter slide. The final 5 minutes remain reserved for questions and troubleshooting.

## Audience outcome

Attendees will know who frames the OpenTelemetry model and who demonstrates its operational consequences before the talk moves into process-boundary tracing.

## Narrative arc

1. S01 names the subject and establishes the two presenters.
2. S02 introduces Robert Pająk, who works at Splunk and maintains OpenTelemetry Go and the OpenTelemetry Specification, then asks the audience where his portrait was taken.
3. S03 introduces Alan Clucas, who works at Pipekit and leads Argo Workflows, then asks the audience where his portrait was taken.
4. The next requested section should begin with the familiar broken-trace problem, then introduce OpenTelemetry terms immediately before use.

## Sections and timing

| Section | Slides | Duration |
| --- | --- | ---: |
| Opening: title, presenter roles, and photo quizzes | S01-S03 | 02:50 |
| Remaining presentation material | Not yet authored | Up to 17:10 |
| Questions and troubleshooting | After the presentation | 05:00 outside the 20:00 presentation |

## Slide purposes, presenter ownership, and handoffs

| ID | Purpose | Lead and handoff | Duration |
| --- | --- | ---: | ---: |
| S01 | Name the talk and both authors. | Theoretician leads; 00:05 handoff to Practitioner within the slide. | 00:20 |
| S02 | Introduce Robert Pająk, Splunk, and his OpenTelemetry roles, then run a photo-location quiz. | Theoretician leads. | 01:15, including 01:00 quiz |
| S03 | Introduce Alan Clucas, Pipekit, and his Argo Workflows role, then run a photo-location quiz. | Practitioner leads. | 01:15, including 01:00 quiz |

## Photo placement

S01 uses the presenters’ current GitHub profile photos. S02 and S03 reserve portrait-oriented areas for photos taken in Prague. Each portrait supports a one-minute audience quiz asking where the photo was taken. The location must remain hidden from the audience-facing slide until the presenter reveals the answer. The placeholders do not use avatars or substitute imagery and should be replaced only after the presenters provide the corresponding photos.

## Source-brief coverage matrix

This request deliberately creates the opening only. The non-opening Description promises, Benefits points, and both demos are deferred rather than implied by introductory copy. They must map to slide IDs when their sections are added.

| Source brief item | Opening slide mapping | Status |
| --- | --- | --- |
| Process boundaries break HTTP or message-metadata propagation. | None | Deferred after S03 |
| Environment variables carry trace context across launched processes. | None | Deferred after S03 |
| Multiple propagation formats remain interoperable. | None | Deferred after S03 |
| Injected `TRACEPARENT` connects stages, subprocesses, and trace evidence. | None | Deferred after S03 |
| Argo/Docker build demo. | None | Deferred after S03 |
| Batch data-lineage demo. | None | Deferred after S03 |
| Normalization, spawn and extract responsibilities, and security constraints. | None | Deferred after S03 |
| Interoperability, less custom glue, and adoption feedback. | None | Deferred after S03 |
| Complementary Theoretician and Practitioner roles. | S01-S03 | Covered |

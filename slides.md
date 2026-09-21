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

# One Trace, Two Carriers: HTTP to a Report CLI

This minimal demo shows an HTTP trace context crossing a process boundary and a
language boundary:

```text
Java client -- HTTP traceparent --> Go report API -- TRACEPARENT --> Python report CLI
```

The Go API receives a standard W3C Trace Context HTTP header and starts a
`run report.py` span. In the fixed run, it injects the active context into a
copied child environment; the Python CLI extracts that context at startup and
creates `build report` (including its `fetch data` and `generate pdf` children)
beneath it.

No code parses a trace ID or synthesizes a propagation format. Both programs
use W3C Trace Context; only the carrier changes from an HTTP header named
`traceparent` to an environment variable named `TRACEPARENT`.

## Prerequisites

- Docker Engine with Docker Compose v2

## Run it

Start Jaeger and the API:

```sh
make up
```

### First: show the broken trace

Send the normal request from the Java client. It forwards W3C trace context over
HTTP, but deliberately does **not** inject it into the Python process
environment:

```sh
make run
```

Open [Jaeger](http://localhost:16686), choose the `report-api` service, and
select the recent trace. It has the HTTP portion, but it does **not** contain
the Python CLI spans, `build report`, `fetch data`, and `generate pdf`:

```text
request report                 (Java report-client)
└── HTTP POST                  (Java HTTP client span)
    └── POST /report           (report-api HTTP span)
        └── run report.py      (report-api)
```

The Python process logs a different trace ID and exports `build report` as a
separate root trace. This is the familiar broken-trace problem at a process
boundary.

### Then: apply the environment-carrier fix

Repeat the same request with the Go API injecting `TRACEPARENT` into the child
environment and the Python CLI extracting it:

```sh
make fixed
```

Open the newest `report-api` trace. It should contain one connected tree:

```text
request report                 (Java report-client)
└── HTTP POST                  (Java HTTP client span)
    └── POST /report           (report-api HTTP span)
        └── run report.py      (report-api)
            └── build report  (report-cli)
                ├── fetch data
                └── generate pdf
```

Every span now has the same trace ID. The browser trace viewer lets you expand
the tree and compare timing across the HTTP request, process launch, and CLI
work.

`make broken` remains an alias for `make run`. The `propagate=true` switch only
affects that request, so no container restart is needed between the two steps.

## What to point out

1. HTTP instrumentation extracts and injects `traceparent` at the network
   boundary.
2. The application, not the OpenTelemetry SDK, is responsible for launching
   the child process.
3. Before the launch, the Go API copies its environment and uses the configured
   propagator with the environment carrier to write `TRACEPARENT` into that
   copy.
4. The Python CLI extracts once from its startup environment, then its
   instrumentation creates `build report` with the extracted parent and the
   nested `fetch data` and `generate pdf` spans.

The environment carrier transports propagation fields; it does not create
spans or automatically start child processes.

## Useful commands

```sh
make logs  # follow report-api logs, including the Python CLI output
make down  # stop this demo's containers
```

## Implementation notes

- The Go API uses `go.opentelemetry.io/contrib/propagators/envcar` to inject
  into the child command's environment without mutating the API process's own
  environment.
- The Java client creates an HTTP client span and uses the configured W3C
  propagator to inject `traceparent` into its request.
- The Python CLI uses OpenTelemetry Python's `EnvironmentGetter` to extract
  from `os.environ` at startup.
- The demo deliberately propagates only trace context. Do not use environment
  propagation for secrets, and review or remove baggage before crossing a
  trust boundary.

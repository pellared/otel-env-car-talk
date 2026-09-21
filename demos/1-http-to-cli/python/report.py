"""A small report CLI that continues context passed in its startup environment."""

from __future__ import annotations

import os
import time

from opentelemetry import propagate, trace
from opentelemetry.exporter.otlp.proto.http.trace_exporter import OTLPSpanExporter
from opentelemetry.propagators._envcarrier import EnvironmentGetter
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import SimpleSpanProcessor
from opentelemetry.trace.propagation.tracecontext import TraceContextTextMapPropagator


DEFAULT_ENDPOINT = "http://localhost:4318/v1/traces"


def configure_telemetry() -> TracerProvider:
    """Configure W3C Trace Context and a synchronous OTLP/HTTP exporter."""
    propagate.set_global_textmap(TraceContextTextMapPropagator())
    provider = TracerProvider(resource=Resource.create({"service.name": "report-cli"}))
    exporter = OTLPSpanExporter(
        endpoint=os.getenv("OTEL_EXPORTER_OTLP_TRACES_ENDPOINT", DEFAULT_ENDPOINT)
    )
    provider.add_span_processor(SimpleSpanProcessor(exporter))
    trace.set_tracer_provider(provider)
    return provider


def parent_trace_id(context: object) -> str | None:
    span_context = trace.get_current_span(context).get_span_context()
    if not span_context.is_valid:
        return None
    return format(span_context.trace_id, "032x")


def main() -> None:
    provider = configure_telemetry()
    try:
        # Extraction happens once at startup. The carrier normalizes the
        # propagator's `traceparent` key to the `TRACEPARENT` environment name.
        parent_context = propagate.extract(os.environ, getter=EnvironmentGetter())
        trace_id = parent_trace_id(parent_context)
        if trace_id:
            print(f"report-cli: extracted TRACEPARENT for trace {trace_id}")
        else:
            print("report-cli: no TRACEPARENT found; build report starts a new trace")

        tracer = trace.get_tracer("demo/report-cli")
        with tracer.start_as_current_span("build report", context=parent_context):
            with tracer.start_as_current_span("fetch data"):
                time.sleep(0.35)
                print("report-cli: fetched source data")
            with tracer.start_as_current_span("generate pdf"):
                time.sleep(0.15)
                print("report-cli: wrote quarterly-summary.pdf")
    finally:
        provider.shutdown()


if __name__ == "__main__":
    main()

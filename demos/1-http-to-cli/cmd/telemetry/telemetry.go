package telemetry

import (
	"context"
	"os"

	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/attribute"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracehttp"
	"go.opentelemetry.io/otel/propagation"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
)

const defaultTracesEndpoint = "http://localhost:4318/v1/traces"

// Init configures a small, always-sampling tracer provider for this demo.
// A synchronous exporter keeps traces visible immediately after the client exits.
func Init(ctx context.Context, serviceName string) (func(context.Context) error, error) {
	endpoint := os.Getenv("OTEL_EXPORTER_OTLP_TRACES_ENDPOINT")
	if endpoint == "" {
		endpoint = defaultTracesEndpoint
	}

	exporter, err := otlptracehttp.New(ctx, otlptracehttp.WithEndpointURL(endpoint))
	if err != nil {
		return nil, err
	}

	provider := sdktrace.NewTracerProvider(
		sdktrace.WithSampler(sdktrace.AlwaysSample()),
		sdktrace.WithResource(resource.NewWithAttributes(
			"",
			attribute.String("service.name", serviceName),
		)),
		sdktrace.WithSyncer(exporter),
	)

	otel.SetTracerProvider(provider)
	otel.SetTextMapPropagator(propagation.TraceContext{})

	return provider.Shutdown, nil
}

package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"os"
	"os/exec"
	"strings"
	"time"

	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
	"go.opentelemetry.io/contrib/propagators/envcar"
	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/codes"
	"go.opentelemetry.io/otel/trace"

	"github.com/ropajak/otel-env-car-talk/demos/1-http-to-cli/cmd/telemetry"
)

const reportScript = "/app/report.py"

func main() {
	ctx := context.Background()
	shutdown, err := telemetry.Init(ctx, "report-api")
	if err != nil {
		log.Fatalf("configure OpenTelemetry: %v", err)
	}
	defer func() {
		if err := shutdown(context.Background()); err != nil {
			log.Printf("shut down OpenTelemetry: %v", err)
		}
	}()

	mux := http.NewServeMux()
	mux.HandleFunc("GET /health", health)
	mux.HandleFunc("POST /report", createReport)

	handler := otelhttp.NewHandler(mux, "report-api")
	server := &http.Server{
		Addr:              ":8080",
		Handler:           handler,
		ReadHeaderTimeout: 5 * time.Second,
	}

	log.Printf("report API listening on %s", server.Addr)
	log.Fatal(server.ListenAndServe())
}

func health(w http.ResponseWriter, _ *http.Request) {
	w.WriteHeader(http.StatusNoContent)
}

func createReport(w http.ResponseWriter, r *http.Request) {
	ctx, span := otel.Tracer("demo/report-api").Start(r.Context(), "run report.py")
	defer span.End()

	propagate := r.URL.Query().Get("propagate") == "true"
	if propagate {
		fmt.Printf("report-api: received HTTP context for trace %s\n", traceID(ctx))
	} else {
		fmt.Printf("report-api: received HTTP context for trace %s; deliberately omitting the environment handoff\n", traceID(ctx))
	}

	output, err := runReport(ctx, propagate)
	if err != nil {
		span.RecordError(err)
		span.SetStatus(codes.Error, "report CLI failed")
		http.Error(w, "report CLI failed", http.StatusInternalServerError)
		log.Printf("report CLI: %v\n%s", err, output)
		return
	}

	fmt.Print(string(output))
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(map[string]any{
		"report":       "quarterly-summary.pdf",
		"propagated":   propagate,
		"trace_id":     traceID(ctx),
		"instructions": "Open Jaeger at http://localhost:16686 and select report-api.",
	})
}

func runReport(ctx context.Context, propagate bool) ([]byte, error) {
	cmd := exec.CommandContext(ctx, "python3", reportScript)
	cmd.Env = childEnvironment(os.Environ())

	if propagate {
		carrier := envcar.Carrier{
			SetEnvFunc: func(key, value string) {
				cmd.Env = setEnvironment(cmd.Env, key, value)
			},
		}
		otel.GetTextMapPropagator().Inject(ctx, &carrier)
		fmt.Printf("report-api: injected TRACEPARENT into the Python child environment for trace %s\n", traceID(ctx))
	} else {
		fmt.Println("report-api: skipped TRACEPARENT injection into the Python child environment")
	}

	return cmd.CombinedOutput()
}

func childEnvironment(environment []string) []string {
	// Do not accidentally pass a stale propagation value from the API's own
	// environment. Each child gets a fresh environment copy and, when enabled,
	// a context injected for this exact spawn.
	filtered := make([]string, 0, len(environment))
	for _, entry := range environment {
		key, _, _ := strings.Cut(entry, "=")
		switch key {
		case "TRACEPARENT", "TRACESTATE", "BAGGAGE":
			continue
		default:
			filtered = append(filtered, entry)
		}
	}
	return filtered
}

func setEnvironment(environment []string, key, value string) []string {
	prefix := key + "="
	for index, entry := range environment {
		if strings.HasPrefix(entry, prefix) {
			environment[index] = prefix + value
			return environment
		}
	}
	return append(environment, prefix+value)
}

func traceID(ctx context.Context) string {
	spanContext := trace.SpanContextFromContext(ctx)
	if !spanContext.IsValid() {
		return "(no valid trace context)"
	}
	return spanContext.TraceID().String()
}

package io.opentelemetry.demo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class ReportClient {
  private static final String DEFAULT_API_URL = "http://localhost:8080/report";
  private static final String DEFAULT_TRACES_ENDPOINT = "http://localhost:4318/v1/traces";
  private static final TextMapSetter<HttpRequest.Builder> HTTP_REQUEST_SETTER =
      (carrier, key, value) -> carrier.header(key, value);

  private ReportClient() {}

  public static void main(String[] args) throws Exception {
    String apiUrl = System.getenv().getOrDefault("REPORT_API_URL", DEFAULT_API_URL);
    if ("true".equals(System.getenv("DEMO_PROPAGATE_ENV"))) {
      apiUrl = enableEnvironmentPropagation(apiUrl);
    }
    String tracesEndpoint =
        System.getenv().getOrDefault("OTEL_EXPORTER_OTLP_TRACES_ENDPOINT", DEFAULT_TRACES_ENDPOINT);

    waitForEndpoint(apiUrl, 80, "report API");
    waitForEndpoint(tracesEndpoint, 4318, "OTLP collector");

    OpenTelemetrySdk sdk = configureTelemetry(tracesEndpoint);
    try {
      requestReport(apiUrl);
    } finally {
      sdk.getSdkTracerProvider().shutdown().join(10, TimeUnit.SECONDS);
    }
  }

  private static OpenTelemetrySdk configureTelemetry(String tracesEndpoint) {
    OtlpHttpSpanExporter exporter =
        OtlpHttpSpanExporter.builder().setEndpoint(tracesEndpoint).build();
    Resource resource =
        Resource.getDefault()
            .merge(
                Resource.create(
                    Attributes.of(AttributeKey.stringKey("service.name"), "report-client")));
    SdkTracerProvider tracerProvider =
        SdkTracerProvider.builder()
            .setSampler(Sampler.alwaysOn())
            .setResource(resource)
            .addSpanProcessor(SimpleSpanProcessor.create(exporter))
            .build();

    return OpenTelemetrySdk.builder()
        .setTracerProvider(tracerProvider)
        .setPropagators(
            ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
        .buildAndRegisterGlobal();
  }

  private static void requestReport(String apiUrl) throws IOException, InterruptedException {
    OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();
    Tracer tracer = openTelemetry.getTracer("demo/report-client");
    Span requestSpan = tracer.spanBuilder("request report").startSpan();

    try (Scope requestScope = requestSpan.makeCurrent()) {
      Span httpSpan = tracer.spanBuilder("HTTP POST").setSpanKind(SpanKind.CLIENT).startSpan();
      try (Scope httpScope = httpSpan.makeCurrent()) {
        HttpRequest.Builder requestBuilder =
            HttpRequest.newBuilder(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.noBody());

        openTelemetry
            .getPropagators()
            .getTextMapPropagator()
            .inject(Context.current(), requestBuilder, HTTP_REQUEST_SETTER);

        HttpResponse<String> response =
            HttpClient.newHttpClient().send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        httpSpan.setAttribute("http.request.method", "POST");
        httpSpan.setAttribute("http.response.status_code", response.statusCode());
        System.out.printf("report-client: API returned %d%n%s%n", response.statusCode(), response.body());
      } catch (IOException | InterruptedException error) {
        httpSpan.recordException(error);
        httpSpan.setStatus(StatusCode.ERROR, "report API call failed");
        throw error;
      } finally {
        httpSpan.end();
      }
    } finally {
      requestSpan.end();
    }
  }

  private static String enableEnvironmentPropagation(String apiUrl) {
    URI parsed = URI.create(apiUrl);
    String separator = parsed.getQuery() == null ? "?" : "&";
    return apiUrl + separator + "propagate=true";
  }

  private static void waitForEndpoint(String rawUrl, int defaultPort, String name)
      throws InterruptedException {
    URI endpoint = URI.create(rawUrl);
    String host = endpoint.getHost();
    if (host == null || host.isBlank()) {
      throw new IllegalArgumentException(name + " URL has no host: " + rawUrl);
    }
    int port = endpoint.getPort() == -1 ? defaultPort : endpoint.getPort();
    InetSocketAddress address = new InetSocketAddress(host, port);
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(20);

    while (System.nanoTime() < deadline) {
      try (var socket = new java.net.Socket()) {
        socket.connect(address, 500);
        return;
      } catch (IOException ignored) {
        Thread.sleep(250);
      }
    }
    throw new IllegalStateException(name + " at " + address + " did not become reachable within 20 seconds");
  }
}

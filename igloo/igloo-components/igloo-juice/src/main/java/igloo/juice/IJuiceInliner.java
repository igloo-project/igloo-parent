package igloo.juice;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.io.IOException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Optional;
import java.util.function.UnaryOperator;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(typeImmutable = "*", typeAbstract = "I*")
public interface IJuiceInliner {

  @Value.Default
  default ObjectMapper objectMapper() {
    return new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
  }

  @Value.Default
  default HttpClient httpClient() {
    return HttpClient.newBuilder().followRedirects(Redirect.NORMAL).build();
  }

  @Value.Default
  default UnaryOperator<HttpRequest.Builder> requestBuilderHandler() {
    return UnaryOperator.identity();
  }

  @Value.Default
  default UnaryOperator<RetryConfig.Builder<HttpResponse<String>>> retryConfigBuilderHandler() {
    return UnaryOperator.identity();
  }

  @Value.Default
  default CircuitBreaker circuitBreaker() {
    return CircuitBreaker.ofDefaults("juice-css");
  }

  @Value.Default
  default Duration requestTimeout() {
    return Duration.ofMinutes(1);
  }

  @Value.Default
  default int retryMaxAttempts() {
    return 3;
  }

  @Value.Default
  default Duration retryWaitDuration() {
    return Duration.ofMillis(500);
  }

  URL inlinerUrl();

  Optional<String> token();

  default String postProcessJuice(String html, String extraCss) {
    try {
      InlinerQuery query = new InlinerQuery();
      query.setContent(html);

      InlinerOptions options = new InlinerOptions();
      options.setExtraCss(extraCss);
      options.setRemoveStyleTags(false);
      query.setOptions(options);

      String rawQuery = objectMapper().writeValueAsString(query);

      Builder httpRequestBuilder =
          HttpRequest.newBuilder(inlinerUrl().toURI())
              .method("GET", BodyPublishers.ofString(rawQuery))
              .header("Content-Type", "application/json")
              .timeout(requestTimeout());
      if (token().isPresent()) {
        httpRequestBuilder.header("Authorization", "Bearer %s".formatted(token().get()));
      }
      HttpRequest request = requestBuilderHandler().apply(httpRequestBuilder).build();

      var retryConfigBuilder =
          RetryConfig.<HttpResponse<String>>custom()
              .maxAttempts(retryMaxAttempts())
              .waitDuration(retryWaitDuration())
              .retryOnResult(response -> response.statusCode() != 200)
              .retryExceptions(IOException.class);
      Retry retry =
          Retry.of("juice-css", retryConfigBuilderHandler().apply(retryConfigBuilder).build());

      HttpResponse<String> response =
          Decorators.ofCallable(() -> httpClient().send(request, BodyHandlers.ofString()))
              .withCircuitBreaker(circuitBreaker())
              .withRetry(retry)
              .call();

      if (response.statusCode() != 200) {
        throw new IllegalStateException(
            "Error inlining HTML CSS, response status code %s.".formatted(response.statusCode()));
      }

      return response.body();
    } catch (Exception e) {
      if (e instanceof InterruptedException) { // NOSONAR
        Thread.currentThread().interrupt();
      }
      throw new IllegalStateException("Error inlining HTML CSS", e);
    }
  }
}

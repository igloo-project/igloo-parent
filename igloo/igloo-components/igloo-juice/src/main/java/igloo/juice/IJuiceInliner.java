package igloo.juice;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
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

  /**
   * @return Retry registry to use to register {@link Retry} object. If not provided a private
   *     registry is used.
   */
  @Value.Default
  default RetryRegistry retryRegistry() {
    return RetryRegistry.ofDefaults();
  }

  /**
   * @return {@link CircuitBreakerRegistry} tp use to register {@link CircuitBreaker}. If not
   *     provided, a private registry is used.
   */
  @Value.Default
  default CircuitBreakerRegistry circuitBreakerRegistry() {
    return CircuitBreakerRegistry.ofDefaults();
  }

  /**
   * @return ObjectMapper used to perform query serialization.
   */
  @Value.Default
  default ObjectMapper objectMapper() {
    return new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
  }

  /**
   * @return {@link HttpClient} used to perform requests.
   */
  @Value.Default
  default HttpClient httpClient() {
    return HttpClient.newBuilder().followRedirects(Redirect.NORMAL).build();
  }

  /**
   * @return Operator used to customize {@link HttpRequest.Builder} object.
   */
  @Value.Default
  default UnaryOperator<HttpRequest.Builder> requestBuilderHandler() {
    return UnaryOperator.identity();
  }

  /**
   * @return Operator used to customize {@link RetryConfig} object.
   */
  @Value.Default
  default UnaryOperator<RetryConfig.Builder<HttpResponse<String>>> retryConfigBuilderHandler() {
    return UnaryOperator.identity();
  }

  /**
   * @return Operator used to customize {@link CircuitBreakerConfig} object.
   */
  @Value.Default
  default UnaryOperator<CircuitBreakerConfig.Builder> circuitBreakerBuilderHandler() {
    return UnaryOperator.identity();
  }

  /**
   * Configure a default {@link CircuitBreaker}. Slow threshold is 1 min. Slow rate is 100%. Failure
   * rate is 50%. Wait duration for OPEN to HALF OPEN is 1 min.
   *
   * @return {@link CircuitBreaker} instance.
   */
  @Value.Derived
  default CircuitBreaker circuitBreaker() {
    CircuitBreakerConfig.Builder configBuilder =
        CircuitBreakerConfig.from(CircuitBreakerConfig.ofDefaults());
    CircuitBreakerConfig config = circuitBreakerBuilderHandler().apply(configBuilder).build();
    return circuitBreakerRegistry().circuitBreaker("juice-css", config);
  }

  /**
   * Object that controls request retry attempts. Request is retried if response status is not 200,
   * or if requests fails with a {@link IOException} (timeout triggers a {@link IOException}
   * subclass).
   *
   * @return {@link Retry} instance.
   */
  @Value.Derived
  default Retry retry() {
    var retryConfigBuilder =
        RetryConfig.<HttpResponse<String>>custom()
            .maxAttempts(retryMaxAttempts())
            .waitDuration(retryWaitDuration())
            .retryOnResult(response -> response.statusCode() != 200)
            .retryExceptions(IOException.class);
    return retryRegistry()
        .retry("juice-css", retryConfigBuilderHandler().apply(retryConfigBuilder).build());
  }

  /**
   * When timeout is reached, request is retried accordingly to {@link Retry} configuration.
   *
   * @return timeout for inlining request.
   */
  @Value.Default
  default Duration requestTimeout() {
    return Duration.ofMinutes(1);
  }

  /**
   * @return Max attempts to perform inlining.
   */
  @Value.Default
  default int retryMaxAttempts() {
    return 3;
  }

  /**
   * @return Wait time between request attempts.
   */
  @Value.Default
  default Duration retryWaitDuration() {
    return Duration.ofMillis(500);
  }

  /**
   * @return Inliner service URL
   */
  URL inlinerUrl();

  /**
   * @return Token for inliner service (sent as <code>Authorization: Bearer ...</code> header).
   */
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

      HttpResponse<String> response =
          Decorators.ofCallable(() -> httpClient().send(request, BodyHandlers.ofString()))
              .withCircuitBreaker(circuitBreaker())
              .withRetry(retry())
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

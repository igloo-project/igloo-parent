package igloo.juice;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

public class JuiceInliner {

	public HttpClient inlinerHttpClient = HttpClient.newBuilder()
		.followRedirects(Redirect.NORMAL)
		.build();
	public ObjectMapper inlinerObjectMapper = new ObjectMapper()
		.configure(SerializationFeature.INDENT_OUTPUT, true)
		.setSerializationInclusion(Include.NON_NULL);

	public String postProcessJuice(String html, String extraCss) {
		try {
			InlinerQuery query = new InlinerQuery();
			query.setContent(html);
			
			InlinerOptions options = new InlinerOptions();
			options.setExtraCss(extraCss);
			options.setRemoveStyleTags(false);
			query.setOptions(options);
			
			String rawQuery = inlinerObjectMapper.writeValueAsString(query);
			
			HttpRequest request = HttpRequest
				.newBuilder(URI.create("http://localhost:8000/juice"))
				.method("GET", BodyPublishers.ofString(rawQuery))
				.header("Content-Type", "application/json")
				.timeout(Duration.ofMinutes(1))
				.build();
			
			CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("juice-css");
			
			Retry retry =  Retry.of(
				"juice-css",
				RetryConfig.<HttpResponse<String>>custom()
					.maxAttempts(3)
					.waitDuration(Duration.ofMillis(500))
					.retryOnResult(response -> response.statusCode() != 200)
					.retryExceptions(IOException.class, HttpTimeoutException.class)
					.build()
			);
			
			HttpResponse<String> response = Decorators.ofCallable(() -> inlinerHttpClient.send(request, BodyHandlers.ofString()))
				.withCircuitBreaker(circuitBreaker)
				.withRetry(retry)
				.call();
			
			if (response.statusCode() != 200) {
				throw new IllegalStateException("Error inlining HTML CSS, response status code %s.".formatted(response.statusCode()));
			}
			
			return response.body();
		} catch (Exception e) {
			if (e instanceof InterruptedException) { //NOSONAR
				Thread.currentThread().interrupt();
			}
			throw new IllegalStateException("Error inlining HTML CSS", e);
		}
	}

}

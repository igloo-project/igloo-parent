package test;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.matchers.Times;
import org.mockserver.model.Delay;
import org.mockserver.model.JsonBody;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;

import igloo.juice.IJuiceInliner;
import igloo.juice.JuiceInliner;

@ExtendWith(MockServerExtension.class)
class TestJuiceInliner {

	private ClientAndServer server;

	@BeforeEach
	public void initMockerServer(ClientAndServer server) {
		this.server = server;
		server.reset();
	}

	/**
	 * Test a standard call.
	 */
	@Test
	void testSuccess() throws MalformedURLException {
		URL url = new URL("http://localhost:%d".formatted(server.getPort()));
		IJuiceInliner inliner = JuiceInliner.builder().inlinerUrl(url).build();
		
		server
			.when(request().withPath("/"))
			.respond(response().withStatusCode(200).withBody("html"));
		Assertions.assertThat(inliner.postProcessJuice("html", "css")).isEqualTo("html");
		server
			.verify(request()
					.withPath("/")
					.withMethod("GET")
					.withContentType(MediaType.APPLICATION_JSON)
					.withBody(JsonBody.json("""
					{
						"content": "html",
						"options": {
							"extraCss": "css",
							"removeStyleTags": false
						}
					}""")), VerificationTimes.once());
	}

	/**
	 * Test a call with retry and success before maxAttempts
	 */
	@Test
	void testRetry_success() throws MalformedURLException {
		URL url = new URL("http://localhost:%d".formatted(server.getPort()));
		IJuiceInliner inliner = JuiceInliner.builder().retryMaxAttempts(2).inlinerUrl(url).build();
		server
			.when(request().withPath("/"), Times.once())
			.respond(response().withStatusCode(500));
		server
			.when(request().withPath("/"), Times.once())
			.respond(response().withStatusCode(200));
		
		inliner.postProcessJuice("html", "css");
		
		server
			.verify(request().withPath("/"), VerificationTimes.exactly(2));
	}

	/**
	 * Test a call with failure. Ensure maxAttempts is honored and exception triggered
	 */
	@Test
	void testRetry_failure() throws MalformedURLException {
		URL url = new URL("http://localhost:%d".formatted(server.getPort()));
		IJuiceInliner inliner = JuiceInliner.builder().inlinerUrl(url).retryMaxAttempts(2).build();
		server
			.when(request().withPath("/"), Times.once())
			.respond(response().withStatusCode(500));
		server
			.when(request().withPath("/"), Times.once())
			.respond(response().withStatusCode(500));
		server
			.when(request().withPath("/"), Times.once())
			.respond(response().withStatusCode(200));
		
		Assertions.assertThatCode(() -> inliner.postProcessJuice("html", "css")).isInstanceOf(IllegalStateException.class);
		
		server
			.verify(request().withPath("/"), VerificationTimes.exactly(2));
	}

	/**
	 * Test a call without authentication (no bearer header)
	 */
	@Test
	void testAuthentication_empty() throws MalformedURLException {
		URL url = new URL("http://localhost:%d".formatted(server.getPort()));
		IJuiceInliner inliner = JuiceInliner.builder().inlinerUrl(url).build();
		server
			.when(request().withPath("/"), Times.once())
			.respond(response().withStatusCode(200));
		inliner.postProcessJuice("html", "css");
		server
			.verify(request().withPath("/").withHeader("Authorization"), VerificationTimes.never());
	}

	/**
	 * Test a call with authentication (bearer token is sent)
	 */
	@Test
	void testAuthentication_notEmpty() throws MalformedURLException {
		URL url = new URL("http://localhost:%d".formatted(server.getPort()));
		String token = "toto";
		IJuiceInliner inliner = JuiceInliner.builder().inlinerUrl(url).token(token).build();
		server
			.when(request().withPath("/"), Times.once())
			.respond(response().withStatusCode(200));
		inliner.postProcessJuice("html", "css");
		server
			.verify(request().withPath("/")
					.withHeader("Authorization", "Bearer %s".formatted(token)), VerificationTimes.once());
	}

	/**
	 * Test a call with a delay on service. Call eventually fails.
	 */
	@Test
	void testTimeout_honored_callFailed() throws MalformedURLException {
		URL url = new URL("http://localhost:%d".formatted(server.getPort()));
		String token = "toto";
		IJuiceInliner inliner = JuiceInliner.builder()
			.inlinerUrl(url)
			.retryMaxAttempts(5)
			// keep a 200ms delay
			.requestTimeout(Duration.ofMillis(1200)).build();
		server
			.when(request().withPath("/"))
			.respond(response().withDelay(Delay.seconds(1)).withStatusCode(200));
		Assertions.assertThatCode(() -> inliner.postProcessJuice("html", "css")).doesNotThrowAnyException();
		
		server
			.verify(request().withPath("/"), VerificationTimes.once());
	}

	/**
	 * Test a call with a delay on service, but requestTimeout high enough to ensure call success.
	 */
	@Test
	void testTimeout_honored_callSuccess() throws MalformedURLException {
		URL url = new URL("http://localhost:%d".formatted(server.getPort()));
		String token = "toto";
		IJuiceInliner inliner = JuiceInliner.builder()
			.inlinerUrl(url)
			.retryMaxAttempts(5)
			.requestTimeout(Duration.ofMillis(500)).build();
		server
			.when(request().withPath("/"))
			.respond(response().withDelay(Delay.seconds(1)).withStatusCode(200));
		Assertions.assertThatCode(() -> inliner.postProcessJuice("html", "css")).isInstanceOf(IllegalStateException.class);
		
		server
			.verify(request().withPath("/"), VerificationTimes.exactly(5));
	}
}

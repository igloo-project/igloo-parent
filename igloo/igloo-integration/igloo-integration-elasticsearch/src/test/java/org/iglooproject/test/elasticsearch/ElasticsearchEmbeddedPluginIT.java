package org.iglooproject.test.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.StringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.iglooproject.lucene.analysis.french.CoreFrenchMinimalStemFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;

public class ElasticsearchEmbeddedPluginIT {

	private static final String GET_METHOD = "GET";

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private int httpPort = 9000;
	private int tcpPort = 9001;

	private EmbeddedElastic runner;

	private boolean inhibited;

	@BeforeEach
	public void setUpElastic() throws Exception {
		FileBasedConfiguration configuration;
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
					new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
			builder.configure(params.properties().setFileName("configuration.properties"));
			configuration = builder.getConfiguration();
		}
		
		// populated by maven
		String pluginPath = configuration.getString("elasticsearch.plugin");
		
		// ignore test in eclipse (resources cannot be filtered with artifact path)
		if (pluginPath.startsWith("$")) {
			inhibited = true;
			return;
		} else {
			inhibited = false;
		}
		
		String pluginUrl = String.format("file://%s", configuration.getString("elasticsearch.plugin"));
		String version = configuration.getString("elasticsearch.version");
		
		runner = ElasticsearchBootstrapHelper.initializeEmbeddedElastic(
				version,
				httpPort, tcpPort,
				"default", 120, TimeUnit.SECONDS,
				Collections.singletonList(pluginUrl));
	}

	@AfterEach
	public void stopElastic() throws IOException {
		if (runner != null) {
			runner.stop();
		}
	}

	// TODO LAL: commented out as this test write to stdout, and this is forbidden by surefire plugin if fork is used
	//@Test
	public void plugin() throws ConfigurationException, IOException, URISyntaxException {
		assumeThat(inhibited).isFalse().as("No plugin artifact available; ignoring test.");
		
		HttpHost host = new HttpHost("localhost", httpPort);
		
		try (RestClient client = RestClient.builder(host).build()) {
			Map<String, String> params = new HashMap<>();
			params.put("pretty", Boolean.TRUE.toString());
			
			Map<String, Object> content = new HashMap<>();
			content.put("tokenizer", "whitespace");
			content.put("filter", Collections.singletonList(CoreFrenchMinimalStemFilter.STEMMER_NAME));
			content.put("text", "chapeaux");
			HttpEntity entity = new StringEntity(OBJECT_MAPPER.writeValueAsString(content));
			Response response = client.performRequest(GET_METHOD, "_analyze", params, entity);
			try (InputStream is = response.getEntity().getContent()) {
				JavaType type = OBJECT_MAPPER.getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class);
				Map<String, Object> value = OBJECT_MAPPER.readValue(is, type);
				
				// beware of double-wrapped list in tokens
				// { "tokens": [[ { "token" : "chapeau", ... }]] }
				assertThat(value.get("tokens"))
					// attribute tokens is a list of 1 item that is a map
					.asList()
					.hasSize(1)
					.first()
					.isInstanceOf(Map.class);
				
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> tokens = (List<Map<String, Object>>) value.get("tokens");
				Map<String, Object> token = tokens.get(0);
				assertThat(token.get("token")).isEqualTo("chapeau");
			}
		}
	}

}

package org.iglooproject.test.elasticsearch;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
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
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;

public class ElasticsearchCustomPluginIT {

	private static final String GET_METHOD = "GET";

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Test
	public void plugin() throws ConfigurationException, IOException, URISyntaxException {
		int httpPort = 9000;
		int tcpPort = 9001;
		
		FileBasedConfiguration configuration;
		{
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
					new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class);
			builder.configure(params.properties().setFileName("configuration.properties"));
			configuration = builder.getConfiguration();
		}
		
		URI baseURI = URI.create(String.format("http://localhost:%d", httpPort));
		HttpHost host = new HttpHost("localhost", httpPort);
		
		// populated by maven
		String pluginPath = String.format("file://%s", configuration.getString("elasticsearch.plugin"));
		String version = configuration.getString("elasticsearch.version");
		System.out.println(pluginPath);
		EmbeddedElastic embedded =
				ElasticsearchBootstrapHelper.initializeEmbeddedElastic(
						version,
						httpPort, tcpPort,
						"default", 120, TimeUnit.SECONDS,
						Collections.singletonList(pluginPath));
		
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

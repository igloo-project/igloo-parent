package org.iglooproject.test.jpa.elasticsearch;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

public final class ElasticsearchHelper {

	public static EmbeddedElastic initializeEmbeddedElastic(String elasticVersion,
			Integer httpPort, Integer transportTcpPort,
			String clusterName, Integer startTimeout) {
		try {
			return EmbeddedElastic.builder()
					.withElasticVersion(elasticVersion)
					.withSetting(PopularProperties.HTTP_PORT, httpPort)
					.withSetting(PopularProperties.TRANSPORT_TCP_PORT, transportTcpPort)
					.withSetting(PopularProperties.CLUSTER_NAME, clusterName)
					.withStartTimeout(startTimeout, TimeUnit.SECONDS)
					.build()
					.start();
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException("Fail to start embedded elasticsearch", e);
		}
	}

}

package org.iglooproject.test.elasticsearch;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic.Builder;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

public final class ElasticsearchBootstrapHelper {

	public static EmbeddedElastic initializeEmbeddedElastic(String elasticVersion,
			Integer httpPort, Integer transportTcpPort,
			String clusterName, Integer startTimeout, TimeUnit startUnit) {
		return initializeEmbeddedElastic(elasticVersion, httpPort, transportTcpPort,
				clusterName, startTimeout, startUnit, null);
	}

	public static EmbeddedElastic initializeEmbeddedElastic(String elasticVersion,
			Integer httpPort, Integer transportTcpPort,
			String clusterName, Integer startTimeout, TimeUnit startUnit,
			Collection<String> plugins) {
		try {
			Builder builder = EmbeddedElastic.builder()
					.withElasticVersion(elasticVersion)
					.withSetting(PopularProperties.HTTP_PORT, httpPort)
					.withSetting(PopularProperties.TRANSPORT_TCP_PORT, transportTcpPort)
					.withSetting(PopularProperties.CLUSTER_NAME, clusterName)
					.withStartTimeout(startTimeout, startUnit);
			if (plugins != null) {
				plugins.stream().forEach(builder::withPlugin);
			}
			return builder.build().start();
		} catch (IOException | InterruptedException e) {
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			throw new IllegalStateException("Fail to start embedded elasticsearch", e);
		}
	}

	private ElasticsearchBootstrapHelper() {
	}

}

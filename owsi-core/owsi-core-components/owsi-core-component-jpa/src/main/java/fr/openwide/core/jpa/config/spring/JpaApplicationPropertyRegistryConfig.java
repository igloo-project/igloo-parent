package fr.openwide.core.jpa.config.spring;

import static fr.openwide.core.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_ELASTICSEARCH_ENABLED;
import static fr.openwide.core.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_ELASTICSEARCH_HOST;
import static fr.openwide.core.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE;
import static fr.openwide.core.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS;
import static fr.openwide.core.jpa.property.JpaPropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;

import org.apache.lucene.search.BooleanQuery;
import org.springframework.context.annotation.Configuration;

import com.google.common.primitives.Ints;

import fr.openwide.core.commons.util.functional.SerializableSupplier;
import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Configuration
public class JpaApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	protected void register(IPropertyRegistry registry) {
		registry.register(
				LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT,
				Ints.stringConverter(),
				new SerializableSupplier<Integer>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Integer get() {
						return BooleanQuery.getMaxClauseCount();
					}
				}
		);
		
		registry.registerInteger(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE, 25);
		registry.registerInteger(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS, 8);
		registry.registerBoolean(HIBERNATE_SEARCH_ELASTICSEARCH_ENABLED, false);
		registry.registerString(HIBERNATE_SEARCH_ELASTICSEARCH_HOST, "http://127.0.0.1:9220");
	}

}

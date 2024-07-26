package org.iglooproject.jpa.config.spring;

import static org.iglooproject.jpa.property.JpaPropertyIds.DB_DATASOURCE_PROVIDER;
import static org.iglooproject.jpa.property.JpaPropertyIds.DB_JNDI_NAME;
import static org.iglooproject.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_ELASTICSEARCH_ENABLED;
import static org.iglooproject.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_ELASTICSEARCH_HOST;
import static org.iglooproject.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE;
import static org.iglooproject.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS;
import static org.iglooproject.jpa.property.JpaPropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;

import com.google.common.primitives.Ints;
import org.apache.lucene.search.BooleanQuery;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.config.spring.provider.DatasourceProvider;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaApplicationPropertyRegistryConfig
    extends AbstractApplicationPropertyRegistryConfig {

  @Override
  public void register(IPropertyRegistry registry) {
    registry.registerEnum(DB_DATASOURCE_PROVIDER, DatasourceProvider.class);
    registry.registerString(DB_JNDI_NAME);

    registry.register(
        LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT,
        Functions2.from(Ints.stringConverter()),
        (Supplier2<? extends Integer>) () -> BooleanQuery.getMaxClauseCount());

    registry.registerInteger(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE, 25);
    registry.registerInteger(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS, 8);
    registry.registerBoolean(HIBERNATE_SEARCH_ELASTICSEARCH_ENABLED, false);
    registry.registerString(HIBERNATE_SEARCH_ELASTICSEARCH_HOST, "http://" + "127.0.0.1" + ":9220");
  }
}

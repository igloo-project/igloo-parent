package org.iglooproject.jpa.config.spring;

import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_ELASTICSEARCH_ENABLED;
import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_ELASTICSEARCH_HOST;
import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE;
import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS;
import static org.iglooproject.jpa.property.JpaSearchPropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;

import org.apache.lucene.search.BooleanQuery;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.property.JpaSearchPropertyIds;
import org.iglooproject.jpa.search.dao.HibernateSearchDaoImpl;
import org.iglooproject.jpa.search.dao.IHibernateSearchDao;
import org.iglooproject.jpa.search.service.HibernateSearchServiceImpl;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.primitives.Ints;

/**
 * <p>Configuration Hibernate search Igloo helpers. This configuration implies:</p>
 * 
 * <ul>
 * <li>A running Hibernate + Hibernate search environment (Spring Boot JPA Repository with
 * <code>spring.jpa.properties.hibernate.search.enabled!=false</code>.</li>
 * <li>{@link IglooJpaConfiguration} helpers ({@link IEntityService}): use {@link IglooJpaConfiguration}.</li>
 * <li>{@link IPropertyService} and {@link JpaSearchPropertyIds} for reindexation configuration behaviors:
 * use {@link JpaSearchPropertyRegistryConfig}.</li>
 * </ul>
 * 
 * <p>These helpers implies to use {@link GenericEntity} base class.</p>
 */
@Configuration
public class IglooHibernateSearchConfiguration {
	@Bean
	public IHibernateSearchService hibernateSearchService() {
		return new HibernateSearchServiceImpl();
	}
	@Bean
	public IHibernateSearchDao hibernateSearchDao() {
		return new HibernateSearchDaoImpl();
	}
	@Configuration
	public static class JpaSearchPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {
		@Override
		public void register(IPropertyRegistry registry) {
			registry.register(
				LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT,
				Functions2.from(Ints.stringConverter()),
				(Supplier2<? extends Integer>) () -> BooleanQuery.getMaxClauseCount()
			);
			
			registry.registerInteger(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE, 25);
			registry.registerInteger(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS, 8);
			registry.registerBoolean(HIBERNATE_SEARCH_ELASTICSEARCH_ENABLED, false);
			registry.registerString(HIBERNATE_SEARCH_ELASTICSEARCH_HOST, "http://" + "127.0.0.1" + ":9220");
		}
	}
}

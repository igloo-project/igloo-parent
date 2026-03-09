package org.iglooproject.jpa.autoconfigure;

import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE;
import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS;
import static org.iglooproject.jpa.property.JpaSearchPropertyIds.LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT;

import com.google.common.primitives.Ints;
import org.apache.lucene.search.IndexSearcher;
import org.hibernate.search.mapper.orm.Search;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.property.JpaSearchPropertyIds;
import org.iglooproject.jpa.search.dao.HibernateSearchDaoImpl;
import org.iglooproject.jpa.search.dao.IHibernateSearchDao;
import org.iglooproject.jpa.search.service.HibernateSearchServiceImpl;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * Configuration Hibernate search Igloo helpers. This configuration implies:
 *
 * <ul>
 *   <li>A running Hibernate + Hibernate search environment (Spring Boot JPA Repository with <code>
 *       spring.jpa.properties.hibernate.search.enabled!=false</code>.
 *   <li>{@link JpaAutoConfiguration} helpers ({@link IEntityService}): use {@link
 *       JpaAutoConfiguration}.
 *   <li>{@link IPropertyService} and {@link JpaSearchPropertyIds} for reindexation configuration
 *       behaviors: use {@link JpaSearchPropertyRegistryConfig}.
 * </ul>
 *
 * <p>These helpers implies to use {@link GenericEntity} base class.
 */
@AutoConfiguration(after = JpaAutoConfiguration.class)
@ConditionalOnClass({Search.class, LocalContainerEntityManagerFactoryBean.class})
@ConditionalOnProperty(
    name = "spring.jpa.properties.hibernate.search.enabled",
    matchIfMissing = true,
    havingValue = "true")
public class HibernateSearchAutoConfiguration {
  @Bean
  public IHibernateSearchService hibernateSearchService() {
    return new HibernateSearchServiceImpl();
  }

  @Bean
  public IHibernateSearchDao hibernateSearchDao() {
    return new HibernateSearchDaoImpl();
  }

  @Configuration
  public static class JpaSearchPropertyRegistryConfig implements IPropertyRegistryConfig {
    @Override
    public void register(IPropertyRegistry registry) {
      registry.register(
          LUCENE_BOOLEAN_QUERY_MAX_CLAUSE_COUNT,
          Functions2.from(Ints.stringConverter()),
          (Supplier2<? extends Integer>) IndexSearcher::getMaxClauseCount);

      registry.registerInteger(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE, 25);
      registry.registerInteger(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS, 8);
    }
  }
}

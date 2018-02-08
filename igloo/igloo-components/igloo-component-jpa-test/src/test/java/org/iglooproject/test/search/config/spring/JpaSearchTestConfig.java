package org.iglooproject.test.search.config.spring;

import org.iglooproject.jpa.hibernate.analyzers.LuceneEmbeddedAnalyzerRegistry;
import org.iglooproject.jpa.hibernate.analyzers.LuceneEmbeddedFromElasticsearchAnalyzerRegistry;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.spring.config.spring.annotation.ApplicationDescription;
import org.iglooproject.spring.config.spring.annotation.ConfigurationLocations;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.config.spring.JpaTestApplicationPropertyConfig;
import org.iglooproject.test.search.JpaTestSearchPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ApplicationDescription(name = "jpa-test")
@ConfigurationLocations(locations = {
		"classpath:igloo-component-jpa.properties",
		"classpath:configuration-private.properties",
		ConfigurationPropertiesUrlConstants.JPA_COMMON,
		ConfigurationPropertiesUrlConstants.JPA_SEARCH_LUCENE_COMMON,
		"classpath:igloo-jpa.properties",
		"classpath:igloo-jpa-search.properties"
})
@Import({
	JpaSearchJpaTestConfig.class,
	JpaTestApplicationPropertyConfig.class
})
@ComponentScan(
	basePackageClasses = JpaTestSearchPackage.class,
	excludeFilters = @Filter(Configuration.class)
)
public class JpaSearchTestConfig extends AbstractApplicationConfig {
	
	@Bean
	LuceneEmbeddedAnalyzerRegistry luceneEmbedddedAnalyzerRegistry() {
		return new LuceneEmbeddedFromElasticsearchAnalyzerRegistry();
	}

}

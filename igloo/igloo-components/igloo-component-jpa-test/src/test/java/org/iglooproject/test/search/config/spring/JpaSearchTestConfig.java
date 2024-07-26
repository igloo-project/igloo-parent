package org.iglooproject.test.search.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.config.spring.JpaTestApplicationPropertyConfig;
import org.iglooproject.test.search.JpaTestSearchPackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.APPLICATION,
    value = {
      ConfigurationPropertiesUrlConstants.JPA_COMMON,
      ConfigurationPropertiesUrlConstants.JPA_SEARCH_LUCENE_COMMON,
      "classpath:igloo-jpa.properties",
      "classpath:igloo-jpa-search.properties",
    },
    encoding = "UTF-8")
@Import({JpaSearchJpaTestConfig.class, JpaTestApplicationPropertyConfig.class})
@ComponentScan(
    basePackageClasses = JpaTestSearchPackage.class,
    excludeFilters = @Filter(Configuration.class))
public class JpaSearchTestConfig extends AbstractApplicationConfig {}

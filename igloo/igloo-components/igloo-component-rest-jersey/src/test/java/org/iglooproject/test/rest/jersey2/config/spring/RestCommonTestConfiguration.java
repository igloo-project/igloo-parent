package org.iglooproject.test.rest.jersey2.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.test.config.spring.ConfigurationPropertiesUrlConstants;
import org.iglooproject.test.jpa.junit.JpaOnlyTestConfiguration;
import org.iglooproject.test.rest.jersey2.business.RestTestBusinessPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@Import({JpaOnlyTestConfiguration.class})
@EntityScan(basePackageClasses = RestTestBusinessPackage.class)
@ComponentScan(basePackageClasses = {RestTestBusinessPackage.class})
@PropertySource(
    name = IglooPropertySourcePriority.APPLICATION,
    value = {
      ConfigurationPropertiesUrlConstants.JPA_COMMON,
      ConfigurationPropertiesUrlConstants.JERSEY_MOCK_COMMON,
      "classpath:rest-common.properties"
    },
    encoding = "UTF-8")
@EnableTransactionManagement
public class RestCommonTestConfiguration {}

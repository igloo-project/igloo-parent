package org.iglooproject.test.jpa.junit;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.config.spring.IglooHibernateSearchConfiguration;
import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({
	ApplicationPropertyTestConfiguration.class,
	IglooHibernateSearchConfiguration.class
})
@PropertySource(
		name = IglooPropertySourcePriority.APPLICATION,
		value = "classpath:igloo-component-jpa-test/jpa-search.properties",
		encoding = "UTF-8"
	)
public class JpaSearchTestConfiguration extends AbstractApplicationConfig {

}

package org.iglooproject.test.spring.notification.spring.config;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.spring.config.CorePropertyPlaceholderConfigurer;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.config.spring.SpringApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.dao.IImmutablePropertyDao;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.dao.ImmutablePropertyDaoImpl;
import org.iglooproject.spring.property.dao.StubMutablePropertyDao;
import org.iglooproject.spring.property.service.IConfigurablePropertyService;
import org.iglooproject.spring.property.service.PropertyServiceImpl;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
	name = IglooPropertySourcePriority.APPLICATION,
	value = "classpath:notification-test.properties",
	encoding = "UTF-8"
)
@Import({ NotificationTestConfig.class })
public class TestConfig {

	@Bean
	public static CorePropertyPlaceholderConfigurer environment(ConfigurableApplicationContext context) {
		return new CorePropertyPlaceholderConfigurer();
	}
	
	@Bean
	public IMutablePropertyDao mutablePropertyDao() {
		return new StubMutablePropertyDao();
	}

	@Bean
	public IImmutablePropertyDao immutablePropertyDao() {
		return new ImmutablePropertyDaoImpl();
	}

	@Bean
	public IConfigurablePropertyService propertyService() {
		return new PropertyServiceImpl();
	}

	@Bean
	public IPropertyRegistryConfig propertyRegistryConfig() {
		return new SpringApplicationPropertyRegistryConfig();
	}

}

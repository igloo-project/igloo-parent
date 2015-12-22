package fr.openwide.core.test.spring.notification.spring.config;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import fr.openwide.core.spring.config.CoreConfigurer;
import fr.openwide.core.spring.config.CorePropertyPlaceholderConfigurer;
import fr.openwide.core.spring.config.spring.annotation.ApplicationConfigurerBeanFactoryPostProcessor;
import fr.openwide.core.spring.config.spring.annotation.ApplicationDescription;
import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;
import fr.openwide.core.spring.property.dao.IImmutablePropertyDao;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.dao.ImmutablePropertyDaoImpl;
import fr.openwide.core.spring.property.dao.StubMutablePropertyDao;
import fr.openwide.core.spring.property.service.IConfigurablePropertyService;
import fr.openwide.core.spring.property.service.PropertyServiceImpl;

@Configuration
@ApplicationDescription(name = "TestNotification")
@ConfigurationLocations(locations = {"classpath:notification-test.properties", "classpath:notification-test-${user}.properties"})
@Import({ NotificationTestConfig.class })
public class TestConfig {

	@Bean(name = { "configurer" })
	public CoreConfigurer configurer() {
		return new CoreConfigurer();
	}
	
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
	public static ApplicationConfigurerBeanFactoryPostProcessor applicationConfigurer() {
		return new ApplicationConfigurerBeanFactoryPostProcessor();
	}
	
}

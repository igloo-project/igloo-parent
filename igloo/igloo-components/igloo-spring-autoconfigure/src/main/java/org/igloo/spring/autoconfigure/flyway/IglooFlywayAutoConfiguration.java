package org.igloo.spring.autoconfigure.flyway;


import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.flyway.FlywayInitializer;
import org.iglooproject.flyway.IFlywayConfiguration;
import org.iglooproject.jpa.config.spring.FlywayPropertyRegistryConfig;
import org.iglooproject.jpa.more.config.util.FlywayConfiguration;
import org.iglooproject.jpa.property.FlywayPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConditionalOnProperty(name = "igloo-ac.flyway.disabled", havingValue = "false", matchIfMissing = true)
@ConditionalOnClass(Flyway.class)
@AutoConfigureAfter(IglooPropertyAutoConfiguration.class)
@PropertySource(
	name = IglooPropertySourcePriority.COMPONENT,
	value = "classpath:/configuration/flyway-common.properties"
)
@Import({ FlywayPropertyRegistryConfig.class })
public class IglooFlywayAutoConfiguration {

	@Bean(initMethod = "migrate", value = { "flyway", "databaseInitialization" })
	public Flyway flyway(DataSource dataSource, IFlywayConfiguration flywayConfiguration,
			IPropertyService propertyService, ConfigurableApplicationContext applicationContext) {
		Map<String, String> placeholders = new HashMap<>();
		for (String property : propertyService.get(FlywayPropertyIds.FLYWAY_PLACEHOLDERS_PROPERTIES)) {
			placeholders.put(property, propertyService.get(FlywayPropertyIds.property(property)));
		}
		return FlywayInitializer.flyway(
				dataSource,
				flywayConfiguration,
				placeholders,
				applicationContext.getAutowireCapableBeanFactory()::autowireBean);
	}

	@Bean
	public IFlywayConfiguration flywayConfiguration() {
		return new FlywayConfiguration();
	}

}

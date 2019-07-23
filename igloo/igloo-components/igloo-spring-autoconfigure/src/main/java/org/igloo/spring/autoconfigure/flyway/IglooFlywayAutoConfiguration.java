package org.igloo.spring.autoconfigure.flyway;


import java.util.Arrays;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.internal.scanner.Scanner;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.config.spring.FlywayPropertyRegistryConfig;
import org.iglooproject.jpa.migration.IglooMigrationResolver;
import org.iglooproject.jpa.more.config.util.FlywayConfiguration;
import org.iglooproject.jpa.property.FlywayPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.google.common.collect.Maps;

@Configuration
@ConditionalOnClass(Flyway.class)
@Import({ FlywayPropertyRegistryConfig.class })
@AutoConfigureAfter(IglooPropertyAutoConfiguration.class)
@PropertySource(
	name = IglooPropertySourcePriority.COMPONENT,
	value = "classpath:/configuration/flyway-common.properties"
)
public class IglooFlywayAutoConfiguration {

	@Bean(initMethod = "migrate", value = { "flyway", "databaseInitialization" })
	public Flyway flyway(DataSource dataSource, FlywayConfiguration flywayConfiguration,
			IPropertyService propertyService, ConfigurableApplicationContext applicationContext) {
		FluentConfiguration configuration = Flyway.configure()
			.dataSource(dataSource)
			.schemas(flywayConfiguration.getSchemas())
			.table(flywayConfiguration.getTable())
			.locations(StringUtils.split(flywayConfiguration.getLocations(), ","))
			.baselineOnMigrate(true)
			// difficult to handle this case for the moment; we ignore mismatching checksums
			// TODO allow developers to handle mismatches during their tests.
			.validateOnMigrate(false);
		
		// Placeholders
		Map<String, String> placeholders = Maps.newHashMap();
		for (String property : propertyService.get(FlywayPropertyIds.FLYWAY_PLACEHOLDERS_PROPERTIES)) {
			placeholders.put(property, propertyService.get(FlywayPropertyIds.property(property)));
		}
		configuration.placeholderReplacement(true);
		configuration.placeholders(placeholders);
		
		// Custom Spring-autowiring migration resolver
		Scanner scanner = new Scanner(
			Arrays.asList(configuration.getLocations()),
			configuration.getClassLoader(),
			configuration.getEncoding()
		);
		
		configuration.resolvers(new IglooMigrationResolver(scanner, configuration, applicationContext));
		
		return configuration.load();
	}

	@Bean
	public FlywayConfiguration flywayConfiguration() {
		return new FlywayConfiguration();
	}

}
package org.igloo.spring.autoconfigure.flyway;


import java.util.Arrays;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.internal.scanner.LocationScannerCache;
import org.flywaydb.core.internal.scanner.ResourceNameCache;
import org.flywaydb.core.internal.scanner.Scanner;
import org.igloo.spring.autoconfigure.property.IglooPropertyAutoConfiguration;
import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.iglooproject.jpa.config.spring.FlywayPropertyRegistryConfig;
import org.iglooproject.jpa.migration.IIglooMigration;
import org.iglooproject.jpa.migration.IglooMigrationResolver;
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

import com.google.common.collect.Maps;

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
		Scanner<IIglooMigration> scanner = new Scanner<>(
				IIglooMigration.class,
				Arrays.asList(configuration.getLocations()),
				configuration.getClassLoader(),
				configuration.getEncoding(),
				configuration.getDetectEncoding(),
				//this boolean has been added since flyway 7.0.0 and is unused in flyway 7.0.2, be carefull when updating flyway
				false,
				new ResourceNameCache(),
				new LocationScannerCache(),
				configuration.getFailOnMissingLocations()
			);
		
		configuration.resolvers(new IglooMigrationResolver(scanner, configuration, applicationContext));
		
		return configuration.load();
	}

	@Bean
	public FlywayConfiguration flywayConfiguration() {
		return new FlywayConfiguration();
	}

}

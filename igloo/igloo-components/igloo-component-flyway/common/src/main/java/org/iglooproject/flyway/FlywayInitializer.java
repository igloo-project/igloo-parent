package org.iglooproject.flyway;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.internal.scanner.LocationScannerCache;
import org.flywaydb.core.internal.scanner.ResourceNameCache;
import org.flywaydb.core.internal.scanner.Scanner;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

public class FlywayInitializer {

	private FlywayInitializer() {}
	
	public static Flyway flyway(DataSource dataSource, IFlywayConfiguration flywayConfiguration,
			Map<String, String> extraProperties, Consumer<IIglooMigration> autowirer) {
		IFlywayAdapter adapter = flywayAdapter();
		
		FluentConfiguration configuration = Flyway.configure()
			.dataSource(dataSource)
			.schemas(flywayConfiguration.getSchemas())
			.table(flywayConfiguration.getTable())
			.locations(Splitter.on(",").splitToStream(flywayConfiguration.getLocations()).toArray(String[]::new))
			.baselineOnMigrate(true)
			// difficult to handle this case for the moment; we ignore mismatching checksums
			// TODO allow developers to handle mismatches during their tests.
			.validateOnMigrate(false);
		
		// Placeholders
		Map<String, String> placeholders = Maps.newHashMap();
		placeholders.putAll(extraProperties);
		configuration.placeholderReplacement(true);
		configuration.placeholders(placeholders);
		
		// Custom Spring-autowiring migration resolver
		Scanner<IIglooMigration> scanner = new Scanner<>(
				IIglooMigration.class,
				Arrays.asList(configuration.getLocations()),
				configuration.getClassLoader(),
				configuration.getEncoding(),
				adapter.isDetectEncoding(configuration),
				//this boolean has been added since flyway 7.0.0 and is unused in flyway 7.0.2, be carefull when updating flyway
				false,
				new ResourceNameCache(),
				new LocationScannerCache(),
				adapter.isFailOnMissingLocations(configuration)
			);
		
		configuration.resolvers(new IglooMigrationResolver(scanner, configuration, autowirer));
		
		return configuration.load();
	}

	private static IFlywayAdapter flywayAdapter() {
		List<IFlywayAdapter> adapters = new ArrayList<>();
		ServiceLoader.load(IFlywayAdapter.class).forEach(adapters::add);
		if (adapters.isEmpty()) {
			throw new IllegalStateException("No IFlywayAdapter found, please provide expected org.iglooproject.components:igloo-component-flyway-{7-8} dependency.");
		}
		if (adapters.size() > 1) {
			throw new IllegalStateException("More than one IFlywayAdapter found, please provide only expected org.iglooproject.components:igloo-component-flyway-{7-8} dependency.");
		}
		return adapters.get(0);
	}
}

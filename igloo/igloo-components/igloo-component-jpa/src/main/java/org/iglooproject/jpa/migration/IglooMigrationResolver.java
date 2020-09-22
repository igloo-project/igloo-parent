package org.iglooproject.jpa.migration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flywaydb.core.api.ClassProvider;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.resolver.Context;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.internal.resolver.ResolvedMigrationComparator;
import org.flywaydb.core.internal.util.ClassUtils;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 
 * Specific migration resolver that supports java migrations with Spring dependency injection
 *
 */
public class IglooMigrationResolver implements MigrationResolver {

	private ClassProvider<IIglooMigration> scanner;

	private ConfigurableApplicationContext applicationContext;

	private Configuration configuration;

	public IglooMigrationResolver(ClassProvider<IIglooMigration> scanner, Configuration configuration, ConfigurableApplicationContext applicationContext) {
		this.scanner = scanner;
		this.applicationContext = applicationContext;
		this.configuration = configuration;
	}

	@Override
	public List<ResolvedMigration> resolveMigrations(Context context) {
		List<ResolvedMigration> migrations = new ArrayList<>();
		
		for (Class<?> clazz : scanner.getClasses()) {
			IIglooMigration javaMigration = ClassUtils.instantiate(clazz.getName(), configuration.getClassLoader());
			applicationContext.getAutowireCapableBeanFactory().autowireBean(javaMigration);
			
			ResolvedMigration resolvedMigration = new ResolvedIglooMigration(javaMigration, new IglooMigrationExecutor(javaMigration));
			migrations.add(resolvedMigration);
		}
		
		Collections.sort(migrations, new ResolvedMigrationComparator());
		return migrations;
	}
}


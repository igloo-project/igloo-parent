package org.iglooproject.flyway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.flywaydb.core.api.ClassProvider;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.resolver.Context;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.internal.resolver.ResolvedMigrationComparator;
import org.flywaydb.core.internal.util.ClassUtils;

/**
 * 
 * Specific migration resolver that supports java migrations with Spring dependency injection
 *
 */
public class IglooMigrationResolver implements MigrationResolver {

	private ClassProvider<IIglooMigration> scanner;

	private Consumer<IIglooMigration> autowirer;

	private Configuration configuration;

	public IglooMigrationResolver(ClassProvider<IIglooMigration> scanner, Configuration configuration,
			Consumer<IIglooMigration> autowirer) {
		this.scanner = scanner;
		this.autowirer = autowirer;
		this.configuration = configuration;
	}

	@Override
	public List<ResolvedMigration> resolveMigrations(Context context) {
		List<ResolvedMigration> migrations = new ArrayList<>();
		
		for (Class<?> clazz : scanner.getClasses()) {
			IIglooMigration javaMigration = ClassUtils.instantiate(clazz.getName(), configuration.getClassLoader());
			autowirer.accept(javaMigration);
			
			ResolvedMigration resolvedMigration = new ResolvedIglooMigration(javaMigration, new IglooMigrationExecutor(javaMigration));
			migrations.add(resolvedMigration);
		}
		
		Collections.sort(migrations, new ResolvedMigrationComparator());
		return migrations;
	}
}


package org.iglooproject.jpa.migration;

import java.util.List;

import org.flywaydb.core.api.MigrationType;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.resolver.Context;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.internal.clazz.ClassProvider;
import org.flywaydb.core.internal.resolver.AbstractJavaMigrationResolver;
import org.flywaydb.core.internal.resolver.ResolvedMigrationImpl;
import org.springframework.context.ConfigurableApplicationContext;

public class IglooMigrationResolver extends AbstractJavaMigrationResolver<IIglooMigration, IglooMigrationExecutor> {

	private ConfigurableApplicationContext applicationContext;

	public IglooMigrationResolver(ClassProvider classProvider, Configuration configuration, ConfigurableApplicationContext applicationContext) {
		super(classProvider, configuration);
		this.applicationContext = applicationContext;
	}

	@Override
	public List<ResolvedMigration> resolveMigrations(Context context) {
		List<ResolvedMigration> resolvedMigrations =  super.resolveMigrations(context);
		
		for (ResolvedMigration resolvedMigration : resolvedMigrations) {
			applicationContext.getAutowireCapableBeanFactory().autowireBean(resolvedMigration);
		}
		
		return resolvedMigrations;
	}

	@Override
	protected IglooMigrationExecutor createExecutor(IIglooMigration migration) {
		// Autowire migration bean
		applicationContext.getAutowireCapableBeanFactory().autowireBean(migration);
		
		return new IglooMigrationExecutor(migration);
	}

	@Override
	public ResolvedMigrationImpl extractMigrationInfo(IIglooMigration migration) {
		ResolvedMigrationImpl resolvedMigration = super.extractMigrationInfo(migration);
		resolvedMigration.setChecksum(migration.getChecksum());
		return resolvedMigration;
	}

	@Override
	protected String getMigrationTypeStr() {
		return "JDBC";
	}

	@Override
	protected Class<IIglooMigration> getImplementedInterface() {
		return IIglooMigration.class;
	}

	@Override
	protected MigrationType getMigrationType() {
		return MigrationType.JDBC;
	}
}

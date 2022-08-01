package org.iglooproject.flyway;

import org.flywaydb.core.api.CoreMigrationType;
import org.flywaydb.core.internal.resolver.ResolvedMigrationImpl;
import org.flywaydb.core.internal.util.ClassUtils;

public class ResolvedIglooMigration extends ResolvedMigrationImpl {
	/**
	 * Creates a new ResolvedJavaMigration based on this JavaMigration.
	 *
	 * @param javaMigration The JavaMigration to use.
	 */
	public ResolvedIglooMigration(IIglooMigration iglooMigration, IglooMigrationExecutor migrationExecutor) {
		super(
			iglooMigration.getVersion(),
			iglooMigration.getDescription(),
			iglooMigration.getClass().getName(),
			iglooMigration.getChecksum(),
			iglooMigration.getEquivalentChecksum(),
			CoreMigrationType.JDBC,
			ClassUtils.getLocationOnDisk(iglooMigration.getClass()),
			migrationExecutor
		);
	}
	
}

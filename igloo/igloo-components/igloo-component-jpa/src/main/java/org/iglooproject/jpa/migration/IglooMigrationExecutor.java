package org.iglooproject.jpa.migration;

import java.sql.Connection;
import java.sql.SQLException;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.executor.Context;
import org.flywaydb.core.api.executor.MigrationExecutor;

public class IglooMigrationExecutor implements MigrationExecutor {

	private final IIglooMigration migration;

	public IglooMigrationExecutor(IIglooMigration migration) {
		this.migration = migration;
	}

	@Override
	public void execute(final Context context) throws SQLException {
		try {
			migration.migrate(new org.flywaydb.core.api.migration.Context() {
				@Override
				public Configuration getConfiguration() {
					return context.getConfiguration();
				}
				
				@Override
				public Connection getConnection() {
					return context.getConnection();
				}
			});
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new FlywayException("Migration failed !", e);
		}
	}

	@Override
	public boolean canExecuteInTransaction() {
		return true;
	}
}
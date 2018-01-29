package org.iglooproject.basicapp.core.business.upgrade.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecord;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

public abstract class AbstractDataUpgradeMigration implements SpringJdbcMigration, MigrationChecksumProvider {

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		final Integer id = new Integer(jdbcTemplate.queryForObject(
				"SELECT NEXTVAL('" + DataUpgradeRecord.class.getSimpleName() + "_id_seq');",
				Integer.class
		));
		
		jdbcTemplate.execute(
				"INSERT INTO DataUpgradeRecord (id, name, autoPerform, done) VALUES (?, ?, ?, ?)",
				new PreparedStatementCallback<Boolean>() {
					@Override
					public Boolean doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.setInt(1, id);
						ps.setString(2, getDataUpgradeClass().getSimpleName());
						ps.setBoolean(3, true);
						ps.setBoolean(4, false);
						return ps.execute();
					}
				}
		);
	}

	protected abstract Class<? extends IDataUpgrade> getDataUpgradeClass();

	@Override
	public Integer getChecksum(){
		return getDataUpgradeClass().hashCode() * 23;
	}
}

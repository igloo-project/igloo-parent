package fr.openwide.core.basicapp.core.config.migration;

import static fr.openwide.core.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_PREFIX;
import static fr.openwide.core.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_SUFFIX;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import fr.openwide.core.jpa.more.business.parameter.model.Parameter;

public class V1_1__AddMigrationToParameterTable implements SpringJdbcMigration, MigrationChecksumProvider{

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		final Integer id = new Integer(jdbcTemplate.queryForObject("SELECT NEXTVAL('" + Parameter.class.getSimpleName() + "_id_seq');", Integer.class));
		jdbcTemplate.execute("INSERT INTO parameter (id,datevalue,name,stringvalue) VALUES (?,?,'" + DATA_UPGRADE_AUTOPERFOM_PREFIX + ".MigrationTest." + DATA_UPGRADE_AUTOPERFOM_SUFFIX + "',true);"
				, new PreparedStatementCallback<Boolean>() {
					@Override
					public Boolean doInPreparedStatement(PreparedStatement ps)
							throws SQLException, DataAccessException {
						ps.setInt(1, id);
						ps.setDate(2, new Date(new java.util.Date().getTime()));

						return ps.execute();
					}
				});
	}

	@Override
	public Integer getChecksum(){
		return new Integer(123456789);
	}
}


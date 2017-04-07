package fr.openwide.core.basicapp.core.config.migration;

import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V1_2__AddValueToTable implements SpringJdbcMigration, MigrationChecksumProvider{

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		jdbcTemplate.execute("DELETE FROM test;");
		jdbcTemplate.execute("INSERT INTO test VALUES(4,'Premierevaleur')");
	}
	
	@Override
	public Integer getChecksum(){
		return new Integer(987654321);
	}
}


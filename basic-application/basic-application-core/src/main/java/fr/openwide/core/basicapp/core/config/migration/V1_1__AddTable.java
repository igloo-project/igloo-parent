package fr.openwide.core.basicapp.core.config.migration;

import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V1_1__AddTable implements SpringJdbcMigration, MigrationChecksumProvider{
	
	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS test ( id serial primary key, value varchar(20) NOT NULL)");
	}
	
	@Override
	public Integer getChecksum(){
		return new Integer(123456789);
	}
}


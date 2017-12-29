package org.iglooproject.jpa.config.spring;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.iglooproject.jpa.util.DatabaseInitializationService;

@Configuration
public class DatabaseInitializationConfig {

	@Bean
	public DatabaseInitializationService databaseInitializationService(DataSource dataSource) {
		return new DatabaseInitializationService();
	}

}

package org.iglooproject.jpa.config.spring;

import javax.sql.DataSource;
import org.iglooproject.jpa.util.DatabaseInitializationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInitializationConfig {

  @Bean
  public DatabaseInitializationService databaseInitializationService(DataSource dataSource) {
    return new DatabaseInitializationService();
  }
}

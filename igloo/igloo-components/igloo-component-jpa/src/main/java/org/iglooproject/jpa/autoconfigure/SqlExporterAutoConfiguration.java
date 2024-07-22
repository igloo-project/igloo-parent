package org.iglooproject.jpa.autoconfigure;

import jakarta.persistence.EntityManagerFactory;
import org.igloo.hibernate.hbm.MetadataRegistryIntegrator;
import org.iglooproject.jpa.sql.SqlRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
    name = "spring.jpa.igloo.sql-exporter.enabled",
    matchIfMissing = false,
    havingValue = "true")
public class SqlExporterAutoConfiguration {

  @Bean
  public SqlRunner sqlRunner(
      EntityManagerFactory entityManagerFactory,
      MetadataRegistryIntegrator metadataRegistryIntegrator) {
    return new SqlRunner(entityManagerFactory, metadataRegistryIntegrator);
  }
}

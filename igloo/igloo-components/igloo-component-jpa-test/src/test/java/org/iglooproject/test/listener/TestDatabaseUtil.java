package org.iglooproject.test.listener;

import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.iglooproject.test.jpa.junit.PSQLTestContainerConfiguration;
import org.iglooproject.test.jpa.spring.database.DatabaseCleanerProperties;
import org.iglooproject.test.jpa.spring.database.DatabaseUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/** Test database truncate utils */
public class TestDatabaseUtil {
  @ExtendWith(SpringExtension.class)
  @ImportTestcontainers
  @TestExecutionListeners({
    // enable dependency injection
    DependencyInjectionTestExecutionListener.class,
    SqlScriptsTestExecutionListener.class
  })
  @ContextConfiguration(
      classes = {
        TestcontainersPropertySourceAutoConfiguration.class,
        PSQLTestContainerConfiguration.class,
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
      })
  @EntityScan(basePackageClasses = TestIglooExecutionListener.class)
  @TestPropertySource(
      properties = {
        "testContainer.database.dockerImageName=postgres:alpine",
        "testContainer.database.exposedPorts=5432",
        "testContainer.database.name=test_database",
        "testContainer.database.userName=test_database",
        "testContainer.database.password=test_database",
        "spring.jpa.properties.hibernate.search.backend.directory.type=local-heap"
      })
  static class FlywayLess {
    @Autowired PlatformTransactionManager platformTransactionManager;
    @Autowired DataSource dataSource;

    @Test
    @Sql(
        statements = {
          "create schema custom",
          "create table custom.test (id int)",
          "insert into custom.test (id) values (1)"
        })
    void testClean() {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "custom.test"))
          .isEqualTo(1);
      DatabaseCleanerProperties cleanerProperties = new DatabaseCleanerProperties();
      DatabaseUtil.cleanDatabase(
          new TransactionTemplate(platformTransactionManager), jdbcTemplate, cleanerProperties);
      Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "custom.test")).isZero();
    }
  }
}

package test;

import igloo.test.listener.postgresql.PsqlCleanerProperties;
import igloo.test.listener.postgresql.PsqlIglooTestListener;
import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import igloo.test.listener.postgresql.PsqlUtil;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Test database truncate utils. Beware that {@link PsqlUtil} and {@link PsqlIglooTestListener} are
 * compatible only with postgresql.
 */
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({
  // enable dependency injection
  DependencyInjectionTestExecutionListener.class,
  SqlScriptsTestExecutionListener.class
})
// ServiceConnectionAutoConfiguration + DataSourceAutoConfiguration
// can work together only with a real auto configuration setup
// ImportAutoConfiguration not working as DataSourceAutoConfiguration
// creates JdbcConnectionDetails managed by ServiceConnectionAutoConfiguration
@EnableAutoConfiguration(
    exclude = {
      FlywayAutoConfiguration.class,
    })
@ContextConfiguration(classes = {PsqlTestContainerConfiguration.class})
@EntityScan(basePackageClasses = TestIglooTestExecutionListener.class)
@TestPropertySource(
    properties = {
      "testContainer.database.dockerImageName=postgres:alpine",
      "testContainer.database.exposedPorts=5432",
      "testContainer.database.name=test_database",
      "testContainer.database.userName=test_database",
      "testContainer.database.password=test_database",
      "spring.jpa.properties.hibernate.search.enabled=false",
    })
class TestPsqlUtil {
  @Autowired JdbcConnectionDetails jdbcConnectionDetails;
  @Autowired PlatformTransactionManager platformTransactionManager;
  @Autowired DataSource dataSource;

  /** Tables populated before tests are cleaned */
  @Test
  @Sql(
      statements = {
        "create schema custom",
        "create table custom.test (id int)",
        "insert into custom.test (id) values (1)"
      })
  @Sql(
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD,
      statements = {"drop schema custom cascade"})
  void testClean() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "custom.test")).isEqualTo(1);
    PsqlCleanerProperties cleanerProperties = new PsqlCleanerProperties();
    PsqlUtil.cleanDatabase(
        new TransactionTemplate(platformTransactionManager), jdbcTemplate, cleanerProperties);
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "custom.test")).isZero();
  }

  /** Tables populated before tests but listed in excludes are NOT cleaned */
  @Test
  @Sql(
      statements = {
        "create schema custom",
        "create table custom.excluded (id int)",
        "insert into custom.excluded (id) values (1)"
      })
  @Sql(
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD,
      statements = {"drop schema custom cascade"})
  void testSkipExcluded() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "custom.excluded"))
        .isEqualTo(1);
    PsqlCleanerProperties cleanerProperties = new PsqlCleanerProperties();
    cleanerProperties.setExcludes(new String[] {"*.excluded"});
    PsqlUtil.cleanDatabase(
        new TransactionTemplate(platformTransactionManager), jdbcTemplate, cleanerProperties);
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "custom.excluded"))
        .isEqualTo(1);
  }

  /** Tables populated before tests but in an excluded schema are NOT cleaned */
  @Test
  @Sql(
      statements = {
        "create schema custom",
        "create table custom.test (id int)",
        "insert into custom.test (id) values (1)",
        "create schema excluded",
        "create table excluded.test1 (id int)",
        "insert into excluded.test1 (id) values (1)",
        "create table excluded.test2 (id int)",
        "insert into excluded.test2 (id) values (1)"
      })
  @Sql(
      executionPhase = ExecutionPhase.AFTER_TEST_METHOD,
      statements = {"drop schema custom cascade", "drop schema excluded cascade"})
  void testSkipExcludedSchema() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "custom.test")).isEqualTo(1);
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "excluded.test1"))
        .isEqualTo(1);
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "excluded.test2"))
        .isEqualTo(1);
    PsqlCleanerProperties cleanerProperties = new PsqlCleanerProperties();
    cleanerProperties.setExcludes(new String[] {"excluded.*"});
    PsqlUtil.cleanDatabase(
        new TransactionTemplate(platformTransactionManager), jdbcTemplate, cleanerProperties);
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "custom.test")).isZero();
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "excluded.test1"))
        .isEqualTo(1);
    Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "excluded.test2"))
        .isEqualTo(1);
  }
}

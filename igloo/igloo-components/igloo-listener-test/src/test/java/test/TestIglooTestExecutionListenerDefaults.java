package test;

import igloo.test.listener.IIglooTestListener;
import igloo.test.listener.IglooTestExecutionListener;
import igloo.test.listener.cache.Level2CacheIglooTestListener;
import igloo.test.listener.postgresql.DatabaseCleanerProperties;
import igloo.test.listener.postgresql.DatabaseIglooTestListener;
import igloo.test.listener.postgresql.PSQLTestContainerConfiguration;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

/**
 * Test provided Igloo provided {@link IIglooTestListener} (database, hibernate-search, second-level
 * cache, ...).
 */
class TestIglooTestExecutionListenerDefaults {
  /** Check that jpa without hibernate-search does not invoke index cleaning. */
  @TestPropertySource(properties = {"spring.jpa.properties.hibernate.search.enabled=false"})
  @BaseTest
  @BaseJpaConfiguration
  static class JpaHibernateSearchLess {
    @Autowired EventRecorder eventRecorder;

    @Test
    void testNoClean() {
      Assertions.assertThat(eventRecorder.getEvents())
          .noneMatch(e -> "hibernate-search:clean".equals(e.getSource()));
    }
  }

  /**
   * Check that jpa + second level cache invokes cache cleaning.
   *
   * @see Level2CacheIglooTestListener
   */
  @TestPropertySource(
      properties = {
        "spring.jpa.properties.hibernate.cache.use_second_level_cache=true",
        "spring.jpa.properties.hibernate.cache.region.factory_class=jcache"
      })
  @BaseTest
  @BaseJpaConfiguration
  static class JpaLevel2Cache {
    @Autowired EventRecorder eventRecorder;

    @Test
    void testNoClean() {
      Assertions.assertThat(eventRecorder.getEvents())
          .anyMatch(e -> "hibernate-cache:clean".equals(e.getSource()));
    }
  }

  /** Check that jpa without second level cache does not invoke cache cleaning. */
  @TestPropertySource(
      properties = {"spring.jpa.properties.hibernate.cache.use_second_level_cache=false"})
  @BaseTest
  @BaseJpaConfiguration
  static class JpaLevel2CacheLess {
    @Autowired EventRecorder eventRecorder;

    @Test
    void testNoClean() {
      Assertions.assertThat(eventRecorder.getEvents())
          .noneMatch(e -> "hibernate-cache:clean".equals(e.getSource()));
    }
  }

  /** Test hibernate-search cleaning. */
  @TestPropertySource(properties = {"spring.jpa.properties.hibernate.search.enabled=true"})
  @BaseTest
  @BaseJpaConfiguration
  static class Hsearch {
    @Autowired EventRecorder eventRecorder;
    @Autowired EntityManagerFactory entityManagerFactory;

    @Test
    void testCleanListener() {
      Assertions.assertThat(eventRecorder.getEvents())
          .anyMatch(e -> "hibernate-search:clean".equals(e.getSource()));
    }
  }

  /**
   * testcontainers setup implies an autoconfiguration (see {@link TestDatabaseUtil}.
   *
   * <p>Test a database + flyway + @{@link Sql} setup. All tables are emptied except flyway history
   * table.
   */
  @ExtendWith(SpringExtension.class)
  @TestExecutionListeners({
    // enable dependency injection
    DependencyInjectionTestExecutionListener.class,
    SqlScriptsTestExecutionListener.class,
    IglooTestExecutionListener.class,
  })
  @EnableAutoConfiguration
  @ContextConfiguration(classes = {PSQLTestContainerConfiguration.class, PostgreSQL.Cleaner.class})
  @EntityScan(basePackageClasses = TestIglooTestExecutionListener.class)
  @TestPropertySource(
      properties = {
        "testContainer.database.dockerImageName=postgres:alpine",
        "testContainer.database.exposedPorts=5432",
        "testContainer.database.name=test_database",
        "testContainer.database.userName=test_database",
        "testContainer.database.password=test_database",
        "spring.jpa.properties.hibernate.search.enabled=false",
        "spring.flyway.default-schema=test_database",
        "spring.flyway.locations=flyway-test/",
      })
  static class PostgreSQL {
    @Autowired JdbcTemplate jdbcTemplate;
    @Autowired FlywayProperties flywayProperties;

    @Test
    void testFlyway() {
      Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "test_database.test"))
          .isZero();
    }

    @Configuration
    public static class Cleaner {
      @Bean
      public DatabaseCleanerProperties databaseCleanerProperties() {
        return new DatabaseCleanerProperties();
      }

      @Bean
      public DatabaseIglooTestListener databaseIglooTestListener(
          PlatformTransactionManager platformTransactionManager,
          DataSource dataSource,
          DatabaseCleanerProperties databaseCleanerProperties) {
        return new DatabaseIglooTestListener(
            platformTransactionManager, dataSource, databaseCleanerProperties);
      }
    }
  }
}

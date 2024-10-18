package test;

import igloo.test.listener.IIglooTestListener;
import igloo.test.listener.IglooTestExecutionListener;
import igloo.test.listener.cache.Level2CacheIglooTestListener;
import igloo.test.listener.postgresql.PsqlTestContainerConfiguration;
import jakarta.persistence.EntityManagerFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.jdbc.JdbcTestUtils;
import test.spring.BaseJpaConfiguration;
import test.spring.BaseTest;
import test.spring.EventRecorder;

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
   * testcontainers setup implies an autoconfiguration (see {@link TestPsqlUtil}.
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
  @SpringBootTest(classes = PsqlTestContainerConfiguration.class)
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
        "spring.flyway.table=custom_flyway_table",
        "spring.flyway.locations=flyway-test/",
      })
  static class Psql {
    @Autowired JdbcTemplate jdbcTemplate;
    @Autowired FlywayProperties flywayProperties;

    @Test
    void testFlyway() {
      Assertions.assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "test_database.test"))
          .isZero();
      Assertions.assertThat(
              JdbcTestUtils.countRowsInTable(jdbcTemplate, "test_database.custom_flyway_table"))
          .isEqualTo(2);
    }
  }
}

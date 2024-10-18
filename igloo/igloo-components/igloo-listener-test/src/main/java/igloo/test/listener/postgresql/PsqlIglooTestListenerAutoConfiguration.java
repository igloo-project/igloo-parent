package igloo.test.listener.postgresql;

import igloo.test.listener.IIglooTestListener;
import igloo.test.listener.IglooTestExecutionListener;
import javax.sql.DataSource;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.shaded.org.bouncycastle.util.Arrays;

/**
 * PostgreSQL cleaning by truncate tables script. Can be disabled by {@code
 * igloo.test.listener.psql.enabled=false}. Enabled by default.
 *
 * <p>If flyway is detected, flyway history table is automatically added to table exclusions.
 * Behavior can be skipped with {@code igloo.test.listener.psql.exclude-flyway-table=false}.
 *
 * <p>Any table (or pattern) can be excluded with {@code igloo.test.listener.psql.excludes}. Fully
 * qualified tables names ({@code ...exludes=schema.table1,schema.table2}) or patterns ({@code
 * ...excludes=*.table schema.*}) must be provided. Exclusion is empty by default. Flyway history
 * table may be added.
 *
 * <p>Table matching is case insensitive.
 *
 * <p>If datasource is not a postgresql database (org.postgresql.Driver), cleaning is skipped with
 * warn.
 *
 * <p>{@link IglooTestExecutionListener} must be a registered {@link TestExecutionListener} to
 * invoke this {@link IIglooTestListener}.
 */
@AutoConfiguration(
    after = {
      DataSourceAutoConfiguration.class,
      HibernateJpaAutoConfiguration.class,
      FlywayAutoConfiguration.class
    })
@ConditionalOnProperty(
    prefix = "igloo.test.listener.psql",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@ConditionalOnClass(Driver.class)
@ConditionalOnBean({PlatformTransactionManager.class, DataSource.class})
public class PsqlIglooTestListenerAutoConfiguration {
  public static final Logger LOGGER =
      LoggerFactory.getLogger(PsqlIglooTestListenerAutoConfiguration.class);

  @Bean
  @ConfigurationProperties(prefix = "igloo.test.listener.psql")
  @ConditionalOnMissingBean
  PsqlCleanerProperties psqlCleanerProperties() {
    return new PsqlCleanerProperties();
  }

  @Bean
  @ConditionalOnMissingBean
  PsqlIglooTestListener psqlIglooTestListener(
      ApplicationEventPublisher eventPublisher,
      JdbcConnectionDetails jdbcConnectionDetails,
      PlatformTransactionManager platformTransactionManager,
      DataSource dataSource,
      PsqlCleanerProperties cleanerProperties) {
    return new PsqlIglooTestListener(
        "default",
        eventPublisher,
        jdbcConnectionDetails,
        platformTransactionManager,
        dataSource,
        cleanerProperties);
  }

  @Bean
  @ConditionalOnBean(FlywayProperties.class)
  BeanPostProcessor flywayAwareCleaningPostProcessor(FlywayProperties flywayProperties) {
    return new BeanPostProcessor() {
      @Override
      public Object postProcessAfterInitialization(Object bean, String beanName)
          throws BeansException {
        if (bean instanceof PsqlCleanerProperties psqlCleanerProperties
            && psqlCleanerProperties.isExcludeFlywayTable()) {
          String[] excludes = psqlCleanerProperties.getExcludes();
          String exclude = "*." + flywayProperties.getTable();
          excludes = Arrays.append(excludes, exclude);
          psqlCleanerProperties.setExcludes(excludes);
          LOGGER.debug(
              "Psql - flyway detected, {} added to table exclude patterns (use igloo.test.listener.psql.exclude-flyway-table=false to skip).",
              exclude);
        }
        return bean;
      }
    };
  }
}

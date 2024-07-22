package org.iglooproject.test.config.bootstrap.spring.util;

import org.assertj.core.api.Assertions;
import org.iglooproject.test.config.bootstrap.spring.environment.SpringBootstrapOverrideByEnvironmentTest;
import org.iglooproject.test.config.bootstrap.spring.environment.SpringBootstrapOverrideBySystemPropertyTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

/**
 * Test both Environment variable and system properties style overriding
 *
 * @see SpringBootstrapOverrideByEnvironmentTest
 * @see SpringBootstrapOverrideBySystemPropertyTest
 */
@TestPropertySource(
    inheritProperties = true,
    properties = {
      "testPropertySourceNotOverridenProperty=@TestPropertySource",
      "testPropertySourceAndBootstrapProperty1=@TestPropertySource",
      "testPropertySourceAndBootstrapProperty2=@TestPropertySource"
    })
public abstract class AbstractSpringBoostrapSimpleTest extends AbstractBootstrapTestCase {

  /** Test override precedence. TestPropertySource -&gt; bootstrap file 1 -&gt; bootstrap file 2 */
  @Test
  public void testOverrides() {
    // not overriden
    Assertions.assertThat(
            applicationContext
                .getEnvironment()
                .getProperty("testPropertySourceNotOverridenProperty"))
        .isEqualTo("@TestPropertySource");
    // @TestPropertySource overrides all
    Assertions.assertThat(
            applicationContext
                .getEnvironment()
                .getProperty("testPropertySourceAndBootstrapProperty1"))
        .isEqualTo("@TestPropertySource");
    Assertions.assertThat(
            applicationContext
                .getEnvironment()
                .getProperty("testPropertySourceAndBootstrapProperty2"))
        .isEqualTo("@TestPropertySource");
    // defined in first bootstrap file
    Assertions.assertThat(
            applicationContext.getEnvironment().getProperty("bootstrapOverridenProperty1"))
        .isEqualTo("test-bootstrap.properties");
    // defined in first bootstrap file and overriden in second bootstrap file
    Assertions.assertThat(
            applicationContext.getEnvironment().getProperty("bootstrapOverridenProperty2"))
        .isEqualTo("test-bootstrap-override.properties");
  }
}

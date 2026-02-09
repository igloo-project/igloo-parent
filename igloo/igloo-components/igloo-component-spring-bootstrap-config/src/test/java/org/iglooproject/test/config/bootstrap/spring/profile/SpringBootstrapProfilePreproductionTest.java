package org.iglooproject.test.config.bootstrap.spring.profile;

import org.assertj.core.api.Assertions;
import org.iglooproject.test.config.bootstrap.spring.util.AbstractSpringBoostrapProfileTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

/**
 * Test with default bootstrap configuration (selection with igloo.profile, and switch with
 * recursive resolve in properties)
 *
 * <p>i.e., igloo.configurationLocations=${igloo.${igloo.profile}.configurationLocations}
 *
 * <p><b>user.name<b> is overriden with <em>username<em>
 */
@TestPropertySource(
    inheritProperties = true,
    properties = {
      "igloo.profile=preproduction",
      // needed as default configuration-default-bootstrap.properties references it
      "igloo.applicationName=igloo-component-spring-bootstrap-config"
    })
public class SpringBootstrapProfilePreproductionTest extends AbstractSpringBoostrapProfileTest {

  /**
   * Test override precedence. default -&gt; preproduction
   *
   * <p>configuration-user-username.properties file is <b>not</b> used in preproduction profile
   */
  @Override
  @Test
  public void testOverrides() {
    // this value is not overriden
    Assertions.assertThat(default_).isEqualTo("default");
    Assertions.assertThat(deployment).isEqualTo("deployment");

    // this value is overriden
    Assertions.assertThat(preproduction).isEqualTo("preproduction");

    // these profiles are not loaded !
    Assertions.assertThat(test).isEqualTo("default");
    Assertions.assertThat(development).isEqualTo("default");
    Assertions.assertThat(integration).isEqualTo("default");
    Assertions.assertThat(qualification).isEqualTo("default");
    Assertions.assertThat(production).isEqualTo("default");

    // user configuration is not loaded
    Assertions.assertThat(user).isEqualTo("default");
  }
}

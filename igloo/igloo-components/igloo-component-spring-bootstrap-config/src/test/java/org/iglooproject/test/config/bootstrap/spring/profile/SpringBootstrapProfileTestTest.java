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
    properties = {"igloo.profile=test"})
public class SpringBootstrapProfileTestTest extends AbstractSpringBoostrapProfileTest {

  /**
   * Test override precedence. default -&gt; preproduction
   *
   * <p>User is overriden by a specific configuration-test-user-username.properties file
   */
  @Override
  @Test
  public void testOverrides() {
    // this value is not overriden
    Assertions.assertThat(default_).isEqualTo("default");

    // this value is overriden
    Assertions.assertThat(test).isEqualTo("test");

    // these profiles are not loaded !
    Assertions.assertThat(development).isEqualTo("default");
    Assertions.assertThat(deployment).isEqualTo("default");
    Assertions.assertThat(preproduction).isEqualTo("default");
    Assertions.assertThat(production).isEqualTo("default");

    // this profile is loaded from configuration-test-username-test.properties
    Assertions.assertThat(user).isEqualTo("user-test");
  }
}

package org.iglooproject.test.config.bootstrap.spring.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

/**
 * Test with default bootstrap configuration (selection with igloo.profile, and switch with
 * recursive resolve in properties)
 *
 * <p>i.e., igloo.configurationLocations=${igloo.${igloo.profile}.configurationLocations}
 *
 * @see AbstractBootstrapTestCase
 */
@TestPropertySource(
    inheritProperties = true,
    properties = {"igloo.profile=OVERRIDE", "user.name=username"})
public abstract class AbstractSpringBoostrapProfileTest extends AbstractBootstrapTestCase {

  @Value("${property.default:}")
  protected String default_;

  @Value("${property.deployment:}")
  protected String deployment;

  @Value("${property.test:}")
  protected String test;

  @Value("${property.development:}")
  protected String development;

  @Value("${property.integration:}")
  protected String integration;

  @Value("${property.qualification:}")
  protected String qualification;

  @Value("${property.preproduction:}")
  protected String preproduction;

  @Value("${property.production:}")
  protected String production;

  @Value("${property.profile:}")
  protected String profile;

  @Value("${property.user:}")
  protected String user;

  /**
   * Test override precedence. default -&gt; preproduction
   *
   * <p>User is not used in preproduction profile
   */
  @Test
  public abstract void testOverrides();
}

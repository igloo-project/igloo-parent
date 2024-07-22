package org.iglooproject.test.config.bootstrap.spring.environment;

import org.iglooproject.test.config.bootstrap.spring.util.AbstractSpringBoostrapSimpleTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
    properties = {
      "IGLOO_BOOTSTRAP_LOCATIONS=classpath:test-bootstrap.properties,classpath:test-bootstrap-override.properties",
      "IGLOO_BOOTSTRAP_OVERRIDE_DEFAULT=true"
    })
public class SpringBootstrapOverrideByEnvironmentTest extends AbstractSpringBoostrapSimpleTest {}

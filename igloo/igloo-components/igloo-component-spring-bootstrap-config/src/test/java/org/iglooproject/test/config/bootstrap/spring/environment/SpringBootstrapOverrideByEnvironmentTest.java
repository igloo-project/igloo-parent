package org.iglooproject.test.config.bootstrap.spring.environment;

import org.iglooproject.test.config.bootstrap.spring.util.AbstractSpringBoostrapSimpleTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
    locations = {
      "classpath:test-bootstrap.properties",
      "classpath:test-bootstrap-override.properties"
    })
public class SpringBootstrapOverrideByEnvironmentTest extends AbstractSpringBoostrapSimpleTest {}

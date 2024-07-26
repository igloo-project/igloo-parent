package org.iglooproject.test.config.bootstrap.spring.environment;

import org.iglooproject.test.config.bootstrap.spring.util.AbstractSpringBoostrapSimpleTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
    properties = {
      "igloo.bootstrapLocations=classpath:test-bootstrap.properties,classpath:test-bootstrap-override.properties",
      "igloo.bootstrapOverrideDefault=true"
    })
public class SpringBootstrapOverrideBySystemPropertyTest extends AbstractSpringBoostrapSimpleTest {}

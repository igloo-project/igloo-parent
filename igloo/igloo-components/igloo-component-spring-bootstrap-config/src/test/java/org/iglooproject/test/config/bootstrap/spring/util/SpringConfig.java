package org.iglooproject.test.config.bootstrap.spring.util;

import org.iglooproject.config.bootstrap.spring.annotations.ApplicationDescription;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ApplicationDescription(name = "application-test")
@Import(BootstrapSpringConfig.class)
public class SpringConfig {

}

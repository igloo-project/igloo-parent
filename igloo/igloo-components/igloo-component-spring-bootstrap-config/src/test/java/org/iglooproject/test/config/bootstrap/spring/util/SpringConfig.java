package org.iglooproject.test.config.bootstrap.spring.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(BootstrapSpringConfig.class)
public class SpringConfig {}

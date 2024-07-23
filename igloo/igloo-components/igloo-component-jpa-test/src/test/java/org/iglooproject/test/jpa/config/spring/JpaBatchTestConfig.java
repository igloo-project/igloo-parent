package org.iglooproject.test.jpa.config.spring;

import org.iglooproject.config.bootstrap.spring.annotations.IglooPropertySourcePriority;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/** Added configuration to override some properties */
@Configuration
@PropertySource(
    name = IglooPropertySourcePriority.APPLICATION,
    value = "classpath:/igloo-jpa-batch.properties",
    encoding = "UTF-8")
public class JpaBatchTestConfig {}

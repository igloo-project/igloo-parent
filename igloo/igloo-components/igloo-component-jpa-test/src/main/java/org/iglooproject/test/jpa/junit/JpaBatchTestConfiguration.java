package org.iglooproject.test.jpa.junit;

import org.iglooproject.spring.config.spring.AbstractApplicationConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import igloo.jpa.batch.spring.IglooJpaBatchConfiguration;

@Configuration
@Import(IglooJpaBatchConfiguration.class)
public class JpaBatchTestConfiguration extends AbstractApplicationConfig {

}

package org.iglooproject.test.config.bootstrap.spring.util;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ExtendWith(SpringExtension.class)
public abstract class AbstractTestCase {

  @Autowired protected ConfigurableApplicationContext applicationContext;

  @Autowired protected ConfigurableEnvironment environment;
}

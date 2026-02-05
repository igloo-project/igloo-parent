package org.iglooproject.test.config.bootstrap.spring.util;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Context bootstraping is invoked with {@link
 * org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer} and {@link
 * ApplicationConfigurerBeanFactoryPostProcessor} (added in {@link SpringConfig}).
 *
 * <p><b>user.name<b> is overriden with <em>username<em> by {@link
 * OverrideSystemPropertiesSourceContextInitializer}
 */
@EnableAutoConfiguration
@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(
    initializers = ExtendedApplicationContextInitializer.class,
    classes = SpringConfig.class)
public abstract class AbstractBootstrapTestCase extends AbstractTestCase {}

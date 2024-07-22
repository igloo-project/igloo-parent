package org.iglooproject.test.config.bootstrap.spring.util;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;

/**
 * Context bootstraping is invoked with {@link ExtendedApplicationContextInitializer} and {@link
 * ApplicationConfigurerBeanFactoryPostProcessor} (added in {@link SpringConfig}).
 *
 * <p><b>user.name<b> is overriden with <em>username<em> by {@link
 * OverrideSystemPropertiesSourceContextInitializer}
 */
@ContextConfiguration(
    initializers = ExtendedApplicationContextInitializer.class,
    classes = SpringConfig.class)
public abstract class AbstractBootstrapTestCase extends AbstractTestCase {}

package org.iglooproject.test.config.bootstrap.spring.util;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;

/**
 * <p>Context bootstraping is invoked with {@link ExtendedApplicationContextInitializer} and
 * {@link ApplicationConfigurerBeanFactoryPostProcessor} (added in {@link SpringConfig}).</p>
 * 
 * <p><b>user.name<b> is overriden with <em>username<em> by {@link OverrideSystemPropertiesSourceContextInitializer}</p>
 */
@ContextConfiguration(
	initializers = ExtendedApplicationContextInitializer.class,
	classes = SpringConfig.class
)
public abstract class AbstractBootstrapTestCase extends AbstractTestCase {

}

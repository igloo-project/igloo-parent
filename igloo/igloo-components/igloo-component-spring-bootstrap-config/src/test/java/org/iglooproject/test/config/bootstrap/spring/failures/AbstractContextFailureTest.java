package org.iglooproject.test.config.bootstrap.spring.failures;

import org.iglooproject.config.bootstrap.spring.ExtendedTestApplicationContextInitializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.env.MockPropertySource;

public class AbstractContextFailureTest {

	public AbstractContextFailureTest() {
		super();
	}

	protected void initializeContext(Class<?>... configurationClasses) {
		ExtendedTestApplicationContextInitializer initializer = new ExtendedTestApplicationContextInitializer();
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(configurationClasses);
		applicationContext.getEnvironment().getPropertySources().addFirst(new MockPropertySource().withProperty("igloo.profile", "development"));
		initializer.initialize(applicationContext);
		applicationContext.refresh();
		applicationContext.start();
	}

}
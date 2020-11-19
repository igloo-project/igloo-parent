package org.iglooproject.basicapp.core.cli;

import java.util.Map;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.config.bootstrap.spring.IApplicationContextInitializer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;

import com.google.common.collect.ImmutableMap;

public class AbstractBasicApplicationMain {

	protected AnnotationConfigApplicationContext initializeContext(String iglooProfile, Class<?> configClass) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		Map<String, Object> ps = ImmutableMap.<String, Object>builder().put(
				IApplicationContextInitializer.IGLOO_PROFILE_PROPERTY,
				iglooProfile).build();
		context.getEnvironment().getPropertySources()
				.addFirst(new MapPropertySource("cliIglooProfile", ps));
		new ExtendedApplicationContextInitializer().initialize(context);
		context.register(configClass);
		return context;
	}

	protected AnnotationConfigApplicationContext startContext(String iglooProfile, Class<?> configClass) {
		AnnotationConfigApplicationContext context = initializeContext(iglooProfile, configClass);
		context.refresh();
		context.start();
		return context;
	}

}

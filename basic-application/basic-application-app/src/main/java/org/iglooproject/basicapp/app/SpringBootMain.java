package org.iglooproject.basicapp.app;

import org.iglooproject.basicapp.web.application.config.spring.BasicApplicationWebappConfig;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

// TODO igloo-boot
@SpringBootApplication
@Import(BasicApplicationWebappConfig.class)
public class SpringBootMain extends SpringBootServletInitializer {

	public static void main(String[] args) {
		doConfigure(new SpringApplicationBuilder()).run(args);
	}

	/**
	 * Common configuration.
	 */
	public static SpringApplicationBuilder doConfigure(SpringApplicationBuilder application) {
		return application
			.sources(BasicApplicationWebappConfig.class)
//			.properties("spring.devtools.restart.enabled=false", "spring.devtools.livereload.enabled=false")
			.initializers(new ExtendedApplicationContextInitializer());
	}

}

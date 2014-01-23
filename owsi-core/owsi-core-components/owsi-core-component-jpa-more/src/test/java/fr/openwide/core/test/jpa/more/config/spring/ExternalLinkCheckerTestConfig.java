package fr.openwide.core.test.jpa.more.config.spring;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.spring.config.spring.annotation.ConfigurationLocations;

@Configuration
@ConfigurationLocations(locations = "classpath:external-link-checker-test.properties")
public class ExternalLinkCheckerTestConfig {

}

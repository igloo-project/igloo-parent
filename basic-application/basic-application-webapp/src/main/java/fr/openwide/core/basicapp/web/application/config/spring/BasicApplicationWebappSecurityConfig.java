package fr.openwide.core.basicapp.web.application.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({ "classpath:spring/security-web-context.xml" })
public class BasicApplicationWebappSecurityConfig {

}

package fr.openwide.core.spring.config.spring;

import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_EXPIRATION_DAYS;
import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_HISTORY_COUNT;
import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES;
import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT;
import static fr.openwide.core.spring.property.SpringSecurityPropertyIds.PASSWORD_SALT;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Configuration
public class SpringSecurityApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	protected void register(IPropertyRegistry registry) {
		registry.registerString(PASSWORD_SALT);
		registry.registerInteger(PASSWORD_EXPIRATION_DAYS, 90);
		registry.registerInteger(PASSWORD_HISTORY_COUNT, 4);
		registry.registerInteger(PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT, 50);
		registry.registerInteger(PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES, 15);
	}

}

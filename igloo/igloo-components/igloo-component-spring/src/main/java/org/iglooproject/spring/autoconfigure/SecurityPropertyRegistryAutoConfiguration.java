package org.iglooproject.spring.autoconfigure;

import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_EXPIRATION_DAYS;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_HISTORY_COUNT;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES;
import static org.iglooproject.spring.property.SpringSecurityPropertyIds.PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT;

import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityPropertyRegistryAutoConfiguration implements IPropertyRegistryConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerInteger(PASSWORD_EXPIRATION_DAYS, 90);
		registry.registerInteger(PASSWORD_HISTORY_COUNT, 4);
		registry.registerInteger(PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT, 50);
		registry.registerInteger(PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES, 15);
	}

}

package org.iglooproject.jpa.externallinkchecker.config.spring;

import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.BATCH_SIZE;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.MAX_REDIRECTS;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.MIN_DELAY_BETWEEN_TWO_CHECKS_IN_DAYS;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.RETRY_ATTEMPTS_NUMBER;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.THREAD_POOL_SIZE;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.TIMEOUT;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.USER_AGENT;

import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

import com.google.common.primitives.Ints;

@Configuration
public class JpaExternalLinkCheckerApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerString(USER_AGENT, "Core External Link Checker");
		registry.registerInteger(MAX_REDIRECTS, 5);
		registry.registerInteger(TIMEOUT, 10000);
		registry.registerInteger(RETRY_ATTEMPTS_NUMBER, 4);
		registry.register(
				THREAD_POOL_SIZE,
				Functions2.from(Ints.stringConverter()),
				(Supplier2<? extends Integer>) () -> Runtime.getRuntime().availableProcessors()
		);
		registry.registerInteger(BATCH_SIZE, 500);
		registry.register(
				MIN_DELAY_BETWEEN_TWO_CHECKS_IN_DAYS,
				input -> {
					Integer value = Ints.stringConverter().convert(input);
					if (value == null) {
						return null;
					}
					return value >= 0 ? value : 0;
				},
				2
		);
	}

}

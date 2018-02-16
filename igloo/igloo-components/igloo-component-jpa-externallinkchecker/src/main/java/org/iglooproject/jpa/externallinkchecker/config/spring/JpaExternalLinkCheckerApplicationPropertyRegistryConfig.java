package org.iglooproject.jpa.externallinkchecker.config.spring;

import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.BATCH_SIZE;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.MAX_REDIRECTS;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.MIN_DELAY_BETWEEN_TWO_CHECKS_IN_DAYS;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.RETRY_ATTEMPTS_NUMBER;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.THREAD_POOL_SIZE;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.TIMEOUT;
import static org.iglooproject.jpa.externallinkchecker.property.JpaExternalLinkCheckerPropertyIds.USER_AGENT;

import org.springframework.context.annotation.Configuration;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.primitives.Ints;

import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;

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
				Ints.stringConverter(),
				new Supplier<Integer>() {
					@Override
					public Integer get() {
						return Runtime.getRuntime().availableProcessors();
					}
				}
		);
		registry.registerInteger(BATCH_SIZE, 500);
		registry.register(
				MIN_DELAY_BETWEEN_TWO_CHECKS_IN_DAYS,
				new Function<String, Integer>() {
					@Override
					public Integer apply(String input) {
						Integer value = Ints.stringConverter().convert(input);
						if (value == null) {
							return null;
						}
						return value >= 0 ? value : 0;
					}
				},
				2
		);
	}

}

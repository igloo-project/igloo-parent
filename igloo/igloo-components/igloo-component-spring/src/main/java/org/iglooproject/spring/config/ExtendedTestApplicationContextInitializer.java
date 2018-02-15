package org.iglooproject.spring.config;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

/**
 * @see AbstractExtendedApplicationContextInitializer
 */
public class ExtendedTestApplicationContextInitializer extends AbstractExtendedApplicationContextInitializer {

	@Override
	protected Collection<String> getDefaultBootstrapConfigurationLocations() {
		return ImmutableList.<String>builder()
				.add("classpath:configuration-bootstrap-test.properties")
				.add("classpath:configuration-bootstrap-test-" + System.getProperty("user.name") + ".properties").build();
	}

}

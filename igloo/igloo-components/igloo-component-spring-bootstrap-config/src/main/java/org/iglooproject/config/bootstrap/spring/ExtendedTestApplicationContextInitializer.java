package org.iglooproject.config.bootstrap.spring;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

/**
 * @see AbstractExtendedApplicationContextInitializer
 */
public class ExtendedTestApplicationContextInitializer extends AbstractExtendedApplicationContextInitializer {

	@Override
	protected Collection<String> getDefaultBootstrapConfigurationLocations() {
		return ImmutableList.<String>builder()
				.add("classpath:configuration-bootstrap-default.properties")
				.add("classpath:configuration-bootstrap-test.properties")
				.add("classpath:configuration-bootstrap-test-" + System.getProperty("user.name") + ".properties").build();
	}

}

package org.iglooproject.config.bootstrap.spring;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

/**
 * @see AbstractExtendedApplicationContextInitializer
 */
public class ExtendedApplicationContextInitializer extends AbstractExtendedApplicationContextInitializer {

	@Override
	protected Collection<String> getDefaultBootstrapConfigurationLocations() {
		return ImmutableList.<String>builder()
				.add("classpath:configuration-bootstrap-default.properties")
				.add("classpath:configuration-bootstrap.properties")
				.add("classpath:configuration-bootstrap-" + System.getProperty("user.name") + ".properties").build();
	}

}

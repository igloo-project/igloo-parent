package org.iglooproject.spring.config.spring.annotation;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class DefaultConfigurationLocationProvider implements IConfigurationLocationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfigurationLocationProvider.class);

	public static final String CONFIGURER_APPLICATION_NAME_PLACEHOLDER = "${applicationName}";

	public static final String CONFIGURER_USER_PLACEHOLDER = "${user}";

	public static final String CONFIGURER_ENVIRONMENT_PLACEHOLDER = "${environment}";

	public static final String CONFIGURER_IGLOO_CONFIG_PLACEHOLDER = "${igloo.config}";

	@Override
	public List<String> getLocations(String applicationName, String environment, String... locationPatterns) {
		List<String> locations = Lists.newArrayList();
		String applicationNameEscapedPattern = Pattern.quote(CONFIGURER_APPLICATION_NAME_PLACEHOLDER);
		String userEscapedPattern = Pattern.quote(CONFIGURER_USER_PLACEHOLDER);
		String environmentEscapedPattern = Pattern.quote(CONFIGURER_ENVIRONMENT_PLACEHOLDER);
		String iglooConfigEscapedPattern = Pattern.quote(CONFIGURER_IGLOO_CONFIG_PLACEHOLDER);
		for (String location : locationPatterns) {
			location = location.replaceAll(applicationNameEscapedPattern, applicationName);
			
			String username = System.getProperty("user.name");
			if (StringUtils.isNotEmpty(username)) {
				location = location.replaceAll(userEscapedPattern, username);
			}
			
			location = location.replaceAll(environmentEscapedPattern, environment);
			String iglooConfigSystemProperty = System.getProperty("igloo.config", null);
			if (iglooConfigSystemProperty != null) {
				location = location.replaceAll(iglooConfigEscapedPattern, iglooConfigSystemProperty);
			} else if (Pattern.matches(iglooConfigEscapedPattern, location)) {
				LOGGER.warn("'igloo.config' system property is referenced but not defined;"
						+ " see @ConfigurationLocations; expression '{}' is ignored", location);
				continue;
			}
			
			locations.add(location);
		}
		return locations;
	}

}

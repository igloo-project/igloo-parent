package fr.openwide.core.spring.config.spring.annotation;

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

	public static final String CONFIGURER_OWSICORE_CONFIG_PLACEHOLDER = "${owsicore.config}";

	@Override
	public List<String> getLocations(String applicationName, String environment, String... locationPatterns) {
		List<String> locations = Lists.newArrayList();
		String applicationNameEscapedPattern = Pattern.quote(CONFIGURER_APPLICATION_NAME_PLACEHOLDER);
		String userEscapedPattern = Pattern.quote(CONFIGURER_USER_PLACEHOLDER);
		String environmentEscapedPattern = Pattern.quote(CONFIGURER_ENVIRONMENT_PLACEHOLDER);
		String owsicoreConfigEscapedPattern = Pattern.quote(CONFIGURER_OWSICORE_CONFIG_PLACEHOLDER);
		for (String location : locationPatterns) {
			location = location.replaceAll(applicationNameEscapedPattern, applicationName);
			
			String username = System.getProperty("user.name");
			if (StringUtils.isNotEmpty(username)) {
				location = location.replaceAll(userEscapedPattern, username);
			}
			
			location = location.replaceAll(environmentEscapedPattern, environment);
			String owsiCoreConfigSystemProperty = System.getProperty("owsicore.config", null);
			if (owsiCoreConfigSystemProperty != null) {
				location = location.replaceAll(owsicoreConfigEscapedPattern, owsiCoreConfigSystemProperty);
			} else if (Pattern.matches(owsicoreConfigEscapedPattern, location)) {
				LOGGER.warn("'owsicore.config' system property is referenced but not defined;"
						+ " see @ConfigurationLocations; expression '{}' is ignored", location);
				continue;
			}
			
			locations.add(location);
		}
		return locations;
	}

}

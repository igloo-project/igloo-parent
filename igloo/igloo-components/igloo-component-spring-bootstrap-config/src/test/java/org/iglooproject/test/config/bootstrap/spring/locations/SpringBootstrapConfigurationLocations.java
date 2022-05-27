package org.iglooproject.test.config.bootstrap.spring.locations;

import static org.assertj.core.api.Assertions.assertThat;

import org.iglooproject.test.config.bootstrap.spring.util.AbstractBootstrapTestCase;
import org.iglooproject.test.config.bootstrap.spring.util.SpringWithConfigurationLocationsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(inheritProperties = true, properties = {
	"igloo.profile=development",
	"user.name=username",
	"igloo.applicationName=application-test"
})
@ContextConfiguration(
		inheritLocations = true,
		classes = SpringWithConfigurationLocationsConfig.class
)
class SpringBootstrapConfigurationLocations extends AbstractBootstrapTestCase {

	@Value("${default:}")
	private String default_;
	@Value("${configuration:}")
	private String configuration;
	@Value("${override:}")
	private String override;
	@Value("${applicationName.property:}")
	private String applicationNameProperty;
	@Value("${igloo.applicationName:}")
	private String applicationName;
	@Value("${placeholder1:}")
	private String placeholder1;
	@Value("${placeholder2:}")
	private String placeholder2;

	@Test
	void configurationLocationsOrder() {
		// test loading by order on @ConfigurationLocations
		assertThat(default_).isEqualTo("default");
		assertThat(configuration).isEqualTo("configuration");
		assertThat(override).isEqualTo("override");
	}

	@Test
	void configurationLocationsApplicationName() {
		// test loading with ${igloo.applicationName}
		assertThat(applicationNameProperty).isEqualTo("app-application-test");
		
		// test ${igloo.applicationName} PropertySource injection with @ApplicationDescription
		assertThat(applicationName).isEqualTo("application-test");
	}

	@Test
	void configurationLocationsPlaceholder() {
		// test placeholder across files
		assertThat(placeholder1).isEqualTo("default.configuration.override");
		assertThat(placeholder2).isEqualTo("override");
	}

	@Test
	void configurationLocationsSystemProperty() {
		// test loading with ${user.name}
		assertThat(applicationNameProperty).isEqualTo("app-application-test");
	}
}

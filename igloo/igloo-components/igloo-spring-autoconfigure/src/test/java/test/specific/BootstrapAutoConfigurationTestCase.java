package test.specific;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManagerFactory;

import org.igloo.spring.autoconfigure.EnableIglooAutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap3AutoConfiguration;
import org.igloo.spring.autoconfigure.bootstrap.IglooBootstrap4AutoConfiguration;
import org.iglooproject.wicket.bootstrap4.application.WicketBootstrapModule;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Base class used to check that {@link EnableIglooAutoConfiguration} triggers IglooBootstrapAutoConfiguration properly. 
 * 
 * This class uses ApplicationContextRunner to initialize contexts with suitable configurations,
 * which are declared at the bottom of the file.
 *  
 */
public class BootstrapAutoConfigurationTestCase {

	/**
	 * Check that autoconfiguration from bootstrap 4 {@link WicketBootstrapModule} is triggered with
	 * EnableIglooAutoConfiguration when excluding bootstrap 3
	 */
	@Test
	public void testIglooBootstrap4AutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.withPropertyValues(String.format("spring.autoconfigure.exclude=%s",
					IglooBootstrap3AutoConfiguration.class.getName()))
			.run(
				(context) -> { 
					assertThat(context).hasSingleBean(WicketBootstrapModule.class);
					assertThat(context).doesNotHaveBean(
							org.iglooproject.wicket.bootstrap3.application.WicketBootstrapModule.class
						);
				}
			);
	}


	/**
	 * Check that autoconfiguration from bootstrap 3 {@link WicketBootstrapModule} is triggered with
	 * EnableIglooAutoConfiguration  when excluding bootstrap 4
	 */
	@Test
	public void testIglooBootstrap3AutoConfigure() {
		new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(TestConfig.class))
			.withPropertyValues(String.format("spring.autoconfigure.exclude=%s",
					IglooBootstrap4AutoConfiguration.class.getName()))
			.run(
				(context) -> { 
					assertThat(context).hasSingleBean(
							org.iglooproject.wicket.bootstrap3.application.WicketBootstrapModule.class
						);
					assertThat(context).doesNotHaveBean(WicketBootstrapModule.class);

				}
			);
	}
	
	@Configuration
	@EnableIglooAutoConfiguration
	public static class TestConfig {}

}

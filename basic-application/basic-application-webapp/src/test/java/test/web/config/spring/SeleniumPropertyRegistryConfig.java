package test.web.config.spring;

import static test.web.property.SeleniumPropertyIds.GECKODRIVER_PATH;

import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerString(GECKODRIVER_PATH);
	}

}

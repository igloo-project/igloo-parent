package org.iglooproject.showcase.web.application.config.spring;

import static org.iglooproject.showcase.web.application.util.property.ShowcaseWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;
import static org.iglooproject.showcase.web.application.util.property.ShowcaseWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_USER;

import org.springframework.context.annotation.Configuration;

import org.iglooproject.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;

@Configuration
public class ShowcaseWebappApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE, 15);
		registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE_USER, 5);
	}

}

package fr.openwide.core.showcase.web.application.config.spring;

import static fr.openwide.core.showcase.web.application.util.property.ShowcaseWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;
import static fr.openwide.core.showcase.web.application.util.property.ShowcaseWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_USER;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Configuration
public class ShowcaseWebappApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE, 15);
		registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE_USER, 5);
	}

}

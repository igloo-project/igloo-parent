package fr.openwide.core.showcase.web.application.config.spring;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.jpa.more.business.property.service.IPropertyRegistry;
import fr.openwide.core.jpa.more.config.spring.AbstractApplicationPropertyRegistryConfig;
import fr.openwide.core.showcase.web.application.util.property.ShowcaseWebappPropertyIds;

@Configuration
public class ShowcaseWebappApplicationPropertyRegisterConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerInteger(ShowcaseWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE, 15);
		registry.registerInteger(ShowcaseWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_USER, 5);
	}

}

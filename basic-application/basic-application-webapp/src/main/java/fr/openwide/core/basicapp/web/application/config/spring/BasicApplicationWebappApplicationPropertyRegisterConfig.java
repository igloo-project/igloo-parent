package fr.openwide.core.basicapp.web.application.config.spring;

import static fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds.MAINTENANCE_URL;
import static fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;
import static fr.openwide.core.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;
import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

public class BasicApplicationWebappApplicationPropertyRegisterConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	protected void register(IPropertyRegistry registry) {
		registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE, 20);
		registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION, 20);
		
		registry.registerString(MAINTENANCE_URL);
	}

}

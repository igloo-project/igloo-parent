package fr.openwide.core.showcase.web.application.config.spring;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.jpa.more.business.property.service.IConfigurablePropertyService;
import fr.openwide.core.jpa.more.config.spring.AbstractApplicationPropertyRegisterConfig;
import fr.openwide.core.showcase.web.application.util.property.ShowcaseWebappPropertyIds;

@Configuration
public class ShowcaseWebappApplicationPropertyRegisterConfig extends AbstractApplicationPropertyRegisterConfig {

	@Override
	public void register(IConfigurablePropertyService propertyService) {
		propertyService.registerInteger(ShowcaseWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE, 15);
		propertyService.registerInteger(ShowcaseWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_USER, 5);
	}

}

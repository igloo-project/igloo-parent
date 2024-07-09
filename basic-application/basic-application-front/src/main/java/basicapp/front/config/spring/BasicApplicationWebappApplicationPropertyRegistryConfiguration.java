package basicapp.front.config.spring;

import static basicapp.front.property.BasicApplicationWebappPropertyIds.APPLICATION_THEME;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.MAINTENANCE_URL;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;
import static basicapp.front.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;

import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

import basicapp.front.common.template.theme.BasicApplicationApplicationTheme;

@Configuration
public class BasicApplicationWebappApplicationPropertyRegistryConfiguration implements IPropertyRegistryConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerEnum(APPLICATION_THEME, BasicApplicationApplicationTheme.class, BasicApplicationApplicationTheme.ADVANCED);
		
		registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE, 20);
		registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION, 20);
		
		registry.registerString(MAINTENANCE_URL);
	}

}

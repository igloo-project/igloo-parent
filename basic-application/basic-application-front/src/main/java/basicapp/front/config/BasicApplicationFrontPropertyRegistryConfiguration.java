package basicapp.front.config;

import static basicapp.front.property.BasicApplicationFrontPropertyIds.APPLICATION_THEME;
import static basicapp.front.property.BasicApplicationFrontPropertyIds.MAINTENANCE_URL;
import static basicapp.front.property.BasicApplicationFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;
import static basicapp.front.property.BasicApplicationFrontPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;

import basicapp.front.common.template.theme.BasicApplicationApplicationTheme;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationFrontPropertyRegistryConfiguration implements IPropertyRegistryConfig {

  @Override
  public void register(IPropertyRegistry registry) {
    registry.registerEnum(
        APPLICATION_THEME,
        BasicApplicationApplicationTheme.class,
        BasicApplicationApplicationTheme.ADVANCED);

    registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE, 20);
    registry.registerInteger(PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION, 20);

    registry.registerString(MAINTENANCE_URL);
  }
}

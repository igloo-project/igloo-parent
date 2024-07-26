package org.iglooproject.basicapp.web.application.config.spring;

import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.APPLICATION_THEME;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.MAINTENANCE_URL;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE;
import static org.iglooproject.basicapp.web.application.property.BasicApplicationWebappPropertyIds.PORTFOLIO_ITEMS_PER_PAGE_DESCRIPTION;

import org.iglooproject.basicapp.web.application.common.template.theme.BasicApplicationApplicationTheme;
import org.iglooproject.spring.config.spring.IPropertyRegistryConfig;
import org.iglooproject.spring.property.service.IPropertyRegistry;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicApplicationWebappApplicationPropertyRegistryConfig
    implements IPropertyRegistryConfig {

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

package org.iglooproject.wicket.more.config;

import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.AUTOPREFIXER_ENABLED;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.SCSS_STATIC_ENABLED;
import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.SCSS_STATIC_RESOURCE_PATH;

import org.iglooproject.sass.config.ISassConfigurationProvider;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SassConfigurationProvider implements ISassConfigurationProvider {

  @Autowired private IPropertyService propertyService;

  @Override
  public boolean isAutoprefixerEnabled() {
    return propertyService.get(AUTOPREFIXER_ENABLED);
  }

  @Override
  public boolean useStatic() {
    return propertyService.get(SCSS_STATIC_ENABLED);
  }

  @Override
  public String getResourcePath() {
    return propertyService.get(SCSS_STATIC_RESOURCE_PATH);
  }
}

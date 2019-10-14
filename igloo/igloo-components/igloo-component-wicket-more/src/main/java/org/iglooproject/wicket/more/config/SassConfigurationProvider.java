package org.iglooproject.wicket.more.config;

import static org.iglooproject.wicket.more.property.WicketMorePropertyIds.AUTOPREFIXER_ENABLED;

import org.iglooproject.sass.config.ISassConfigurationProvider;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SassConfigurationProvider implements ISassConfigurationProvider {

	@Autowired
	private IPropertyService propertyService;

	@Override
	public boolean isAutoprefixerEnabled() {
		return propertyService.get(AUTOPREFIXER_ENABLED);
	}

}

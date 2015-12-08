package fr.openwide.core.showcase.core.config.spring;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.jpa.more.business.property.service.IConfigurablePropertyService;
import fr.openwide.core.jpa.more.config.spring.AbstractApplicationPropertyRegisterConfig;
import fr.openwide.core.showcase.core.util.property.ShowcaseCorePropertyIds;

@Configuration
public class ShowcaseCoreApplicationPropertyRegisterConfig extends AbstractApplicationPropertyRegisterConfig {

	@Override
	public void register(IConfigurablePropertyService propertyService) {
		propertyService.registerString(ShowcaseCorePropertyIds.SHOWCASE_FILE_ROOT_DIRECTORY);
	}

}

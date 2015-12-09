package fr.openwide.core.showcase.core.config.spring;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.jpa.more.business.property.service.IPropertyRegistry;
import fr.openwide.core.jpa.more.config.spring.AbstractApplicationPropertyConfig;
import fr.openwide.core.showcase.core.util.property.ShowcaseCorePropertyIds;

@Configuration
public class ShowcaseCoreApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerString(ShowcaseCorePropertyIds.SHOWCASE_FILE_ROOT_DIRECTORY);
	}

}

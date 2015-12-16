package fr.openwide.core.showcase.core.config.spring;

import static fr.openwide.core.showcase.core.util.property.ShowcaseCorePropertyIds.SHOWCASE_FILE_ROOT_DIRECTORY;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyConfig;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Configuration
public final class ShowcaseCoreApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	public void register(IPropertyRegistry registry) {
		registry.registerString(SHOWCASE_FILE_ROOT_DIRECTORY);
	}

}

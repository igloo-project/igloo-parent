package fr.openwide.core.jpa.more.config.spring;

import static fr.openwide.core.jpa.more.property.JpaMorePropertyIds.DATABASE_INITIALIZED;
import static fr.openwide.core.jpa.more.property.JpaMorePropertyIds.DATA_UPGRADE_DONE_TEMPLATE;
import static fr.openwide.core.jpa.more.property.JpaMorePropertyIds.MAINTENANCE;

import org.springframework.context.annotation.Import;

import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyRegistryConfig;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Import(JpaMoreTaskApplicationPropertyRegistryConfig.class)
public class JpaMoreApplicationPropertyRegistryConfig extends AbstractApplicationPropertyRegistryConfig {

	@Override
	protected void register(IPropertyRegistry registry) {
		registry.registerBoolean(DATABASE_INITIALIZED, false);
		registry.registerBoolean(DATA_UPGRADE_DONE_TEMPLATE, false);
		registry.registerBoolean(MAINTENANCE, false);
	}

}

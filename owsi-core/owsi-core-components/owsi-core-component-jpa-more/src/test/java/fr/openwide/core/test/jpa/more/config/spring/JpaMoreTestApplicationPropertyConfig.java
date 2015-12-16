package fr.openwide.core.test.jpa.more.config.spring;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyConfig;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Configuration
public final class JpaMoreTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	protected void register(IPropertyRegistry registry) {
	}

}

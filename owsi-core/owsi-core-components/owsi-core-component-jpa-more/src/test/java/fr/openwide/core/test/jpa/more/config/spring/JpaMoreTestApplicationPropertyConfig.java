package fr.openwide.core.test.jpa.more.config.spring;

import org.springframework.context.annotation.Configuration;

import fr.openwide.core.jpa.more.business.property.service.IPropertyRegistry;
import fr.openwide.core.jpa.more.config.spring.AbstractApplicationPropertyConfig;

@Configuration
public class JpaMoreTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	protected void register(IPropertyRegistry registry) {
	}

}

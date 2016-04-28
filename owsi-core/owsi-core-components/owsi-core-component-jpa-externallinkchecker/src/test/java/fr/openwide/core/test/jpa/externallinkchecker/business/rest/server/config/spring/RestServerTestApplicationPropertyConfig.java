package fr.openwide.core.test.jpa.externallinkchecker.business.rest.server.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import fr.openwide.core.jpa.externallinkchecker.config.spring.JpaExternalLinkCheckerApplicationPropertyRegistryConfig;
import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyConfig;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.dao.StubMutablePropertyDao;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

@Import(JpaExternalLinkCheckerApplicationPropertyRegistryConfig.class)
public class RestServerTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	@Bean
	public IMutablePropertyDao mutablePropertyDao() {
		return new StubMutablePropertyDao();
	}

	@Override
	protected void register(IPropertyRegistry registry) {
	}

}

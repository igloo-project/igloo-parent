package fr.openwide.core.test.wicket.more.config.spring;

import org.springframework.context.annotation.Bean;

import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyConfig;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.dao.StubMutablePropertyDao;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

public class WicketMoreTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	@Bean
	public IMutablePropertyDao mutablePropertyDao() {
		return new StubMutablePropertyDao();
	}

	@Override
	protected void register(IPropertyRegistry registry) {
	}

}

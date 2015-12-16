package fr.openwide.jpa.example.config.spring;

import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyConfig;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.dao.StubMutablePropertyDao;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

public class CoreJpaExampleApplicationPropertyConfig extends AbstractApplicationPropertyConfig {

	@Override
	public IMutablePropertyDao mutablePropertyDao() {
		return new StubMutablePropertyDao();
	}

	@Override
	protected void register(IPropertyRegistry registry) {
	}

}

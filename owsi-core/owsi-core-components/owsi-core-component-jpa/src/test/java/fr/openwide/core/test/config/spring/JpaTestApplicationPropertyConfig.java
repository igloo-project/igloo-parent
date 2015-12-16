package fr.openwide.core.test.config.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.config.spring.AbstractApplicationPropertyConfig;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.service.IPropertyRegistry;

public class JpaTestApplicationPropertyConfig extends AbstractApplicationPropertyConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JpaTestApplicationPropertyConfig.class);
	
	@Override
	@Bean
	public IMutablePropertyDao mutablePropertyDao() {
		// Return a stub
		return new IMutablePropertyDao() {
			@Override
			public void set(String key, String value) throws ServiceException, SecurityServiceException {
				LOGGER.warn(String.format("Call set(%1s, %1s) from mutablePropertyDao stub.", key, value));
			}
			@Override
			public String get(String key) {
				return null;
			}
			@Override
			public void clean() {
				LOGGER.warn("Call clean() from mutablePropertyDao stub.");
			}
		};
	}

	@Override
	protected void register(IPropertyRegistry registry) {
	}

}

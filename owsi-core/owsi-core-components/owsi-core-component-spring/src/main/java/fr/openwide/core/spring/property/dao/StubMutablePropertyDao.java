package fr.openwide.core.spring.property.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public class StubMutablePropertyDao implements IMutablePropertyDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(StubMutablePropertyDao.class);

	@Override
	public void setInTransaction(String key, String value) throws ServiceException, SecurityServiceException {
		LOGGER.warn(String.format("Call set(%1s, %1s) from mutablePropertyDao stub.", key, value));
	}

	@Override
	public String getInTransaction(String key) {
		return null;
	}

	@Override
	public void cleanInTransaction() {
		LOGGER.warn("Call clean() from mutablePropertyDao stub.");
	}

}

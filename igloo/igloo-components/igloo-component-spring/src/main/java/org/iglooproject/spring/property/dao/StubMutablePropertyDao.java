package org.iglooproject.spring.property.dao;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StubMutablePropertyDao implements IMutablePropertyDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(StubMutablePropertyDao.class);

  @Override
  public void setInTransaction(String key, String value)
      throws ServiceException, SecurityServiceException {
    LOGGER.warn("Call set({}, {}) from mutablePropertyDao stub.", key, value);
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

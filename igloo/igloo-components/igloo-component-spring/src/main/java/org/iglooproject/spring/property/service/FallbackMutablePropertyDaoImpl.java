package org.iglooproject.spring.property.service;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.dao.IImmutablePropertyDao;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.jspecify.annotations.Nullable;

public class FallbackMutablePropertyDaoImpl implements IMutablePropertyDao {

  private final IImmutablePropertyDao immutablePropertyDao;

  public FallbackMutablePropertyDaoImpl(IImmutablePropertyDao immutablePropertyDao) {
    this.immutablePropertyDao = immutablePropertyDao;
  }

  @Override
  public @Nullable String getInTransaction(String key) {
    return immutablePropertyDao.get(key);
  }

  @Override
  public void setInTransaction(String key, @Nullable String value)
      throws ServiceException, SecurityServiceException {
    throw new RuntimeException(
        String.format(
            "%s does not support mutability",
            FallbackMutablePropertyDaoImpl.class.getSimpleName()));
  }

  @Override
  public void cleanInTransaction() {
    // Nothing persisted, nothing to clean
  }
}

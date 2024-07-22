package org.iglooproject.spring.property.dao;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface IMutablePropertyDao {

  String getInTransaction(String key);

  void setInTransaction(String key, String value) throws ServiceException, SecurityServiceException;

  void cleanInTransaction();
}

package org.iglooproject.jpa.business.generic.listener;

import java.io.Serializable;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface GenericEntityListener<
    K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>> {

  void onCreate(E entity) throws ServiceException, SecurityServiceException;

  void onUpdate(E entity) throws ServiceException, SecurityServiceException;

  void onDelete(E entity) throws ServiceException, SecurityServiceException;
}

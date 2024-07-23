package org.iglooproject.jpa.more.business.upgrade.model;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface IDataUpgrade {

  /**
   * @return Le nom (unique) de cette mise à jour.
   */
  String getName();

  void perform() throws ServiceException, SecurityServiceException;
}

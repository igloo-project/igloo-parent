package org.iglooproject.basicapp.core.business.upgrade.service;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface IDataUpgradeManager {

  void autoPerformDataUpgrades() throws ServiceException, SecurityServiceException;
}

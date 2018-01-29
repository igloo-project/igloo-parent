package org.iglooproject.basicapp.core.business.upgrade.service;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface IDataUpgradeManager {

	public final static String DATA_UPGRADE_NAME_PREFIX = "dataUpgrade.";
	public final static String DATA_UPGRADE_NAME_SUFFIX = ".autoPerform";

	void autoPerformDataUpgrades() throws ServiceException, SecurityServiceException;
}

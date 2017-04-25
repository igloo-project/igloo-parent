package fr.openwide.core.basicapp.core.business.upgrade.service;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IDataUpgradeService {

	public final static String DATA_UPGRADE_NAME_PREFIX = "dataUpgrade.";
	public final static String DATA_UPGRADE_NAME_SUFFIX = ".autoPerform";

	void autoPerformDataUpgrades() throws ServiceException, SecurityServiceException;
}

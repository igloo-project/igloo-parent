package basicapp.back.business.upgrade.service;

import basicapp.back.business.upgrade.exception.DataUpgradeServiceException;

public interface IDataUpgradeManager {

  void autoPerformDataUpgrades() throws DataUpgradeServiceException;
}

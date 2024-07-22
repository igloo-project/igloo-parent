package org.iglooproject.jpa.more.business.upgrade.service;

import java.util.List;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.spring.util.SpringBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class AbstractDataUpgradeServiceImpl implements IAbstractDataUpgradeService {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractDataUpgradeServiceImpl.class);

  @Autowired private IDataUpgradeRecordService recordService;

  @Autowired private ApplicationContext applicationContext;

  @Override
  public void executeDataUpgrade(IDataUpgrade upgrade)
      throws ServiceException, SecurityServiceException {
    boolean upgradeAlreadyDone = recordService.isDone(upgrade);

    if (!upgradeAlreadyDone) {
      SpringBeanUtils.autowireBean(applicationContext, upgrade);
      upgrade.perform();

      recordService.markAsDone(upgrade);
    } else {
      LOGGER.warn("Data upgrade '" + upgrade.getName() + "' has already been done.");
    }
  }

  @Override
  public abstract List<IDataUpgrade> listDataUpgrades();
}

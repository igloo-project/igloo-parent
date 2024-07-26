package org.iglooproject.jpa.more.business.upgrade.service;

import java.util.List;
import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;

public interface IAbstractDataUpgradeService extends ITransactionalAspectAwareService {

  /**
   * Execute une mise à jour ponctuelle sur les données de l'application
   *
   * @param upgrade le handler de la mise à jour
   * @throws ServiceException
   * @throws SecurityServiceException
   */
  void executeDataUpgrade(IDataUpgrade upgrade) throws ServiceException, SecurityServiceException;

  List<IDataUpgrade> listDataUpgrades();
}

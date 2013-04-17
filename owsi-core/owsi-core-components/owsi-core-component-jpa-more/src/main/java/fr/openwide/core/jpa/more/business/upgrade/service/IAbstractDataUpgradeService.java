package fr.openwide.core.jpa.more.business.upgrade.service;

import java.util.List;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;

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

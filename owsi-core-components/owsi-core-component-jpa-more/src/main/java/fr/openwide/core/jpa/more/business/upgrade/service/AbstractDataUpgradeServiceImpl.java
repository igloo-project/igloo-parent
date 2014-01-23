package fr.openwide.core.jpa.more.business.upgrade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.parameter.service.IAbstractParameterService;
import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;
import fr.openwide.core.spring.util.SpringBeanUtils;

public abstract class AbstractDataUpgradeServiceImpl implements IAbstractDataUpgradeService {

	@Autowired
	private IAbstractParameterService parameterService;
	
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void executeDataUpgrade(IDataUpgrade upgrade) throws ServiceException, SecurityServiceException {
		// On vérifie que la mise à jour n'a pas déjà été exécutée
		boolean upgradeAlreadyDone = parameterService.isDataUpgradeDone(upgrade);
		
		if (!upgradeAlreadyDone) {
			SpringBeanUtils.autowireBean(applicationContext, upgrade);
			upgrade.perform();
			
			// On marque la mise à jour comme exécutée
			parameterService.setDataUpgradeDone(upgrade, true);
		}
	}

	@Override
	public abstract List<IDataUpgrade> listDataUpgrades();
}

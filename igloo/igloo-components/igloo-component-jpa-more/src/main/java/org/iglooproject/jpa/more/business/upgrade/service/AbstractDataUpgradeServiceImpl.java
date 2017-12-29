package org.iglooproject.jpa.more.business.upgrade.service;

import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.dataUpgrade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.spring.util.SpringBeanUtils;

public abstract class AbstractDataUpgradeServiceImpl implements IAbstractDataUpgradeService {

	@Autowired
	private IPropertyService propertyService;
	
	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void executeDataUpgrade(IDataUpgrade upgrade) throws ServiceException, SecurityServiceException {
		// On vérifie que la mise à jour n'a pas déjà été exécutée
		boolean upgradeAlreadyDone = propertyService.get(dataUpgrade(upgrade));
		
		if (!upgradeAlreadyDone) {
			SpringBeanUtils.autowireBean(applicationContext, upgrade);
			upgrade.perform();
			
			// On marque la mise à jour comme exécutée
			propertyService.set(dataUpgrade(upgrade), true);
		}
	}

	@Override
	public abstract List<IDataUpgrade> listDataUpgrades();
}

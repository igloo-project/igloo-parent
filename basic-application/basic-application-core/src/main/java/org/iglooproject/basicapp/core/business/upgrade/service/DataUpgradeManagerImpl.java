package org.iglooproject.basicapp.core.business.upgrade.service;

import java.util.List;

import org.iglooproject.basicapp.core.business.upgrade.model.DataUpgradePackage;
import org.iglooproject.basicapp.core.business.upgrade.model.DataUpgrade_InitDataFromExcel;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.upgrade.model.DataUpgradeRecord;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.jpa.more.business.upgrade.service.AbstractDataUpgradeServiceImpl;
import org.iglooproject.jpa.more.business.upgrade.service.IDataUpgradeRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableList;

@Service("dataUpgradeManager")
public class DataUpgradeManagerImpl extends AbstractDataUpgradeServiceImpl implements IDataUpgradeManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUpgradeManagerImpl.class);

	@Autowired
	private IDataUpgradeRecordService dataUpgradeRecordService;

	private TransactionTemplate transactionTemplate;

	@Override
	public List<IDataUpgrade> listDataUpgrades() {
		return ImmutableList.<IDataUpgrade>of(
				new DataUpgrade_InitDataFromExcel()
		);
	}

	@Override
	public void autoPerformDataUpgrades() throws ServiceException, SecurityServiceException {
		List<DataUpgradeRecord> records = dataUpgradeRecordService.listAutoPerform();
		
		if (!records.isEmpty()) {
			LOGGER.info("Executing automatically data upgrades (starting)");
			
			for (DataUpgradeRecord record : records) {
				String dataUpdateClassName = DataUpgradePackage.class.getPackage().getName() + "." + record.getName();
				
				final IDataUpgrade upgrade;
				try {
					upgrade = (IDataUpgrade) Class.forName(dataUpdateClassName).newInstance();
				} catch (ClassNotFoundException e) {
					throw new ServiceException("La classe d'upgrade '" + dataUpdateClassName + "' n'a pas été trouvée", e);
				} catch (InstantiationException e) {
					throw new ServiceException("Erreur lors de l'instantiation de la class d'upgrade '" + dataUpdateClassName + "'", e);
				} catch (IllegalAccessException e) {
					throw new ServiceException("L'instantiation de la class d'upgrade '" + dataUpdateClassName + " n'est pas autorisée", e);
				}
				
				Throwable result = null;
				try {
					result = transactionTemplate.execute(new TransactionCallback<Throwable>() {
						@Override
						public Throwable doInTransaction(TransactionStatus transactionStatus) {
							try {
								executeDataUpgrade(upgrade);
							} catch (ServiceException | SecurityServiceException e) {
								transactionStatus.setRollbackOnly();
								return e;
							}
							return null;
						}
					});
				} catch (Exception e) {
					LOGGER.error("Erreur lors de l'exécution automatique d'un data upgrade", e);
				}
				
				if (upgrade != null && result != null) {
					LOGGER.error("Erreur lors de l'exécution automatique d'un data upgrade", result);
					dataUpgradeRecordService.markAsToDo(upgrade);
					dataUpgradeRecordService.disableAutoPerform(upgrade);
				}
			}
			
			LOGGER.info("Executing automatically data upgrades (success)");
		}
	}

	@Autowired
	private void setTransactionTemplate(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
		transactionTemplate = new TransactionTemplate(transactionManager, transactionAttribute);
	}
}

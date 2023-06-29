package org.iglooproject.basicapp.core.business.upgrade.service;

import java.lang.reflect.InvocationTargetException;
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
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class DataUpgradeManagerImpl extends AbstractDataUpgradeServiceImpl implements IDataUpgradeManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUpgradeManagerImpl.class);

	@Autowired
	private IDataUpgradeRecordService dataUpgradeRecordService;

	private TransactionTemplate transactionTemplate;

	@Override
	public List<IDataUpgrade> listDataUpgrades() {
		return List.<IDataUpgrade>of(
			new DataUpgrade_InitDataFromExcel()
		);
	}

	@Override
	public void autoPerformDataUpgrades() throws ServiceException, SecurityServiceException {
		List<DataUpgradeRecord> records = dataUpgradeRecordService.listAutoPerform();
		
		if (!records.isEmpty()) {
			LOGGER.info("Executing automatically data upgrades (starting)");
			
			// TODO: allow a failed data upgrade to stop application startup and subsequent upgrades
			int missed = 0;
			for (DataUpgradeRecord record : records) {
				boolean success = performDataUpgradeRecord(record);
				if (!success) {
					missed++;
				}
			}
			
			if (missed == 0) {
				LOGGER.info("Executing automatically data upgrades (success)");
			} else {
				LOGGER.warn("Executing automatically data upgrades (done): {} errors", missed);
			}
		}
	}

	private boolean performDataUpgradeRecord(DataUpgradeRecord record) throws ServiceException, SecurityServiceException {
		String dataUpdateClassName = DataUpgradePackage.class.getPackage().getName() + "." + record.getName();
		
		final IDataUpgrade upgrade;
		try {
			upgrade = (IDataUpgrade) Class.forName(dataUpdateClassName).getConstructor().newInstance();
		} catch (ClassNotFoundException e) {
			throw new ServiceException(String.format("Upgrade class %s not found", dataUpdateClassName), e);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
			throw new ServiceException(String.format("Upgrade class %s cannot be instantiated", dataUpdateClassName), e);
		}

		
		Throwable result = null;
		try {
			transactionTemplate.execute(transactionStatus -> performUpgrade(upgrade, transactionStatus));
		} catch (Exception e) {
			result = e;
		}
		
		if (result != null) {
			LOGGER.error("Erreur lors de l'exécution automatique d'un data upgrade", result);
			dataUpgradeRecordService.markAsToDo(upgrade);
			dataUpgradeRecordService.disableAutoPerform(upgrade);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Perform an upgrade, any thrown {@link RuntimeException}, {@link ServiceException}
	 * and {@link SecurityServiceException} mark current transaction as rollbacked.
	 */
	private Throwable performUpgrade(IDataUpgrade upgrade, TransactionStatus transactionStatus) {
		try {
			executeDataUpgrade(upgrade);
		} catch (RuntimeException | ServiceException | SecurityServiceException e) {
			transactionStatus.setRollbackOnly();
			return e;
		}
		return null;
	}

	@Autowired
	private void setTransactionTemplate(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
		transactionTemplate = new TransactionTemplate(transactionManager, transactionAttribute);
	}

}

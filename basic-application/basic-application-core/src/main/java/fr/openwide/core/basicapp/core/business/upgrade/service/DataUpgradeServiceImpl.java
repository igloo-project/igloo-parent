package fr.openwide.core.basicapp.core.business.upgrade.service;

import static fr.openwide.core.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_PREFIX;
import static fr.openwide.core.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_SUFFIX;
import static fr.openwide.core.basicapp.core.property.BasicApplicationCorePropertyIds.dataUpgradeAutoperfom;
import static fr.openwide.core.jpa.more.property.JpaMorePropertyIds.dataUpgrade;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import fr.openwide.core.basicapp.core.business.upgrade.dao.IDataUpgradeDao;
import fr.openwide.core.basicapp.core.business.upgrade.model.DataUpgradePackage;
import fr.openwide.core.basicapp.core.business.upgrade.model.MigrationTest;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;
import fr.openwide.core.jpa.more.business.upgrade.service.AbstractDataUpgradeServiceImpl;
import fr.openwide.core.spring.property.service.IPropertyService;

@Service("dataUpgradeService")
public class DataUpgradeServiceImpl extends AbstractDataUpgradeServiceImpl implements IDataUpgradeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataUpgradeServiceImpl.class);

	private static final String DATA_UPDATE_AUTOPERFORM_PATTERN = 
			DATA_UPGRADE_AUTOPERFOM_PREFIX + "\\.([^\\.]+)\\." + DATA_UPGRADE_AUTOPERFOM_SUFFIX;

	@Autowired
	private IDataUpgradeDao dataUpgradeDao;
	
	@Autowired
	private IPropertyService propertyService;
	
	private TransactionTemplate transactionTemplate;
	
	@Override
	public List<IDataUpgrade> listDataUpgrades() {
		return ImmutableList.<IDataUpgrade>of(
				new MigrationTest()
		);
	}
	
	@Override
	public void autoPerformDataUpgrades() throws ServiceException, SecurityServiceException {
		List<String> upgradeNamesFromDao = dataUpgradeDao.listDataUpgradeAutoPerfom();
		Pattern p = Pattern.compile(DATA_UPDATE_AUTOPERFORM_PATTERN);
		
		for (String nameFromDao : upgradeNamesFromDao) {
			Matcher m = p.matcher(nameFromDao);
			
			if (m.matches()) {
				String dataUpdateClassName = DataUpgradePackage.class.getPackage().getName() + "." + m.group(1);
				
				final IDataUpgrade dataUpgrade;
				try {
					dataUpgrade = (IDataUpgrade) Class
							.forName(dataUpdateClassName)
							.newInstance();
				} catch (Exception e) {
					throw new ServiceException("Erreur lors de l'instantiation d'un data upgrade", e);
				}
				
				Throwable result = null;
				try {
					result = transactionTemplate.execute(new TransactionCallback<Throwable>() {
						@Override
						public Throwable doInTransaction(TransactionStatus transactionStatus) {
							try {
								executeDataUpgrade(dataUpgrade);
								propertyService.set(dataUpgradeAutoperfom(dataUpgrade), false);
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
				
				if (dataUpgrade != null && result != null) {
					LOGGER.error("Erreur lors de l'exécution automatique d'un data upgrade", result);
					
					propertyService.set(dataUpgrade(dataUpgrade), false);
					propertyService.set(dataUpgradeAutoperfom(dataUpgrade), false);
				}
			}
		}
	}
	
	
	@Autowired
	private void setTransactionTemplate(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute transactionAttribute = new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
		transactionTemplate = new TransactionTemplate(transactionManager, transactionAttribute);
	}
}

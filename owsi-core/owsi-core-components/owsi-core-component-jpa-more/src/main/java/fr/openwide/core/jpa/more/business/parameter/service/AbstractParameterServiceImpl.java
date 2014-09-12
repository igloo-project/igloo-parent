package fr.openwide.core.jpa.more.business.parameter.service;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.parameter.dao.IParameterDao;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter_;
import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;

public class AbstractParameterServiceImpl extends GenericEntityServiceImpl<Long, Parameter>
		implements ApplicationListener<ContextRefreshedEvent>, IAbstractParameterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParameterServiceImpl.class);

	private static final String DATABASE_INITIALIZED = "databaseInitialized";

	public static final String PARAMETER_DATA_UPGRADE_PREFIX_DEFAULT = "dataUpgrade.";

	private static final String PARAMETER_MAINTENANCE = "maintenance";

	private Boolean isInMaintenance;

	private IParameterDao dao;

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	public AbstractParameterServiceImpl(IParameterDao dao) {
		super(dao);
		this.dao = dao;
	}

	@PostConstruct
	public void init() throws ServiceException, SecurityServiceException {
		DefaultTransactionAttribute transactionAttributes = new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		transactionAttributes.setReadOnly(false);
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionAttributes);
		
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					if (getByName(PARAMETER_MAINTENANCE) == null) {
						setParameterMaintenance(false);
					}
					isInMaintenance = getParameterMaintenance();
				} catch (Exception e) {
					LOGGER.error("Erreur lors de l'initialisation de la variable maintenance", e);
				}
			}
		});
	}

	@Override
	public boolean isDatabaseInitialized() {
		return getBooleanValue(DATABASE_INITIALIZED, false);
	}

	@Override
	public void setDatabaseInitialized(boolean databaseInitialized) throws ServiceException, SecurityServiceException {
		updateBooleanValue(DATABASE_INITIALIZED, databaseInitialized);
	}

	@Override
	public boolean isDataUpgradeDone(IDataUpgrade upgrade) {
		return getBooleanValue(getDataUpgradeParameterPrefix() + upgrade.getName(), false);
	}

	@Override
	public void setDataUpgradeDone(IDataUpgrade upgrade, boolean dateUpgradeDone)
			throws ServiceException, SecurityServiceException {
		updateBooleanValue(getDataUpgradeParameterPrefix() + upgrade.getName(), dateUpgradeDone);
	}

	protected Parameter getByName(String name) {
		return dao.getByField(Parameter_.name, name);
	}

	protected boolean getBooleanValue(String name, boolean defaultValue) {
		Parameter parameter = getByName(name);
		if (parameter != null && parameter.getBooleanValue() != null) {
			return parameter.getBooleanValue().booleanValue();
		} else {
			return defaultValue;
		}
	}

	protected String getStringValue(String name, String defaultValue) {
		Parameter parameter = getByName(name);
		if (parameter != null && parameter.getStringValue() != null) {
			return parameter.getStringValue();
		} else {
			return defaultValue;
		}
	}

	protected String getStringValue(String name) {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			return parameter.getStringValue();
		} else {
			return null;
		}
	}

	protected int getIntegerValue(String name, int defaultValue) {
		Parameter parameter = getByName(name);
		if (parameter != null && parameter.getIntegerValue() != null) {
			return parameter.getIntegerValue().intValue();
		} else {
			return defaultValue;
		}
	}

	protected float getFloatValue(String name, float defaultValue) {
		Parameter parameter = getByName(name);
		if (parameter != null && parameter.getFloatValue() != null) {
			return parameter.getFloatValue().floatValue();
		} else {
			return defaultValue;
		}
	}

	protected Date getDateValue(String name) {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			return parameter.getDateValue();
		} else {
			return null;
		}
	}

	@Override
	public final void onApplicationEvent(ContextRefreshedEvent event) {
		// si l'événement est un refresh et que la source de l'événement est un application context SANS
		// parent alors on lance l'import de la configuration dans la base
		// (ceci pour distinguer le contexte du -core qui ne possède pas de
		// parent
		// et qui est celui qui nous intéresse, et le contexte du -web, qui
		// possède
		// un parent et pour lequel il est superflu de lancer l'import).
		if (event != null && event.getSource() != null
				&& AbstractApplicationContext.class.isAssignableFrom(event.getSource().getClass())
				&& ((AbstractApplicationContext) event.getSource()).getParent() == null) {
			// On peut avoir besoin de charger un certain nombre de paramètres
			// du fichier de configuration
			// dans la base pour par exemple pouvoir y accéder via du pl/pgsql.
			try {
				LOGGER.info("Loading properties into the database.");
				doOnApplicationEvent();
			} catch (Exception e) {
				LOGGER.error("Unable to load the properties into the database.", e);
			}
		}
	}

	protected void doOnApplicationEvent() throws ServiceException, SecurityServiceException {
	}

	protected final void updateBooleanValue(String name, Boolean booleanValue)
			throws ServiceException, SecurityServiceException {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			parameter.setBooleanValue(booleanValue);
			update(parameter);
		} else {
			create(new Parameter(name, booleanValue));
		}
	}

	protected final void updateIntegerValue(String name, Integer integerValue)
			throws ServiceException, SecurityServiceException {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			parameter.setIntegerValue(integerValue);
			update(parameter);
		} else {
			create(new Parameter(name, integerValue));
		}
	}

	protected final void updateIntegerValue(String name, Float floatValue)
			throws ServiceException, SecurityServiceException {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			parameter.setFloatValue(floatValue);
			update(parameter);
		} else {
			create(new Parameter(name, floatValue));
		}
	}

	protected final void updateStringValue(String name, String stringValue)
			throws ServiceException, SecurityServiceException {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			parameter.setStringValue(stringValue);
			update(parameter);
		} else {
			create(new Parameter(name, stringValue));
		}
	}

	protected final void updateDateValue(String name, Date dateValue)
			throws ServiceException, SecurityServiceException {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			parameter.setDateValue(dateValue);
			update(parameter);
		} else {
			create(new Parameter(name, dateValue));
		}
	}

	protected String getDataUpgradeParameterPrefix() {
		// On permet la surcharge pour les applications pré-existantes.
		return PARAMETER_DATA_UPGRADE_PREFIX_DEFAULT;
	}

	private boolean getParameterMaintenance() {
		return getBooleanValue(PARAMETER_MAINTENANCE, false);
	}
	
	@Override
	public boolean isInMaintenance() {
		return isInMaintenance;
	}

	@Override
	public void setParameterMaintenance(boolean value) throws ServiceException, SecurityServiceException {
		updateBooleanValue(PARAMETER_MAINTENANCE, value);
		isInMaintenance = value;
	}
}
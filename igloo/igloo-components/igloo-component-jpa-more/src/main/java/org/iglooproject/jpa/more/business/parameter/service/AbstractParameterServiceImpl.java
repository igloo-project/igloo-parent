package org.iglooproject.jpa.more.business.parameter.service;

import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.DATABASE_INITIALIZED;
import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.MAINTENANCE;
import static org.iglooproject.jpa.more.property.JpaMorePropertyIds.dataUpgrade;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import org.iglooproject.jpa.business.generic.service.GenericEntityServiceImpl;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.parameter.dao.IParameterDao;
import org.iglooproject.jpa.more.business.parameter.model.Parameter;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.spring.property.service.IPropertyService;

/**
 * @deprecated Parameters are deprecated in favor of properties.
 * @see IPropertyService
 */
@Deprecated
public class AbstractParameterServiceImpl extends GenericEntityServiceImpl<Long, Parameter>
		implements ApplicationListener<ContextRefreshedEvent>, IAbstractParameterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParameterServiceImpl.class);

	@Deprecated
	public static final String PARAMETER_DATA_UPGRADE_PREFIX_DEFAULT = "dataUpgrade.";

	private IParameterDao dao;

	@Autowired
	private IPropertyService propertyService;

	public AbstractParameterServiceImpl(IParameterDao dao) {
		super(dao);
		this.dao = dao;
	}

	/**
	 * @deprecated Use propertyService.get(JpaMorePropertyIds.DATABASE_INITIALIZED)
	 */
	@Deprecated
	@Override
	public boolean isDatabaseInitialized() {
		return propertyService.get(DATABASE_INITIALIZED);
	}

	/**
	 * @deprecated Use propertyService.set(JpaMorePropertyIds.DATABASE_INITIALIZED, value)
	 */
	@Deprecated
	@Override
	public void setDatabaseInitialized(boolean databaseInitialized) throws ServiceException, SecurityServiceException {
		propertyService.set(DATABASE_INITIALIZED, databaseInitialized);
	}

	/**
	 * @deprecated Use propertyService.get(JpaMorePropertyIds.dataUpgrade(upgrade))
	 */
	@Deprecated
	@Override
	public boolean isDataUpgradeDone(IDataUpgrade upgrade) {
		return propertyService.get(dataUpgrade(upgrade));
	}

	/**
	 * @deprecated Use propertyService.set(JpaMorePropertyIds.dataUpgrade(upgrade), value)
	 */
	@Deprecated
	@Override
	public void setDataUpgradeDone(IDataUpgrade upgrade, boolean dataUpgradeDone)
			throws ServiceException, SecurityServiceException {
		propertyService.set(dataUpgrade(upgrade), dataUpgradeDone);
	}

	/**
	 * @deprecated Use propertyService.get(JpaMorePropertyIds.MAINTENANCE)
	 */
	@Deprecated
	@Override
	public boolean isInMaintenance() {
		return propertyService.get(MAINTENANCE);
	}

	/**
	 * @deprecated Use propertyService.set(JpaMorePropertyIds.MAINTENANCE, value)
	 */
	@Deprecated
	@Override
	public void setParameterMaintenance(boolean value) throws ServiceException, SecurityServiceException {
		propertyService.set(MAINTENANCE, value);
	}

	@Deprecated
	protected Parameter getByName(String name) {
		return dao.getByName(name);
	}

	@Deprecated
	protected boolean getBooleanValue(String name, boolean defaultValue) {
		Parameter parameter = getByName(name);
		if (parameter != null && parameter.getBooleanValue() != null) {
			return parameter.getBooleanValue().booleanValue();
		} else {
			return defaultValue;
		}
	}

	@Deprecated
	protected String getStringValue(String name, String defaultValue) {
		Parameter parameter = getByName(name);
		if (parameter != null && parameter.getStringValue() != null) {
			return parameter.getStringValue();
		} else {
			return defaultValue;
		}
	}

	@Deprecated
	protected String getStringValue(String name) {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			return parameter.getStringValue();
		} else {
			return null;
		}
	}

	@Deprecated
	protected int getIntegerValue(String name, int defaultValue) {
		Parameter parameter = getByName(name);
		if (parameter != null && parameter.getIntegerValue() != null) {
			return parameter.getIntegerValue().intValue();
		} else {
			return defaultValue;
		}
	}

	@Deprecated
	protected float getFloatValue(String name, float defaultValue) {
		Parameter parameter = getByName(name);
		if (parameter != null && parameter.getFloatValue() != null) {
			return parameter.getFloatValue().floatValue();
		} else {
			return defaultValue;
		}
	}

	@Deprecated
	protected Date getDateValue(String name) {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			return parameter.getDateValue();
		} else {
			return null;
		}
	}

	@Deprecated
	protected BigDecimal getBigDecimalValue(String name) {
		return getBigDecimalValue(name, null);
	}

	@Deprecated
	protected BigDecimal getBigDecimalValue(String name, BigDecimal defaultValue) {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			try {
				return new BigDecimal(parameter.getStringValue());
			} catch (RuntimeException e) {
				LOGGER.error("Error while retrieving BigDecimal from String", e);
				return null;
			}
		} else {
			return defaultValue;
		}
	}

	@Override
	@Deprecated
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
			} catch (RuntimeException | ServiceException | SecurityServiceException e) {
				LOGGER.error("Unable to load the properties into the database.", e);
			}
		}
	}

	@Deprecated
	protected void doOnApplicationEvent() throws ServiceException, SecurityServiceException {
	}

	@Deprecated
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

	@Deprecated
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

	@Deprecated
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

	@Deprecated
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

	@Deprecated
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

	@Deprecated
	protected final void updateBigDecimalValue(String name, BigDecimal bigDecimalValue)
			throws ServiceException, SecurityServiceException {
		Parameter parameter = getByName(name);
		if (parameter != null) {
			parameter.setStringValue(bigDecimalValue != null ? bigDecimalValue.toString() : null);
			update(parameter);
		} else {
			create(new Parameter(name, bigDecimalValue != null ? bigDecimalValue.toString() : null));
		}
	}

	@Deprecated
	protected String getDataUpgradeParameterPrefix() {
		// On permet la surcharge pour les applications pré-existantes.
		return PARAMETER_DATA_UPGRADE_PREFIX_DEFAULT;
	}

}
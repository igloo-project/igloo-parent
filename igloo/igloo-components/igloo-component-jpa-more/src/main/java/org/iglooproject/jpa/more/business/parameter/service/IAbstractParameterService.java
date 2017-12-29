package org.iglooproject.jpa.more.business.parameter.service;

import org.iglooproject.jpa.business.generic.service.IGenericEntityService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.parameter.model.Parameter;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.spring.property.service.IPropertyService;

/**
 * @deprecated Parameters are deprecated in favor of properties.
 * @see IPropertyService
 */
@Deprecated
public interface IAbstractParameterService extends IGenericEntityService<Long, Parameter>{

	/**
	 * @deprecated Use propertyService.get(JpaMorePropertyIds.DATABASE_INITIALIZED)
	 */
	@Deprecated
	boolean isDatabaseInitialized();

	/**
	 * @deprecated Use propertyService.set(JpaMorePropertyIds.DATABASE_INITIALIZED, value)
	 */
	@Deprecated
	void setDatabaseInitialized(boolean databaseInitialized) throws ServiceException, SecurityServiceException;

	/**
	 * @deprecated Use propertyService.get(JpaMorePropertyIds.dataUpgrade(upgrade))
	 */
	@Deprecated
	boolean isDataUpgradeDone(IDataUpgrade upgrade);

	/**
	 * @deprecated Use propertyService.set(JpaMorePropertyIds.dataUpgrade(upgrade), value)
	 */
	@Deprecated
	void setDataUpgradeDone(IDataUpgrade upgrade, boolean dataUpgradeDone) throws ServiceException,
			SecurityServiceException;

	/**
	 * @deprecated Use propertyService.set(JpaMorePropertyIds.MAINTENANCE, value)
	 */
	@Deprecated
	void setParameterMaintenance(boolean value) throws ServiceException, SecurityServiceException;

	/**
	 * @deprecated Use propertyService.get(JpaMorePropertyIds.MAINTENANCE)
	 */
	@Deprecated
	boolean isInMaintenance();

}

package fr.openwide.core.jpa.more.business.parameter.service;

import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.parameter.model.Parameter;

public interface IAbstractParameterService extends IGenericEntityService<Long, Parameter>{

	boolean isDatabaseInitialized();

	void setDatabaseInitialized(boolean databaseInitialized)
			throws ServiceException, SecurityServiceException;
	
}

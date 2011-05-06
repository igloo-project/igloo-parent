package fr.openwide.core.jpa.more.business.parameter.service;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IAbstractParameterService {

	boolean isDatabaseInitialized();

	void setDatabaseInitialized(boolean databaseInitialized)
			throws ServiceException, SecurityServiceException;
	
}

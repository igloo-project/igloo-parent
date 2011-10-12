package fr.openwide.core.jpa.util;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IDatabaseConsistencyCheckService extends ITransactionalAspectAwareService {

	void checkOnStartup(boolean allowCreateMissingElements) throws ServiceException, SecurityServiceException;

	void checkDatabaseAccess();

	void checkDatabaseConsistency() throws ServiceException, SecurityServiceException;

}

package org.iglooproject.jpa.util;

import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface IDatabaseConsistencyCheckService extends ITransactionalAspectAwareService {

  void checkOnStartup(boolean allowCreateMissingElements)
      throws ServiceException, SecurityServiceException;

  void checkDatabaseAccess();

  void checkDatabaseConsistency() throws ServiceException, SecurityServiceException;
}

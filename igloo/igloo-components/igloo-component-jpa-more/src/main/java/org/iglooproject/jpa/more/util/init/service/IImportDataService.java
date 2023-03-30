package org.iglooproject.jpa.more.util.init.service;

import java.io.IOException;

import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface IImportDataService extends ITransactionalAspectAwareService {

	void importDirectory(String classpathFolder) throws ServiceException, SecurityServiceException, IOException, Exception;

}

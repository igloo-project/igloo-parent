package org.iglooproject.jpa.more.util.init.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public interface IImportDataService extends ITransactionalAspectAwareService {

	void importDirectory(File directory) throws ServiceException, SecurityServiceException,
			FileNotFoundException, IOException ;

}

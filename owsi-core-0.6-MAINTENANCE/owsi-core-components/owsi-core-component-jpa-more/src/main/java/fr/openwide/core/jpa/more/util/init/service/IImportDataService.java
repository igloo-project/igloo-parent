package fr.openwide.core.jpa.more.util.init.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface IImportDataService extends ITransactionalAspectAwareService {

	void importDirectory(File directory) throws ServiceException, SecurityServiceException,
			FileNotFoundException, IOException ;
	
}

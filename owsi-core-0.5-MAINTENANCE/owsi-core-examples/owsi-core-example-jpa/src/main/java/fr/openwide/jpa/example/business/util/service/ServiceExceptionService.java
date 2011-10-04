package fr.openwide.jpa.example.business.util.service;

import fr.openwide.core.jpa.business.generic.service.TransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface ServiceExceptionService extends TransactionalAspectAwareService {

	void dontThrow() throws ServiceException, SecurityServiceException;

	void throwServiceException() throws ServiceException, SecurityServiceException;

	void throwServiceInheritedException() throws ServiceException, SecurityServiceException;

	void throwUncheckedException() throws ServiceException, SecurityServiceException;

	long size();

}

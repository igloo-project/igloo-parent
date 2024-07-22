package org.iglooproject.test.business.util.service;

import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.transaction.CheckedException;

public interface ServiceExceptionService extends ITransactionalAspectAwareService {

  void dontThrow() throws ServiceException, SecurityServiceException;

  void throwServiceException() throws ServiceException, SecurityServiceException;

  void throwServiceInheritedException() throws ServiceException, SecurityServiceException;

  void throwUncheckedException() throws ServiceException, SecurityServiceException;

  void throwCheckedException() throws ServiceException, SecurityServiceException, CheckedException;

  long size();
}

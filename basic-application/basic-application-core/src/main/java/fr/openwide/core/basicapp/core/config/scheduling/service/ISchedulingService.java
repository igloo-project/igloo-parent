package fr.openwide.core.basicapp.core.config.scheduling.service;

import fr.openwide.core.jpa.business.generic.service.ITransactionalAspectAwareService;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface ISchedulingService extends ITransactionalAspectAwareService {

	void temporaryFilesCleaning();

	void executeAutoPerformDataUpgrade() throws ServiceException, SecurityServiceException;
}

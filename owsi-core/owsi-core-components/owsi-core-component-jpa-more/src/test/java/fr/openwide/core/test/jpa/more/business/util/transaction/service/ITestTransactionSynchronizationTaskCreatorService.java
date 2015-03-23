package fr.openwide.core.test.jpa.more.business.util.transaction.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

public interface ITestTransactionSynchronizationTaskCreatorService {

	@Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
	void addBasicTaskTransactionReadOnly();

	void addBasicTaskNoTransaction();

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	void addRollbackBasicTask(Long testEntityId) throws ServiceException, SecurityServiceException;

}

package fr.openwide.core.test.jpa.more.business.util.transaction.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.test.jpa.more.business.util.transaction.model.TestTransactionSynchronizationRollbackBasicTask;


/**
 * Attention, la définition transactionnelle se fait pour chaque méthode.
 */
public interface ITestTransactionSynchronizationTaskService {

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	void basicTask() throws ServiceException, SecurityServiceException;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	void rollbackBasicTask(TestTransactionSynchronizationRollbackBasicTask rollbackBasicTask)
			throws ServiceException, SecurityServiceException;

}

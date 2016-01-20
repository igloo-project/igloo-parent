package fr.openwide.core.test.jpa.more.business.util.transaction.service;

import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;


/**
 * Caution: each method opens a new transaction on purpose.
 */
public interface ITestTransactionSynchronizationTaskService {
	
	GenericEntityReference<Long, TestEntity> createInNewTransaction() throws ServiceException, SecurityServiceException;

	void deleteInNewTransaction(Long testEntityId)
			throws ServiceException, SecurityServiceException;

}

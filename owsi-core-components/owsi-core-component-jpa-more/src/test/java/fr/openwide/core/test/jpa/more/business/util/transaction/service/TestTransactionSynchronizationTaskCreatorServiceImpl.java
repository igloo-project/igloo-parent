package fr.openwide.core.test.jpa.more.business.util.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;
import fr.openwide.core.test.jpa.more.business.entity.service.ITestEntityService;
import fr.openwide.core.test.jpa.more.business.util.transaction.model.TestTransactionSynchronizationBasicTask;
import fr.openwide.core.test.jpa.more.business.util.transaction.model.TestTransactionSynchronizationRollbackBasicTask;

@Service("testTransactionSynchronizationTaskCreatorService")
public class TestTransactionSynchronizationTaskCreatorServiceImpl implements ITestTransactionSynchronizationTaskCreatorService {

	@Autowired
	private ITransactionSynchronizationTaskManagerService transactionSynchronizationTaskManagerService;

	@Autowired
	private ITestEntityService testEntityService;

	@Override
	public void addBasicTaskTransactionReadOnly() {
		transactionSynchronizationTaskManagerService.push(new TestTransactionSynchronizationBasicTask());
	}

	@Override
	public void addBasicTaskNoTransaction() {
		transactionSynchronizationTaskManagerService.push(new TestTransactionSynchronizationBasicTask());
	}

	@Override
	public void addRollbackBasicTask(Long testEntityId) throws ServiceException, SecurityServiceException {
		transactionSynchronizationTaskManagerService.push(new TestTransactionSynchronizationRollbackBasicTask(testEntityId));
		TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
	}

}

package fr.openwide.core.test.jpa.more.business.util.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;
import fr.openwide.core.test.jpa.more.business.entity.service.ITestEntityService;
import fr.openwide.core.test.jpa.more.business.util.transaction.model.TestTransactionSynchronizationRollbackBasicTask;

@Service
public class TestTransactionSynchronizationTaskServiceImpl implements ITestTransactionSynchronizationTaskService {

	@Autowired
	private ITestEntityService testEntityService;

	@Override
	public void basicTask() throws ServiceException, SecurityServiceException {
		TestEntity entity = new TestEntity("entity");
		testEntityService.create(entity);
	}

	@Override
	public void rollbackBasicTask(TestTransactionSynchronizationRollbackBasicTask rollbackBasicTask)
			throws ServiceException, SecurityServiceException {
		TestEntity entity = testEntityService.getById(rollbackBasicTask.getTestEntityId());
		testEntityService.delete(entity);
	}

}

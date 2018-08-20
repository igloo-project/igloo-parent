package org.iglooproject.test.jpa.more.business.util.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.jpa.more.business.entity.model.TestEntity;
import org.iglooproject.test.jpa.more.business.entity.service.ITestEntityService;

@Service
public class TestTransactionSynchronizationTaskServiceImpl implements ITestTransactionSynchronizationTaskService {

	@Autowired
	private ITestEntityService testEntityService;

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public GenericEntityReference<Long, TestEntity> createInNewTransaction() throws ServiceException, SecurityServiceException {
		TestEntity entity = new TestEntity("entity");
		testEntityService.create(entity);
		return GenericEntityReference.of(entity);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteInNewTransaction(Long testEntityId)
			throws ServiceException, SecurityServiceException {
		TestEntity entity = testEntityService.getById(testEntityId);
		testEntityService.delete(entity);
	}

}

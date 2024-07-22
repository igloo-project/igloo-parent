package test.jpa.more.business.util.transaction.service;

import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import test.jpa.more.business.entity.model.TestEntity;

/** Caution: each method opens a new transaction on purpose. */
public interface ITestTransactionSynchronizationTaskService {

  GenericEntityReference<Long, TestEntity> createInNewTransaction()
      throws ServiceException, SecurityServiceException;

  void deleteInNewTransaction(Long testEntityId) throws ServiceException, SecurityServiceException;
}

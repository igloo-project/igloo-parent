package test.jpa.more.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collection;

import org.apache.commons.lang3.mutable.MutableObject;
import org.iglooproject.jpa.batch.executor.BatchExecutorCreator;
import org.iglooproject.jpa.batch.runnable.ReadWriteBatchRunnable;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;
import org.iglooproject.jpa.more.util.transaction.service.TransactionSynchronizationTaskManagerServiceImpl;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import test.jpa.more.business.entity.model.TestEntity;
import test.jpa.more.business.entity.service.ITestEntityService;
import test.jpa.more.business.util.transaction.model.TestCreateAfterCommitTask;
import test.jpa.more.business.util.transaction.model.TestDeleteOnRollbackTask;
import test.jpa.more.business.util.transaction.model.TestUseEntityBeforeCommitOrClearTask;

public class TestTransactionSynchronization extends AbstractJpaMoreTestCase {

	@Autowired
	private ITransactionSynchronizationTaskManagerService transactionSynchronizationTaskManagerService;

	@Autowired
	private ITestEntityService testEntityService;
	
	@Autowired
	private IHibernateSearchService hibernateSearchService;
	
	@Autowired
	private BatchExecutorCreator batchExecutorCreator;

	private TransactionTemplate writeTransactionTemplate;

	private TransactionTemplate writeRequiresNewTransactionTemplate;

	private TransactionTemplate readOnlyTransactionTemplate;
	
	@Autowired
	private void setTransactionTemplate(PlatformTransactionManager transactionManager) {
		DefaultTransactionAttribute writeTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		writeTransactionAttribute.setReadOnly(false);
		writeTransactionTemplate = new TransactionTemplate(transactionManager, writeTransactionAttribute);
		
		DefaultTransactionAttribute writeRequiresNewTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
		writeRequiresNewTransactionAttribute.setReadOnly(false);
		writeRequiresNewTransactionTemplate = new TransactionTemplate(transactionManager, writeRequiresNewTransactionAttribute);
		
		DefaultTransactionAttribute readOnlyTransactionAttribute =
				new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
		readOnlyTransactionAttribute.setReadOnly(true);
		readOnlyTransactionTemplate = new TransactionTemplate(transactionManager, readOnlyTransactionAttribute);
	}

	@Test
	public void testTaskNoTransaction() {
		// Cannot push tasks if there's no transaction
		Assert.assertThrows(
				TransactionSynchronizationTaskManagerServiceImpl.EXCEPTION_MESSAGE_NO_ACTUAL_TRANSACTION_ACTIVE, 
				IllegalStateException.class,
				() -> transactionSynchronizationTaskManagerService.push(new TestCreateAfterCommitTask()));
	}

	@Test
	public void testAfterCommitTask() {
		final TestCreateAfterCommitTask createAfterCommitTask = new TestCreateAfterCommitTask();
		readOnlyTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				transactionSynchronizationTaskManagerService.push(createAfterCommitTask);
			}
		});
		
		entityManagerClear();
		
		Collection<GenericEntityReference<Long, TestEntity>> createdEntities = createAfterCommitTask.getCreatedEntities();
		assertEquals(1, createdEntities.size());
		TestEntity createdEntity = testEntityService.getById(createdEntities.iterator().next());
		assertNotNull(createdEntity);
	}

	@Test
	public void testRollbackTask() throws ServiceException, SecurityServiceException {
		TestEntity entity = new TestEntity("entity");
		testEntityService.create(entity);
		
		final Long testEntityId = entity.getId();
		
		writeTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				transactionSynchronizationTaskManagerService.push(new TestDeleteOnRollbackTask(testEntityId));
				status.setRollbackOnly();
			}
		});
		
		entityManagerClear();
		
		assertNull(testEntityService.getById(testEntityId));
	}

	@Test
	public void testNestedTransactions() throws ServiceException, SecurityServiceException {
		TestEntity entityExpectedToBeDeleted = new TestEntity("entityExpectedToBeDeleted");
		testEntityService.create(entityExpectedToBeDeleted);
		final Long entityExpectedToBeDeletedId = entityExpectedToBeDeleted.getId();
		
		TestEntity entityNOTExpectedToBeDeleted = new TestEntity("entityNOTExpectedToBeDeleted");
		testEntityService.create(entityNOTExpectedToBeDeleted);
		final Long entityNOTExpectedToBeDeletedId = entityNOTExpectedToBeDeleted.getId();
		
		writeTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				// Create a new transaction
				writeRequiresNewTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						// Should not be executed since this transaction will execute just fine
						transactionSynchronizationTaskManagerService.push(new TestDeleteOnRollbackTask(entityNOTExpectedToBeDeletedId));
					}
				});
				
				// Should not trigger the execution of the rollback task declared above, but only the one below
				status.setRollbackOnly();
				
				transactionSynchronizationTaskManagerService.push(new TestDeleteOnRollbackTask(entityExpectedToBeDeletedId));
			}
		});
		
		entityManagerClear();
		
		assertNull(testEntityService.getById(entityExpectedToBeDeletedId));
		assertNotNull(testEntityService.getById(entityNOTExpectedToBeDeletedId));
	}

	@Test
	public void testBeforeCommitOrClearTask() throws ServiceException, SecurityServiceException {
		TestEntity entity = new TestEntity("entity");
		testEntityService.create(entity);
		final Long entityId = entity.getId();
		
		final MutableObject<TestUseEntityBeforeCommitOrClearTask> taskReference = new MutableObject<>();
		
		writeTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				TestEntity reloadedEntity = testEntityService.getById(entityId);

				// This task will fail if executed when the entity is not in the session anymore
				TestUseEntityBeforeCommitOrClearTask task = new TestUseEntityBeforeCommitOrClearTask(reloadedEntity);
				taskReference.setValue(task);
				
				transactionSynchronizationTaskManagerService.push(task);
				
				// Should trigger the task's execution
				transactionSynchronizationTaskManagerService.beforeClear();
				
				entityManagerClear();
			}
		});
		
		entityManagerClear();
		
		assertEquals(1, taskReference.getValue().getExecutionCount());
	}

	@Test
	public void testBeforeCommitOrClearTaskWithRollback() throws ServiceException, SecurityServiceException {
		TestEntity entity = new TestEntity("entity");
		testEntityService.create(entity);
		final Long entityId = entity.getId();
		
		final MutableObject<TestUseEntityBeforeCommitOrClearTask> taskReference = new MutableObject<>();
		
		writeTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				TestEntity reloadedEntity = testEntityService.getById(entityId);

				// This task will fail if executed when the entity is not in the session anymore
				TestUseEntityBeforeCommitOrClearTask task = new TestUseEntityBeforeCommitOrClearTask(reloadedEntity);
				taskReference.setValue(task);
				
				transactionSynchronizationTaskManagerService.push(task);
				
				// Should trigger the task's execution
				transactionSynchronizationTaskManagerService.beforeClear();
				
				entityManagerClear();
				
				status.setRollbackOnly();
			}
		});
		
		entityManagerClear();
		
		assertEquals(1, taskReference.getValue().getExecutionCount());
		assertEquals(1, taskReference.getValue().getRollbackCount());
	}

	@Test
	public void testBeforeCommitOrClearTaskBatchExecutor() throws ServiceException, SecurityServiceException {
		TestEntity entity1 = new TestEntity("entity1");
		testEntityService.create(entity1);
		final Long entityId1 = entity1.getId();
		TestEntity entity2 = new TestEntity("entity2");
		testEntityService.create(entity2);
		final Long entityId2 = entity2.getId();
		
		hibernateSearchService.flushToIndexes();
		
		final Collection<TestUseEntityBeforeCommitOrClearTask> tasks = Lists.newArrayList();
		
		batchExecutorCreator.newSimpleHibernateBatchExecutor().commitOnEnd().batchSize(1)
				.run(TestEntity.class, ImmutableList.of(entityId1, entityId2), new ReadWriteBatchRunnable<TestEntity>() {
					@Override
					protected void executeUnit(TestEntity unit) {
						// This task will fail if executed when the entity is not in the session anymore
						TestUseEntityBeforeCommitOrClearTask task = new TestUseEntityBeforeCommitOrClearTask(unit);
						tasks.add(task);
						
						transactionSynchronizationTaskManagerService.push(task);
						
						// The executor should trigger the task's execution just before each clear
					}
				});
		
		entityManagerClear();
		
		assertEquals(2, tasks.size());
		for (TestUseEntityBeforeCommitOrClearTask task : tasks) {
			assertEquals(1, task.getExecutionCount());
		}
	}
}

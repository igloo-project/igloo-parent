package test.jpa.more.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.google.common.collect.Lists;
import igloo.jpa.batch.executor.BatchExecutorCreator;
import igloo.jpa.batch.runnable.ReadWriteBatchRunnable;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableObject;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;
import org.iglooproject.jpa.more.util.transaction.service.TransactionSynchronizationTaskManagerServiceImpl;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;
import test.jpa.more.business.entity.model.TestEntity;
import test.jpa.more.business.entity.service.ITestEntityService;
import test.jpa.more.business.util.transaction.model.TestCreateAfterCommitTask;
import test.jpa.more.business.util.transaction.model.TestDeleteOnRollbackTask;
import test.jpa.more.business.util.transaction.model.TestUseEntityBeforeCommitOrClearTask;
import test.jpa.more.config.spring.SpringBootTestJpaMoreBatchSearch;

@SpringBootTestJpaMoreBatchSearch
class TestTransactionSynchronization extends AbstractJpaMoreTestCase {

  @Autowired
  private ITransactionSynchronizationTaskManagerService
      transactionSynchronizationTaskManagerService;

  @Autowired private ITestEntityService testEntityService;

  @Autowired private IHibernateSearchService hibernateSearchService;

  @Autowired private BatchExecutorCreator batchExecutorCreator;

  private TransactionTemplate writeTransactionTemplate;

  private TransactionTemplate writeRequiresNewTransactionTemplate;

  private TransactionTemplate readOnlyTransactionTemplate;

  @Autowired
  private void setTransactionTemplate(PlatformTransactionManager transactionManager) {
    DefaultTransactionAttribute writeTransactionAttribute =
        new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
    writeTransactionAttribute.setReadOnly(false);
    writeTransactionTemplate =
        new TransactionTemplate(transactionManager, writeTransactionAttribute);

    DefaultTransactionAttribute writeRequiresNewTransactionAttribute =
        new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRES_NEW);
    writeRequiresNewTransactionAttribute.setReadOnly(false);
    writeRequiresNewTransactionTemplate =
        new TransactionTemplate(transactionManager, writeRequiresNewTransactionAttribute);

    DefaultTransactionAttribute readOnlyTransactionAttribute =
        new DefaultTransactionAttribute(TransactionAttribute.PROPAGATION_REQUIRED);
    readOnlyTransactionAttribute.setReadOnly(true);
    readOnlyTransactionTemplate =
        new TransactionTemplate(transactionManager, readOnlyTransactionAttribute);
  }

  @Test
  void testTaskNoTransaction() {
    // Cannot push tasks if there's no transaction
    assertThatCode(
            () ->
                transactionSynchronizationTaskManagerService.push(new TestCreateAfterCommitTask()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage(
            TransactionSynchronizationTaskManagerServiceImpl
                .EXCEPTION_MESSAGE_NO_ACTUAL_TRANSACTION_ACTIVE);
  }

  @Test
  void testAfterCommitTask() {
    final TestCreateAfterCommitTask createAfterCommitTask = new TestCreateAfterCommitTask();
    readOnlyTransactionTemplate.executeWithoutResult(
        status -> transactionSynchronizationTaskManagerService.push(createAfterCommitTask));

    entityManagerClear();

    Collection<GenericEntityReference<Long, TestEntity>> createdEntities =
        createAfterCommitTask.getCreatedEntities();
    assertThat(createdEntities).hasSize(1);
    TestEntity createdEntity = testEntityService.getById(createdEntities.iterator().next());
    assertThat(createdEntity).isNotNull();
  }

  @Test
  void testRollbackTask() throws ServiceException, SecurityServiceException {
    TestEntity entity = new TestEntity("entity");
    testEntityService.create(entity);

    final Long testEntityId = entity.getId();

    writeTransactionTemplate.executeWithoutResult(
        status -> {
          transactionSynchronizationTaskManagerService.push(
              new TestDeleteOnRollbackTask(testEntityId));
          status.setRollbackOnly();
        });

    entityManagerClear();

    assertThat(testEntityService.getById(testEntityId)).isNull();
  }

  @Test
  void testNestedTransactions() throws ServiceException, SecurityServiceException {
    TestEntity entityExpectedToBeDeleted = new TestEntity("entityExpectedToBeDeleted");
    testEntityService.create(entityExpectedToBeDeleted);
    final Long entityExpectedToBeDeletedId = entityExpectedToBeDeleted.getId();

    TestEntity entityNOTExpectedToBeDeleted = new TestEntity("entityNOTExpectedToBeDeleted");
    testEntityService.create(entityNOTExpectedToBeDeleted);
    final Long entityNOTExpectedToBeDeletedId = entityNOTExpectedToBeDeleted.getId();

    writeTransactionTemplate.executeWithoutResult(
        status -> {
          // Create a new transaction
          writeRequiresNewTransactionTemplate.executeWithoutResult(
              status1 -> {
                // Should not be executed since this transaction will execute just fine
                transactionSynchronizationTaskManagerService.push(
                    new TestDeleteOnRollbackTask(entityNOTExpectedToBeDeletedId));
              });

          // Should not trigger the execution of the rollback task declared above, but only the
          // one below
          status.setRollbackOnly();

          transactionSynchronizationTaskManagerService.push(
              new TestDeleteOnRollbackTask(entityExpectedToBeDeletedId));
        });

    entityManagerClear();

    assertThat(testEntityService.getById(entityExpectedToBeDeletedId)).isNull();
    assertThat(testEntityService.getById(entityNOTExpectedToBeDeletedId)).isNotNull();
  }

  @Test
  void testBeforeCommitOrClearTask() throws ServiceException, SecurityServiceException {
    TestEntity entity = new TestEntity("entity");
    testEntityService.create(entity);
    final Long entityId = entity.getId();

    final MutableObject<TestUseEntityBeforeCommitOrClearTask> taskReference = new MutableObject<>();

    writeTransactionTemplate.executeWithoutResult(
        status -> {
          TestEntity reloadedEntity = testEntityService.getById(entityId);

          // This task will fail if executed when the entity is not in the session anymore
          TestUseEntityBeforeCommitOrClearTask task =
              new TestUseEntityBeforeCommitOrClearTask(reloadedEntity);
          taskReference.setValue(task);

          transactionSynchronizationTaskManagerService.push(task);

          // Should trigger the task's execution
          transactionSynchronizationTaskManagerService.beforeClear();

          entityManagerClear();
        });

    entityManagerClear();

    assertThat(taskReference.get().getExecutionCount()).isEqualTo(1);
  }

  @Test
  void testBeforeCommitOrClearTaskWithRollback() throws ServiceException, SecurityServiceException {
    TestEntity entity = new TestEntity("entity");
    testEntityService.create(entity);
    final Long entityId = entity.getId();

    final MutableObject<TestUseEntityBeforeCommitOrClearTask> taskReference = new MutableObject<>();

    writeTransactionTemplate.executeWithoutResult(
        status -> {
          TestEntity reloadedEntity = testEntityService.getById(entityId);

          // This task will fail if executed when the entity is not in the session anymore
          TestUseEntityBeforeCommitOrClearTask task =
              new TestUseEntityBeforeCommitOrClearTask(reloadedEntity);
          taskReference.setValue(task);

          transactionSynchronizationTaskManagerService.push(task);

          // Should trigger the task's execution
          transactionSynchronizationTaskManagerService.beforeClear();

          entityManagerClear();

          status.setRollbackOnly();
        });

    entityManagerClear();

    assertThat(taskReference.get().getExecutionCount()).isEqualTo(1);
    assertThat(taskReference.get().getRollbackCount()).isEqualTo(1);
  }

  @Test
  void testBeforeCommitOrClearTaskBatchExecutor()
      throws ServiceException, SecurityServiceException {
    TestEntity entity1 = new TestEntity("entity1");
    testEntityService.create(entity1);
    final Long entityId1 = entity1.getId();
    TestEntity entity2 = new TestEntity("entity2");
    testEntityService.create(entity2);
    final Long entityId2 = entity2.getId();

    hibernateSearchService.flushToIndexes();

    final Collection<TestUseEntityBeforeCommitOrClearTask> tasks = Lists.newArrayList();

    batchExecutorCreator
        .newSimpleHibernateBatchExecutor()
        .commitOnEnd()
        .batchSize(1)
        .run(
            TestEntity.class,
            List.of(entityId1, entityId2),
            new ReadWriteBatchRunnable<TestEntity>() {
              @Override
              protected void executeUnit(TestEntity unit) {
                // This task will fail if executed when the entity is not in the session anymore
                TestUseEntityBeforeCommitOrClearTask task =
                    new TestUseEntityBeforeCommitOrClearTask(unit);
                tasks.add(task);

                transactionSynchronizationTaskManagerService.push(task);

                // The executor should trigger the task's execution just before each clear
              }
            });

    entityManagerClear();

    assertThat(tasks).hasSize(2);
    for (TestUseEntityBeforeCommitOrClearTask task : tasks) {
      assertThat(task.getExecutionCount()).isEqualTo(1);
    }
  }
}

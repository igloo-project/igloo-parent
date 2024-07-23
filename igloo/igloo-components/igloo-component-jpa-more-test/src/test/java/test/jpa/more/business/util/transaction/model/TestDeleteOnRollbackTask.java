package test.jpa.more.business.util.transaction.model;

import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import org.iglooproject.jpa.more.util.transaction.model.ITransactionSynchronizationTaskRollbackAware;

public class TestDeleteOnRollbackTask extends TestAbstractTransactionSynchronizationTask
    implements ITransactionSynchronizationAfterCommitTask,
        ITransactionSynchronizationTaskRollbackAware {

  private static final long serialVersionUID = 20642307623916853L;

  private Long testEntityId;

  public TestDeleteOnRollbackTask(Long testEntityId) {
    super();
    this.testEntityId = testEntityId;
  }

  @Override
  public void run() throws Exception {
    // nothing to do
  }

  @Override
  public void afterRollback() throws Exception {
    transactionSynchronizationTaskService.deleteInNewTransaction(testEntityId);
  }

  public Long getTestEntityId() {
    return testEntityId;
  }

  public void setTestEntityId(Long testEntityId) {
    this.testEntityId = testEntityId;
  }
}

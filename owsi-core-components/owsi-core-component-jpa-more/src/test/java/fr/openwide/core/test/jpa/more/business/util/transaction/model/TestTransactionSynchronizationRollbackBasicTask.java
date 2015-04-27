package fr.openwide.core.test.jpa.more.business.util.transaction.model;

import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationTaskRollbackAware;

public class TestTransactionSynchronizationRollbackBasicTask extends TestAbstractTransactionSynchronizationTask
		implements ITransactionSynchronizationAfterCommitTask, ITransactionSynchronizationTaskRollbackAware {

	private static final long serialVersionUID = 20642307623916853L;

	private Long testEntityId;

	public TestTransactionSynchronizationRollbackBasicTask(Long testEntityId) {
		super();
		this.testEntityId = testEntityId;
	}

	@Override
	public void run() throws Exception {
		// nothing to do
	}

	@Override
	public void afterRollback() throws Exception {
		transactionSynchronizationTaskService.rollbackBasicTask(this);
	}

	public Long getTestEntityId() {
		return testEntityId;
	}

	public void setTestEntityId(Long testEntityId) {
		this.testEntityId = testEntityId;
	}

}

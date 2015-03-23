package fr.openwide.core.test.jpa.more.business.util.transaction.model;

import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchornizationAfterCommitTask;

public class TestTransactionSynchronizationBasicTask extends
		TestAbstractTransactionSynchronizationTask<TestTransactionSynchronizationBasicTask> implements
		ITransactionSynchornizationAfterCommitTask<TestTransactionSynchronizationBasicTask> {

	private static final long serialVersionUID = 20642307623916853L;

	@Override
	public void run() throws Exception {
		transactionSynchronizationTaskService.basicTask();
	}

}

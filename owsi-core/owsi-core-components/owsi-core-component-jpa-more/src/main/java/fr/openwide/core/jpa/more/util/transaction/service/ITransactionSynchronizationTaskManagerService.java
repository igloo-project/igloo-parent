package fr.openwide.core.jpa.more.util.transaction.service;

import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationTaskRollbackAware;

public interface ITransactionSynchronizationTaskManagerService {

	/**
	 * Push a task to be executed <strong>before</strong> commit.
	 * <p>The task may optionally implement {@link ITransactionSynchronizationTaskRollbackAware},
	 * in which case the relevant methods will be called if the appropriate circumstances arise.
	 */
	void push(ITransactionSynchronizationBeforeCommitTask beforeCommitTask);

	/**
	 * Push a task to be executed <strong>after</strong> commit.
	 * <p>The task may optionally implement {@link ITransactionSynchronizationTaskRollbackAware},
	 * in which case the relevant methods will be called if the appropriate circumstances arise.
	 */
	void push(ITransactionSynchronizationAfterCommitTask afterCommitTask);
}
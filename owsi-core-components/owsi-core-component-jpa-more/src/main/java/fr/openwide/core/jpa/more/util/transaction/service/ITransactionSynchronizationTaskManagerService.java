package fr.openwide.core.jpa.more.util.transaction.service;

import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.TransactionSynchronizationTasks;

public interface ITransactionSynchronizationTaskManagerService {

	TransactionSynchronizationTasks getTasks();

	void push(ITransactionSynchronizationBeforeCommitTask beforeCommitTask);

	void push(ITransactionSynchronizationAfterCommitTask afterCommitTask);

	void clean();

	void merge();
}
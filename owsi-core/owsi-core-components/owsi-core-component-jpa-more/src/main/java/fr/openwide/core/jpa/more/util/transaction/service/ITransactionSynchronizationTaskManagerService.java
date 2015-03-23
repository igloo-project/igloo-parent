package fr.openwide.core.jpa.more.util.transaction.service;

import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchornizationAfterCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchornizationBeforeCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.TransactionSynchronizationTasks;

public interface ITransactionSynchronizationTaskManagerService {

	TransactionSynchronizationTasks getTasks();

	void push(ITransactionSynchornizationBeforeCommitTask<?> beforeCommitTask);

	void push(ITransactionSynchornizationAfterCommitTask<?> afterCommitTask);

	void clean();

	void merge();
}
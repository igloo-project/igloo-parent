package fr.openwide.core.jpa.more.util.transaction.service;

import fr.openwide.core.jpa.more.util.transaction.model.TransactionSynchronizationTasks;

public interface ITransactionSynchronizationTaskMergerService {

	void merge(TransactionSynchronizationTasks tasks);

}

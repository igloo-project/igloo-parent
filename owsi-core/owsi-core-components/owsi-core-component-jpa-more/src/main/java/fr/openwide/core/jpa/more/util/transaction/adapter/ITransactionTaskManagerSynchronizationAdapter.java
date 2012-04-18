package fr.openwide.core.jpa.more.util.transaction.adapter;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import fr.openwide.core.jpa.more.util.transaction.model.IAfterCommitOperation;

public interface ITransactionTaskManagerSynchronizationAdapter {

	void addAfterCommitOperation(IAfterCommitOperation afterCommitOperation);

	TransactionSynchronizationAdapter getTransactionSynchronizationAdapter();
}

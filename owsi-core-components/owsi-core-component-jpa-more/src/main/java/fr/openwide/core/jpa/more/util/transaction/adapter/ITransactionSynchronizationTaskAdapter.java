package fr.openwide.core.jpa.more.util.transaction.adapter;

import org.springframework.transaction.support.TransactionSynchronizationAdapter;

public interface ITransactionSynchronizationTaskAdapter {

	TransactionSynchronizationAdapter getTransactionSynchronizationAdapter();

}

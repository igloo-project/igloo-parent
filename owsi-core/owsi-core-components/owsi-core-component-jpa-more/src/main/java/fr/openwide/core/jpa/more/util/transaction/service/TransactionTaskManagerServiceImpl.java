package fr.openwide.core.jpa.more.util.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import fr.openwide.core.jpa.more.util.transaction.adapter.ITransactionTaskManagerSynchronizationAdapter;
import fr.openwide.core.jpa.more.util.transaction.model.IAfterCommitOperation;

@Service("transactionTaskManagerService")
public class TransactionTaskManagerServiceImpl implements ITransactionTaskManagerService {

	@Autowired
	private ITransactionTaskManagerSynchronizationAdapter afterCommitTaskTransactionStatusProvider;

	@Override
	public void addAfterCommitOperation(IAfterCommitOperation afterCommitOperation) {
		registerTransactionSynchronization();
		afterCommitTaskTransactionStatusProvider.addAfterCommitOperation(afterCommitOperation);
	}

	protected void registerTransactionSynchronization() {
		if (TransactionSynchronizationManager.isSynchronizationActive()
				&& !TransactionSynchronizationManager.getSynchronizations().contains(afterCommitTaskTransactionStatusProvider)) {
			TransactionSynchronizationManager.registerSynchronization(afterCommitTaskTransactionStatusProvider
					.getTransactionSynchronizationAdapter());
		}
	}
}

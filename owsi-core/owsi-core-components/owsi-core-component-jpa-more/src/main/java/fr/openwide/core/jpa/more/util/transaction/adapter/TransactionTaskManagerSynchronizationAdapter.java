package fr.openwide.core.jpa.more.util.transaction.adapter;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import fr.openwide.core.jpa.more.util.transaction.model.IAfterCommitOperation;

@Component("transactionTaskManagerSynchronizationAdapter")
public class TransactionTaskManagerSynchronizationAdapter extends TransactionSynchronizationAdapter
		implements ITransactionTaskManagerSynchronizationAdapter {

	private final static Logger LOGGER = LoggerFactory.getLogger(TransactionTaskManagerSynchronizationAdapter.class);

	private ThreadLocal<List<IAfterCommitOperation>> afterCommitOperations;

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public void afterCommit() {
		List<IAfterCommitOperation> operations = this.afterCommitOperations.get();
		for (IAfterCommitOperation operation : operations) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(operation.toString());
			}
			
			operation.run();
		}
	}

	@Override
	public void afterCompletion(int status) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Purge de la liste des tâches après commit.");
			if (TransactionSynchronization.STATUS_ROLLED_BACK == status) {
				// TODO: détail des mark supprimés qui sont ignorés (car rollback)
			}
		}
		afterCommitOperations.get().clear();
		afterCommitOperations.remove();
	}

	@Override
	public void addAfterCommitOperation(IAfterCommitOperation afterCommitOperation) {
		getAfterCommitOperations().add(afterCommitOperation);
	}

	@Override
	public TransactionSynchronizationAdapter getTransactionSynchronizationAdapter() {
		return this;
	}

	private List<IAfterCommitOperation> getAfterCommitOperations() {
		if (this.afterCommitOperations == null) {
			this.afterCommitOperations = new ThreadLocal<List<IAfterCommitOperation>>();
		}
		
		List<IAfterCommitOperation> operations = this.afterCommitOperations.get();
		if (operations ==  null) {
			this.afterCommitOperations.set(new LinkedList<IAfterCommitOperation>());
			operations = this.afterCommitOperations.get();
		}
		return operations;
	}
}

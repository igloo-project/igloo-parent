package fr.openwide.core.jpa.more.util.transaction.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import com.google.common.collect.Iterables;

import fr.openwide.core.jpa.more.util.transaction.exception.TransactionSynchronizationException;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationTaskRollbackAware;
import fr.openwide.core.jpa.more.util.transaction.model.TransactionSynchronizationTasks;
import fr.openwide.core.jpa.more.util.transaction.service.ITransactionSynchronizationTaskManagerService;

@Component
public class TransactionSynchronizationTaskAdapter extends TransactionSynchronizationAdapter implements
		ITransactionSynchronizationTaskAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionSynchronizationTaskAdapter.class);

	@Autowired
	private ITransactionSynchronizationTaskManagerService transactionSynchronizationTaskManagerService;

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Override
	public void beforeCommit(boolean readOnly) {
		transactionSynchronizationTaskManagerService.merge();
		
		TransactionSynchronizationTasks tasks = transactionSynchronizationTaskManagerService.getTasks();
		for (ITransactionSynchronizationBeforeCommitTask beforeCommitTask : tasks.getBeforeCommitTasks()) {
			try {
				beforeCommitTask.run();
			} catch (Exception e) {
				// This exception MUST be thrown, as we want to rollback if anything goes wrong.
				// We better ignore other tasks, as they will have no effect on the transaction.
				throw new TransactionSynchronizationException("Error while executing a 'before commit' task.", e);
			}
		}
	}

	@Override
	public void afterCommit() {
		TransactionSynchronizationTasks tasks = transactionSynchronizationTaskManagerService.getTasks();
		Exception firstException = null;
		for (ITransactionSynchronizationAfterCommitTask afterCommitTask : tasks.getAfterCommitTasks()) {
			try {
				afterCommitTask.run();
			} catch (Exception e) {
				if (firstException == null) {
					firstException = e;
				} else {
					LOGGER.error("Multiple exceptions while executing 'after commit' tasks. Only the first exception has been propagated.", e);
				}
			}
		}
		if (firstException != null) {
			// We better throw an exception here, as we want the caller to know something went awry.
			throw new TransactionSynchronizationException("Error while executing an 'after commit' task.", firstException);
		}
	}

	@Override
	public void afterCompletion(int status) {
		if (TransactionSynchronization.STATUS_ROLLED_BACK == status) {
			Exception firstException = null;
			TransactionSynchronizationTasks tasks = transactionSynchronizationTaskManagerService.getTasks();
			for (ITransactionSynchronizationTaskRollbackAware beforeCommitTask : Iterables.filter(tasks.getBeforeCommitTasks(), ITransactionSynchronizationTaskRollbackAware.class)) {
				try {
					((ITransactionSynchronizationTaskRollbackAware) beforeCommitTask).afterRollback();
				} catch (Exception e) {
					if (firstException == null) {
						firstException = e;
					} else {
						LOGGER.error("Multiple exceptions while executing afterRollback() on synchronization tasks. Only the first exception has been propagated.", e);
					}
				}
			}
			for (ITransactionSynchronizationTaskRollbackAware afterCommitTask : Iterables.filter(tasks.getAfterCommitTasks(), ITransactionSynchronizationTaskRollbackAware.class)) {
				try {
					((ITransactionSynchronizationTaskRollbackAware) afterCommitTask).afterRollback();
				} catch (Exception e) {
					if (firstException == null) {
						firstException = e;
					} else {
						LOGGER.error("Multiple exceptions while executing afterRollback() on synchronization tasks. Only the first exception has been propagated.", e);
					}
				}
			}
			if (firstException != null) {
				// We better throw an exception here, as we want the caller to know something went awry.
				throw new TransactionSynchronizationException(
						"Error while executing afterRollback() on a synchronization task.", firstException
				);
			}
		}
		transactionSynchronizationTaskManagerService.clean();
	}

	@Override
	public TransactionSynchronizationAdapter getTransactionSynchronizationAdapter() {
		return this;
	}
}

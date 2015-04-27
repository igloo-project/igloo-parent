package fr.openwide.core.jpa.more.util.transaction.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;

import com.google.common.collect.Iterables;

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
				LOGGER.error("Error while executing a 'before commit' task .", e);
			}
		}
	}

	@Override
	public void afterCommit() {
		TransactionSynchronizationTasks tasks = transactionSynchronizationTaskManagerService.getTasks();
		for (ITransactionSynchronizationAfterCommitTask afterCommitTask : tasks.getAfterCommitTasks()) {
			try {
				afterCommitTask.run();
			} catch (Exception e) {
				LOGGER.error("Error while executing an 'after commit' task.", e);
			}
		}
	}

	@Override
	public void afterCompletion(int status) {
		if (TransactionSynchronization.STATUS_ROLLED_BACK == status) {
			TransactionSynchronizationTasks tasks = transactionSynchronizationTaskManagerService.getTasks();
			for (ITransactionSynchronizationTaskRollbackAware beforeCommitTask : Iterables.filter(tasks.getBeforeCommitTasks(), ITransactionSynchronizationTaskRollbackAware.class)) {
				try {
					((ITransactionSynchronizationTaskRollbackAware) beforeCommitTask).afterRollback();
				} catch (Exception e) {
					LOGGER.error("Error while executing the rollback from a 'before commit' task.", e);
				}
			}
			for (ITransactionSynchronizationTaskRollbackAware afterCommitTask : Iterables.filter(tasks.getAfterCommitTasks(), ITransactionSynchronizationTaskRollbackAware.class)) {
				try {
					((ITransactionSynchronizationTaskRollbackAware) afterCommitTask).afterRollback();
				} catch (Exception e) {
					LOGGER.error("Error while executing the rollback from an 'after commit' task.", e);
				}
			}
		}
		transactionSynchronizationTaskManagerService.clean();
	}

	@Override
	public TransactionSynchronizationAdapter getTransactionSynchronizationAdapter() {
		return this;
	}
}

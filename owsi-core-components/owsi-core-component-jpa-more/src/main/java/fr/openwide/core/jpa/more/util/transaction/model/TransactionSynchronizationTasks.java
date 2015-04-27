package fr.openwide.core.jpa.more.util.transaction.model;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

public class TransactionSynchronizationTasks {

	private final List<ITransactionSynchronizationBeforeCommitTask> beforeCommitTasks = Lists.newLinkedList();

	private final List<ITransactionSynchronizationAfterCommitTask> afterCommitTasks = Lists.newLinkedList();

	private TransactionSynchronizationTasksStatus status = TransactionSynchronizationTasksStatus.OPEN;

	public void reset() {
		beforeCommitTasks.clear();
		afterCommitTasks.clear();
		open();
	}

	public List<ITransactionSynchronizationBeforeCommitTask> getBeforeCommitTasks() {
		if (isLocked()) {
			return Collections.unmodifiableList(beforeCommitTasks);
		}
		return beforeCommitTasks;
	}

	public List<ITransactionSynchronizationAfterCommitTask> getAfterCommitTasks() {
		if (isLocked()) {
			return Collections.unmodifiableList(afterCommitTasks);
		}
		return afterCommitTasks;
	}

	public TransactionSynchronizationTasks lock() {
		status = TransactionSynchronizationTasksStatus.LOCKED;
		return this;
	}

	public TransactionSynchronizationTasks open() {
		status = TransactionSynchronizationTasksStatus.OPEN;
		return this;
	}

	public boolean isLocked() {
		return TransactionSynchronizationTasksStatus.LOCKED.equals(status);
	}

	public enum TransactionSynchronizationTasksStatus {
		LOCKED,
		OPEN;
	}

}

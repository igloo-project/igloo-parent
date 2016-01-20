package fr.openwide.core.jpa.more.util.transaction.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.google.common.collect.Iterables;

import fr.openwide.core.jpa.more.util.transaction.exception.TransactionSynchronizationException;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationTaskRollbackAware;
import fr.openwide.core.jpa.more.util.transaction.model.TransactionSynchronizationTasks;

@Service
public class TransactionSynchronizationTaskManagerServiceImpl
		implements ITransactionSynchronizationTaskManagerService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionSynchronizationTaskManagerServiceImpl.class);

	private static final Class<?> TASKS_RESOURCE_KEY = TransactionSynchronizationTaskManagerServiceImpl.class;

	public static final String EXCEPTION_MESSAGE_NO_ACTUAL_TRANSACTION_ACTIVE = "No actual transaction active.";

	@Autowired(required = false)
	private ITransactionSynchronizationTaskMergerService transactionSynchronizationTaskMergerService;

	@Autowired
	private ConfigurableApplicationContext configurableApplicationContext;

	@Override
	public void push(ITransactionSynchronizationBeforeCommitTask beforeCommitTask) {
		addSynchronizationIfNeeded();
		
		autowireAndInitialize(beforeCommitTask);
		getTasksIfExist().getBeforeCommitTasks().add(beforeCommitTask);
	}
	 
	@Override
	public void push(ITransactionSynchronizationAfterCommitTask afterCommitTask) {
		addSynchronizationIfNeeded();
		
		autowireAndInitialize(afterCommitTask);
		getTasksIfExist().getAfterCommitTasks().add(afterCommitTask);
	}

	protected void autowireAndInitialize(ITransactionSynchronizationTask beforeCommitTask) {
		AutowireCapableBeanFactory autowireCapableBeanFactory = configurableApplicationContext.getAutowireCapableBeanFactory();
		autowireCapableBeanFactory.autowireBean(beforeCommitTask);
		autowireCapableBeanFactory.initializeBean(beforeCommitTask, beforeCommitTask.getClass().getName());
	}

	protected void addSynchronizationIfNeeded() {
		if (!TransactionSynchronizationManager.isActualTransactionActive()) {
			throw new IllegalStateException(EXCEPTION_MESSAGE_NO_ACTUAL_TRANSACTION_ACTIVE);
		}
		if (!isTaskSynchronizationActive()) {
			// Initialize the list of tasks that will be executed later
			bindContext(new TransactionSynchronizationTasks());
			
			// Register the synchronization adapter (that will transmit events to this service)
			TransactionSynchronizationManager.registerSynchronization(new TaskTransactionSynchronizationAdapter());
		}
	}

	protected boolean isTaskSynchronizationActive() {
		return TransactionSynchronizationManager.hasResource(TASKS_RESOURCE_KEY);
	}

	protected void bindContext(TransactionSynchronizationTasks tasks) {
		TransactionSynchronizationManager.bindResource(TASKS_RESOURCE_KEY, tasks);
	}

	protected TransactionSynchronizationTasks unbindContext() {
		return (TransactionSynchronizationTasks) TransactionSynchronizationManager.unbindResourceIfPossible(TASKS_RESOURCE_KEY);
	}

	private TransactionSynchronizationTasks getTasksIfExist() {
		return (TransactionSynchronizationTasks) TransactionSynchronizationManager.getResource(TASKS_RESOURCE_KEY);
	}

	protected void merge() {
		if (transactionSynchronizationTaskMergerService == null) {
			return;
		}
		TransactionSynchronizationTasks operations = getTasksIfExist();
		transactionSynchronizationTaskMergerService.merge(operations);
		operations.lock();
	}
	
	private void doBeforeCommit() {
		merge();
		
		TransactionSynchronizationTasks tasks = getTasksIfExist();
		if (tasks == null) {
			return;
		}
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
	
	private void doAfterCommit() {
		TransactionSynchronizationTasks tasks = getTasksIfExist();
		if (tasks == null) {
			return;
		}
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
	
	private void doOnRollback() {
		Exception firstException = null;
		TransactionSynchronizationTasks tasks = getTasksIfExist();
		if (tasks == null) {
			return;
		}
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
	
	public class TaskTransactionSynchronizationAdapter extends TransactionSynchronizationAdapter {
		
		private TransactionSynchronizationTasks suspendedTasks = null;
		
		@Override
		public int getOrder() {
			return Ordered.LOWEST_PRECEDENCE;
		}
		
		@Override
		public void suspend() {
			suspendedTasks = unbindContext();
		}
		
		@Override
		public void resume() {
			bindContext(suspendedTasks);
		}
		
		@Override
		public void beforeCommit(boolean readOnly) {
			doBeforeCommit();
		}
		
		@Override
		public void afterCommit() {
			doAfterCommit();
		}
		
		@Override
		public void afterCompletion(int status) {
			if (TransactionSynchronization.STATUS_ROLLED_BACK == status) {
				doOnRollback();
			}
			unbindContext();
		}
	}
}

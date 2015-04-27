package fr.openwide.core.jpa.more.util.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import fr.openwide.core.jpa.more.util.transaction.adapter.ITransactionSynchronizationTaskAdapter;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationAfterCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationBeforeCommitTask;
import fr.openwide.core.jpa.more.util.transaction.model.ITransactionSynchronizationTask;
import fr.openwide.core.jpa.more.util.transaction.model.TransactionSynchronizationTasks;

@Service
public class TransactionSynchronizationTaskManagerServiceImpl implements ITransactionSynchronizationTaskManagerService {

	public static final String EXCEPTION_MESSAGE_NO_ACTUAL_TRANSACTION_ACTIVE = "No actual transaction active.";

	@Autowired
	private ITransactionSynchronizationTaskAdapter transactionSynchronizationTaskAdapter;

	@Autowired(required = false)
	private ITransactionSynchronizationTaskMergerService transactionSynchronizationTaskMergerService;

	@Autowired
	private ConfigurableApplicationContext configurableApplicationContext;

	private static final Class<?> TASKS_RESOURCE_KEY = TransactionSynchronizationTaskManagerServiceImpl.class;

	@Override
	public TransactionSynchronizationTasks getTasks() {
		return (TransactionSynchronizationTasks) TransactionSynchronizationManager.getResource(TASKS_RESOURCE_KEY);
	}

	@Override
	public void push(ITransactionSynchronizationBeforeCommitTask beforeCommitTask) {
		checkTransactionActive();
		registerTransactionSynchronization();
		autowire(beforeCommitTask);
		bindResource().getBeforeCommitTasks().add(beforeCommitTask);
	}
	 
	@Override
	public void push(ITransactionSynchronizationAfterCommitTask afterCommitTask) {
		checkTransactionActive();
		registerTransactionSynchronization();
		autowire(afterCommitTask);
		bindResource().getAfterCommitTasks().add(afterCommitTask);
	}

	private void checkTransactionActive() {
		if (!TransactionSynchronizationManager.isActualTransactionActive()) {
			throw new IllegalStateException(EXCEPTION_MESSAGE_NO_ACTUAL_TRANSACTION_ACTIVE);
		}
	}

	protected void autowire(ITransactionSynchronizationTask beforeCommitTask) {
		AutowireCapableBeanFactory autowireCapableBeanFactory = configurableApplicationContext.getAutowireCapableBeanFactory();
		autowireCapableBeanFactory.autowireBean(beforeCommitTask);
		autowireCapableBeanFactory.initializeBean(beforeCommitTask, beforeCommitTask.getClass().getName());
	}

	protected void registerTransactionSynchronization() {
		if (TransactionSynchronizationManager.isSynchronizationActive()
				&& !TransactionSynchronizationManager.getSynchronizations().contains(transactionSynchronizationTaskAdapter)) {
			TransactionSynchronizationManager.registerSynchronization(transactionSynchronizationTaskAdapter
					.getTransactionSynchronizationAdapter());
		}
	}

	protected TransactionSynchronizationTasks bindResource() {
		TransactionSynchronizationTasks operations = getTasks();
		if (operations == null) {
			operations = new TransactionSynchronizationTasks();
			TransactionSynchronizationManager.bindResource(TASKS_RESOURCE_KEY, operations);
		}
		return operations;
	}

	@Override
	public void clean() {
		TransactionSynchronizationTasks tasks = getTasks();
		if (tasks != null) {
			tasks.reset();
		}
		TransactionSynchronizationManager.unbindResourceIfPossible(TASKS_RESOURCE_KEY);
	}

	@Override
	public void merge() {
		if (transactionSynchronizationTaskMergerService == null) {
			return;
		}
		TransactionSynchronizationTasks operations = getTasks();
		transactionSynchronizationTaskMergerService.merge(operations);
		operations.lock();
	}
}

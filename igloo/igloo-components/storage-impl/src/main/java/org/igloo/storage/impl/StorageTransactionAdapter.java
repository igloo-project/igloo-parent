package org.igloo.storage.impl;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.igloo.storage.model.Fichier;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

/**
 * <p>Implements Spring {@link TransactionSynchronization} contract. This instance must be added in
 * {@link TransactionSynchronization} context and {@link #addTask(Long, StorageTaskType, Path)} must be called
 * for each {@link Fichier} transaction-involving operation.</p>
 * 
 * <p>After transaction status switch, operations are delegated to {@link StorageTransactionHandler}.</p>
 * 
 * @see TransactionSynchronization
 * @see StorageTransactionHandler
 * @see TransactionSynchronizationUtils
 */
public class StorageTransactionAdapter implements TransactionSynchronization {

	static final Logger LOGGER = LoggerFactory.getLogger(StorageTransactionAdapter.class);

	private final List<StorageTask> tasks = new LinkedList<>();
	private final StorageTransactionHandler handler;

	public StorageTransactionAdapter(StorageTransactionHandler handler) {
		this.handler = handler;
	}

	@Override
	public void afterCompletion(int status) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageTransactionAdapter is triggered with {} status", status);
		}
		switch (status) {
		case TransactionSynchronization.STATUS_COMMITTED:
			handler.doRemovePhysicalDeleteFichiersOnCommit(tasks);
			break;
		case TransactionSynchronization.STATUS_ROLLED_BACK:
			handler.doRemovePhysicalAddedFichiersOnRollback(tasks);
			break;
		case TransactionSynchronization.STATUS_UNKNOWN:
		default:
			throw new IllegalSwitchValueException(status);
		}
	}

	public void addTask(Long id, StorageTaskType type, Path path) {
		tasks.add(new StorageTask(id, type, path));
	}
}

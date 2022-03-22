package org.igloo.storage.impl;

import java.nio.file.Path;

import org.igloo.storage.model.Fichier;
import org.iglooproject.commons.util.exception.IllegalSwitchValueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

/**
 * <p>Implements Spring {@link TransactionSynchronization} contract. This instance must be added in
 * {@link TransactionSynchronization} context and {@link #addEvent(Long, StorageEventType, Path)} must be called
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

	private final Integer order;

	private final StorageTransactionHandler handler;

	private final IStorageTransactionResourceManager resourceManager;

	public StorageTransactionAdapter(Integer order, StorageTransactionHandler handler, IStorageTransactionResourceManager resourceManager) {
		this.order = order;
		this.handler = handler;
		this.resourceManager = resourceManager;
	}

	@Override
	public void afterCompletion(int status) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("StorageTransactionAdapter is triggered with {} status", status);
		}
		try {
			switch (status) {
			case TransactionSynchronization.STATUS_COMMITTED:
				handler.onCommit(resourceManager.getEvents());
				break;
			case TransactionSynchronization.STATUS_ROLLED_BACK:
				handler.onRollback(resourceManager.getEvents());
				break;
			case TransactionSynchronization.STATUS_UNKNOWN:
			default:
				throw new IllegalSwitchValueException(status);
			}
		} finally {
			resourceManager.unbind();
		}
	}

	@Override
	public int getOrder() {
		return order;
	}
}

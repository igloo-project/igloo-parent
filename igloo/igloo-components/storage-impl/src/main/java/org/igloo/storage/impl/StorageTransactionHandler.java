package org.igloo.storage.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class reads provided {@link StorageEvent} collections to apply appropriate transaction completion action
 * on storage.
 * 
 * All file operations are delegated to {@link StorageOperations}. All meaningful logs are triggered by
 * {@link StorageOperations} instance.
 * 
 * @see StorageOperations
 * @see StorageTransactionAdapter
 */
public class StorageTransactionHandler {

	static final Logger LOGGER = LoggerFactory.getLogger(StorageTransactionHandler.class);

	private final StorageOperations operations;

	public StorageTransactionHandler(StorageOperations operations) {
		this.operations = operations;
	}

    // TODO MPI : on ne devrait pas plutôt appeler la méthode `onRollback`, comme ça l'adapter n'a pas à savoir ce que
    // fait le handler en cas de rollback
	public void doRemovePhysicalAddedFichiersOnRollback(List<StorageEvent> events) {
		events.stream().filter(StorageTransactionHandler::isAdd).forEach(t -> {
			operations.removePhysicalFile("[rollback/add]", t);
		});
	}

    // idem
	public void doRemovePhysicalDeleteFichiersOnCommit(List<StorageEvent> events) {
		events.stream().filter(StorageTransactionHandler::isDelete).forEach(t -> {
			operations.removePhysicalFile("[commit/delete]", t);
		});
	}

	public static boolean isAdd(StorageEvent event) {
		return StorageEventType.ADD.equals(event.getType());
	}

	public static boolean isDelete(StorageEvent event) {
		return StorageEventType.DELETE.equals(event.getType());
	}

}

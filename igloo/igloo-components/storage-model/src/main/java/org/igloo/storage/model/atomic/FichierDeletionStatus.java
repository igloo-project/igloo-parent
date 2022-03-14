package org.igloo.storage.model.atomic;

public enum FichierDeletionStatus {

	/**
	 * Deletion trigger by the application
	 */
	APPLICATION,

	/**
	 * Deletion trigger by rollback during creation
	 */
	ROLLBACK;

}

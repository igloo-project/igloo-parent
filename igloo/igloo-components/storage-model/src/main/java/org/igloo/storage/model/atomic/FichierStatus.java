package org.igloo.storage.model.atomic;

import org.igloo.storage.model.Fichier;

public enum FichierStatus {

	/**
	 * The {@link Fichier} is in use
	 */
	ALIVE,

	/**
	 * The deletion of {@link Fichier} has been asked and the file in the filesystem is deleted.
	 * The entity will soon be deleted.
	 */
	DELETED;
}

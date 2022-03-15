package org.igloo.storage.model.atomic;

import org.igloo.storage.model.StorageUnit;

// TODO MPI : à travailler
public enum StorageUnitStatus {

	// TODO MPI : définir
	ALIVE,

	/**
	 * {@link StorageUnit} does not store new file
	 */
	ARCHIVED;

}

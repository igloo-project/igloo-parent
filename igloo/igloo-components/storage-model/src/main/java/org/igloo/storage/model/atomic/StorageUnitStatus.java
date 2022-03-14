package org.igloo.storage.model.atomic;

import org.igloo.storage.model.StorageUnit;

// TODO MPI : à travailler
public enum StorageUnitStatus {

	/**
	 * {@link StorageUnit} is closed
	 */
	READ_ONLY,
	RESTORING,
	ABSENT, // TODO MPI : ça correspond à quoi ?
	ERROR; // TODO MPI : à voir si on le garde, sans doute plutôt dans une entité dédiée aux erreurs

}

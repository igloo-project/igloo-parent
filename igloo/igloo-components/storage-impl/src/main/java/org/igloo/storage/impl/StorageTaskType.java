package org.igloo.storage.impl;

public enum StorageTaskType {

	ADD,

	// TODO MPI : bascule de l'entité à l'état DELETED
	ASK_DELETION,

	// TODO MPI : deletion réelle
	DELETE;

}

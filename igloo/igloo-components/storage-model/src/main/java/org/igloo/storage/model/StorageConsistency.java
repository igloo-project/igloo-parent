package org.igloo.storage.model;

import java.io.Serializable;

import org.igloo.storage.model.atomic.IFichierType;

// TODO MPI Faire un seul pour une unité et un fichierType
public class StorageConsistency implements Serializable {

	private StorageUnit unit;

	private IFichierType fichierType;

	private int fsFileCount;

	private int dbFichierCount;

	public StorageConsistency(StorageUnit unit, IFichierType fichierType) {
		this.unit = unit;
		this.fichierType = fichierType;
	}

	public StorageUnit getUnit() {
		return unit;
	}

	public void setUnit(StorageUnit unit) {
		this.unit = unit;
	}

	public IFichierType getFichierType() {
		return fichierType;
	}

	public void setFichierType(IFichierType fichierType) {
		this.fichierType = fichierType;
	}

	public int getFsFileCount() {
		return fsFileCount;
	}

	public void setFsFileCount(int fsFileCount) {
		this.fsFileCount = fsFileCount;
	}

	public int getDbFichierCount() {
		return dbFichierCount;
	}

	public void setDbFichierCount(int dbFichierCount) {
		this.dbFichierCount = dbFichierCount;
	}
}

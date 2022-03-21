package org.igloo.storage.model;

import org.igloo.storage.model.atomic.IFichierType;

import java.io.Serializable;

public class IFichierTypeConsistency implements Serializable {

	private IFichierType fichierType;

	private int fsFileCount;

	private int dbFileCount;

	public IFichierTypeConsistency(IFichierType fichierType) {
		this.fichierType = fichierType;
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

	public int getDbFileCount() {
		return dbFileCount;
	}

	public void setDbFileCount(int dbFileCount) {
		this.dbFileCount = dbFileCount;
	}
}

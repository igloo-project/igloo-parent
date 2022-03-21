package org.igloo.storage.model;

import java.io.Serializable;

public class IFichierTypeConsistency implements Serializable {

	private int fsFileCount;

	private int dbFileCount;

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

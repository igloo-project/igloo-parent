package org.igloo.storage.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class StorageConsistency implements Serializable {

	private static final long serialVersionUID = -9046836892299254738L;

	@ManyToOne
	private StorageUnit unit;

	private int fsFileCount;

	private int dbFichierCount;

	public StorageConsistency() {
		super();
	}

	public StorageConsistency(StorageUnit unit) {
		this();
		this.unit = unit;
	}

	public StorageUnit getUnit() {
		return unit;
	}

	public void setUnit(StorageUnit unit) {
		this.unit = unit;
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

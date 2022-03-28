package org.igloo.storage.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
public class StorageConsistencyCheck extends GenericEntity<Long, StorageConsistencyCheck> implements Serializable {

	private static final long serialVersionUID = -9046836892299254738L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private StorageUnit storageUnit;

	private int fsFileCount;

	private int dbFichierCount;

	public StorageConsistencyCheck() {
		super();
	}

	public StorageConsistencyCheck(StorageUnit unit) {
		this();
		this.storageUnit = unit;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public StorageUnit getStorageUnit() {
		return storageUnit;
	}

	public void setStorageUnit(StorageUnit storageUnit) {
		this.storageUnit = storageUnit;
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

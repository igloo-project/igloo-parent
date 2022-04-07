package org.igloo.storage.model.statistics;

import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;

public class StorageStatistic {

	private Long storageUnitId;
	private IStorageUnitType storageUnitType;
	private IFichierType fichierType;
	private FichierStatus fichierStatus;
	private Integer count;
	private Long size;

	public StorageStatistic() {
		super();
	}

	public Long getStorageUnitId() {
		return storageUnitId;
	}
	public void setStorageUnitId(Long storageUnitId) {
		this.storageUnitId = storageUnitId;
	}
	public IStorageUnitType getStorageUnitType() {
		return storageUnitType;
	}
	public void setStorageUnitType(IStorageUnitType storageUnitType) {
		this.storageUnitType = storageUnitType;
	}
	public IFichierType getFichierType() {
		return fichierType;
	}
	public void setFichierType(IFichierType fichierType) {
		this.fichierType = fichierType;
	}
	public FichierStatus getFichierStatus() {
		return fichierStatus;
	}
	public void setFichierStatus(FichierStatus fichierStatus) {
		this.fichierStatus = fichierStatus;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
}

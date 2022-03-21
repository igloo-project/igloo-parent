package org.igloo.storage.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StorageConsistency implements Serializable {

	private List<StorageUnitConsistency> storageUnitConsistencies = new ArrayList<>();

	public List<StorageUnitConsistency> getStorageUnitConsistencies() {
		return storageUnitConsistencies;
	}

	public void setStorageUnitConsistencies(List<StorageUnitConsistency> storageUnitConsistencies) {
		this.storageUnitConsistencies = storageUnitConsistencies;
	}

	public void addStorageUnitConsistencies(StorageUnitConsistency storageUnitConsistency) {
		storageUnitConsistencies.add(storageUnitConsistency);
	}

}

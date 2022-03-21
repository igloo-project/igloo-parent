package org.igloo.storage.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StorageUnitConsistency implements Serializable {

	private StorageUnit unit;

	private List<IFichierTypeConsistency> fichierTypeConsistencies = new ArrayList<>();

	public StorageUnitConsistency(StorageUnit unit) {
		this.unit = unit;
	}

	public List<IFichierTypeConsistency> getFichierTypeConsistencies() {
		return fichierTypeConsistencies;
	}

	public void setFichierTypeConsistencies(List<IFichierTypeConsistency> fichierTypeConsistencies) {
		this.fichierTypeConsistencies = fichierTypeConsistencies;
	}

	public void addFichierTypeConsistencies(IFichierTypeConsistency fichierTypeConsistency) {
		fichierTypeConsistencies.add(fichierTypeConsistency);
	}
}

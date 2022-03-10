package org.igloo.storage.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * Entity that represents a stored file. {@code Fichier} are dispatched in {@code StorageUnit}.
 */
public class Fichier extends GenericEntity<Long, Fichier> {

	private static final long serialVersionUID = 2683095626872762980L;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Used to organize file storage. Cannot be changed after initial attribution.
	 */
	private IFichierType fichierType;

	/**
	 * {@code StorageUnit} responible for file storage. Cannot be changed after initial attribution.
	 */
	@ManyToOne
	private StorageUnit storageUnit;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public IFichierType getFichierType() {
		return fichierType;
	}

	public void setFichierType(IFichierType fichierType) {
		this.fichierType = fichierType;
	}

	public StorageUnit getStorageUnit() {
		return storageUnit;
	}

	public void setStorageUnit(StorageUnit storageUnit) {
		this.storageUnit = storageUnit;
	}

}

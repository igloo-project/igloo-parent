package org.igloo.storage.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;
import org.igloo.storage.model.hibernate.StorageHibernateConstants;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * Entity that represents a stored file. {@code Fichier} are dispatched in {@code StorageUnit}.
 */
@Entity
@Bindable
public class Fichier extends GenericEntity<Long, Fichier> {

	private static final long serialVersionUID = 2683095626872762980L;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Used to organize file storage. Cannot be changed after initial attribution.
	 */
	@Type(type = StorageHibernateConstants.TYPE_FICHIER_TYPE)
	private IFichierType fichierType;

	/**
	 * {@code StorageUnit} responsible for file storage. Cannot be changed after initial attribution.
	 */
	@ManyToOne(optional = false)
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

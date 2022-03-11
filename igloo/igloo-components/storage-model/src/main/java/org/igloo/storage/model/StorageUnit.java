package org.igloo.storage.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.bindgen.Bindable;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * A {@code StorageUnit} is a physical device that can store files. {@code StorageUnit} implementation must provide
 * listing API to allow consistency jobs to check the storage status (missing or orphan files). Performance can
 * be ensured by splitting {@code StorageUnit} before listing can be problematic (duration, memory).
 */
@Entity
@Bindable
public class StorageUnit extends GenericEntity<Long, StorageUnit> {

	private static final long serialVersionUID = -4475039934044786927L;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Do not use this attribute, keep it lazy ! (ten thousand items expected).
	 */
	@OneToMany(mappedBy = "storageUnit", fetch = FetchType.LAZY)
	private Set<Fichier> fichiers;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Set<Fichier> getFichiers() {
		throw new IllegalStateException("This collection must remain lazy.");
	}

	public void setFichiers(Set<Fichier> fichiers) {
		throw new IllegalStateException("This collection must remain lazy.");
	}

}

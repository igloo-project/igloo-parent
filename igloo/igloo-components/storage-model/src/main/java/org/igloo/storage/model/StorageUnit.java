package org.igloo.storage.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.igloo.storage.model.hibernate.StorageHibernateConstants;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * A {@link StorageUnit} is a physical device that can store files. {@link StorageUnit} implementation must provide
 * listing API to allow consistency jobs to check the storage status (missing or orphan files/{@link Fichier}).
 * Performance can be ensured by splitting {@link StorageUnit} before listing can be problematic (duration, memory).
 */
@Entity
@Bindable
public class StorageUnit extends GenericEntity<Long, StorageUnit> {

	private static final long serialVersionUID = -4475039934044786927L;

	@Id
	@GeneratedValue
	private Long id;

	@Basic(optional = false)
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StorageUnitStatus status;

	@Column(nullable = false, updatable = false)
	@Basic(optional = false)
	@Type(type = StorageHibernateConstants.TYPE_STORAGE_UNIT_TYPE)
	private IStorageUnitType type;

	// TODO MPI : il est obligatoire dans les faits mais vide avant calcul du path
	@Basic
	@Column
	private String path;

	@Basic(optional = false)
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	// TODO MPI : Supprimer; on aura des collections de StorageUnitStatistics car une par type de fichier
	@OneToOne(mappedBy = "storageUnit", fetch = FetchType.LAZY)
	private StorageUnitStatistics statistics;

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

	public StorageUnitStatus getStatus() {
		return status;
	}

	public void setStatus(StorageUnitStatus status) {
		this.status = status;
	}

	public IStorageUnitType getType() {
		return type;
	}

	public void setType(IStorageUnitType type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public StorageUnitStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(StorageUnitStatistics statistics) {
		this.statistics = statistics;
	}

	public Set<Fichier> getFichiers() {
		throw new IllegalStateException("This collection must remain lazy.");
	}

	public void setFichiers(Set<Fichier> fichiers) {
		throw new IllegalStateException("This collection must remain lazy.");
	}

}

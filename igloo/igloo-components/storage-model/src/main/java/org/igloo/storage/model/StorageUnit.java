package org.igloo.storage.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	@Basic(optional = false)
	@Column(unique = true, nullable = false, updatable = false)
	private Long id;

	@Basic(optional = false)
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StorageUnitStatus status;

	@Column(nullable = false, updatable = false)
	@Basic(optional = false)
	@Type(type = StorageHibernateConstants.TYPE_STORAGE_UNIT_TYPE)
	private IStorageUnitType type;

	@Basic(optional = false)
	@Column(unique = true, nullable = false, updatable = false)
	private String path;

	@Basic(optional = false)
	@Column(nullable = false)
	private LocalDateTime creationDate;

	@OneToMany(mappedBy = "storageUnit", fetch = FetchType.LAZY, orphanRemoval = true)
	private List<StorageConsistencyCheck> consistencyChecks;

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

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public List<StorageConsistencyCheck> getConsistencyChecks() {
		throw new IllegalStateException("This collection must remain lazy");
	}

	public void setStatistics(List<StorageConsistencyCheck> statistics) {
		throw new IllegalStateException("This collection must remain lazy");
	}

	public Set<Fichier> getFichiers() {
		throw new IllegalStateException("This collection must remain lazy.");
	}

	public void setFichiers(Set<Fichier> fichiers) {
		throw new IllegalStateException("This collection must remain lazy.");
	}

}

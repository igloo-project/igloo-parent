package org.igloo.storage.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import org.bindgen.Bindable;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * A {@link StorageUnit} is a physical device that can store files. {@link StorageUnit} implementation must provide
 * listing API to allow consistency jobs to check the storage status (missing or orphan files). Performance can
 * be ensured by splitting {@link StorageUnit} before listing can be problematic (duration, memory).
 */
@Entity
@Bindable
public class StorageUnit extends GenericEntity<Long, StorageUnit> {

	private static final long serialVersionUID = -4475039934044786927L;

	@Id
	@GeneratedValue
	private Long id;

	@Basic // TODO MPI : vu les valeurs actuelles, ça doit être optionnel(optional = false)
	@Enumerated(EnumType.STRING)
	private StorageUnitStatus status;

	@Basic(optional = false)
	private String path;

	// TODO MPI : Comment je passe du uuid au relativePath - sans doute une interface
	@Basic(optional = false)
	private String pathStrategy;

	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@OneToOne(mappedBy = "storageUnit", optional = false, fetch = FetchType.LAZY)
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPathStrategy() {
		return pathStrategy;
	}

	public void setPathStrategy(String pathStrategy) {
		this.pathStrategy = pathStrategy;
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

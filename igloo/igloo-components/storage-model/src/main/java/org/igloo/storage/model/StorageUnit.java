package org.igloo.storage.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.bindgen.Bindable;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

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

	/**
	 * Automatic consistency check mechanism.
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StorageUnitCheckType checkType = StorageUnitCheckType.NONE;

	/**
	 * Check delay for {@link StorageUnit}. If null, application settings is applied.
	 */
	@Basic(optional = true)
	private Duration checkDelay;

	/**
	 * Checksum-enabled check delay for {@link StorageUnit}. If null application settings is applied. Used only if
	 * {@link #checkType} allows checksum check.
	 */
	@Basic(optional = true)
	private Duration checkChecksumDelay;

	/**
	 * Split condition, by total file size, before a storage unit split. New storage unit inherits all settings.
	 * 
	 * If null, file size does not trigger storage unit split.
	 */
	@Basic(optional = true)
	private Long splitSize;

	/**
	 * Split condition, by duration, before a storage unit split. New storage unit inherits all settings.
	 * 
	 * If null, storage unit age does not trigger storage unit split.
	 */
	@Basic(optional = true)
	private Duration splitDuration;

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

	public void setConsistencyChecks(List<StorageConsistencyCheck> statistics) {
		throw new IllegalStateException("This collection must remain lazy");
	}

	public Set<Fichier> getFichiers() {
		throw new IllegalStateException("This collection must remain lazy.");
	}

	public void setFichiers(Set<Fichier> fichiers) {
		throw new IllegalStateException("This collection must remain lazy.");
	}

	public StorageUnitCheckType getCheckType() {
		return checkType;
	}

	public void setCheckType(StorageUnitCheckType checkType) {
		this.checkType = checkType;
	}

	public Duration getCheckDelay() {
		return checkDelay;
	}

	public void setCheckDelay(Duration checkDelay) {
		this.checkDelay = checkDelay;
	}

	public Duration getCheckChecksumDelay() {
		return checkChecksumDelay;
	}

	public void setCheckChecksumDelay(Duration checkChecksumDelay) {
		this.checkChecksumDelay = checkChecksumDelay;
	}

	public synchronized Long getSplitSize() {
		return splitSize;
	}

	public synchronized void setSplitSize(Long splitSize) {
		this.splitSize = splitSize;
	}

	public synchronized Duration getSplitDuration() {
		return splitDuration;
	}

	public synchronized void setSplitDuration(Duration splitDuration) {
		this.splitDuration = splitDuration;
	}

}

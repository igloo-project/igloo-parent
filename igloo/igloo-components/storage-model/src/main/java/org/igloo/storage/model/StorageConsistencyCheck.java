package org.igloo.storage.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.igloo.storage.model.atomic.StorageConsistencyCheckResult;
import org.igloo.storage.model.atomic.StorageUnitCheckType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
public class StorageConsistencyCheck extends GenericEntity<Long, StorageConsistencyCheck> implements Serializable {

	private static final long serialVersionUID = -9046836892299254738L;

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Storage unit targetted by this check. Listings are performed on entity linked with this {@link StorageUnit} and
	 * filesystem content in {@link StorageUnit} directory (see {@link StorageUnit#getPath()}.
	 */
	@ManyToOne
	private StorageUnit storageUnit;

	/**
	 * Check startup time.
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private LocalDateTime checkStartedOn;

	/**
	 * Check end time. Set to start time during check processing; must not be visible outside transaction.
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private LocalDateTime checkFinishedOn;

	@Enumerated(EnumType.STRING)
	private StorageConsistencyCheckResult status;

	@Enumerated(EnumType.STRING)
	private StorageUnitCheckType checkType;

	@Basic
	private Integer fsFileCount;

	@Basic
	private Long fsFileSize;

	@Basic
	private Integer dbFichierCount;

	@Basic
	private Long dbFichierSize;

	@Basic
	private Integer missingFileCount;

	@Basic
	private Integer missingFichierCount;

	@Basic
	private Integer contentMismatchCount;

	public StorageConsistencyCheck() {
		super();
	}

	public StorageConsistencyCheck(StorageUnit unit) {
		this();
		this.storageUnit = unit;
		this.checkStartedOn = this.checkFinishedOn = LocalDateTime.now();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public StorageUnit getStorageUnit() {
		return storageUnit;
	}

	public void setStorageUnit(StorageUnit storageUnit) {
		this.storageUnit = storageUnit;
	}

	public LocalDateTime getCheckStartedOn() {
		return checkStartedOn;
	}

	public void setCheckStartedOn(LocalDateTime checkStartedOn) {
		this.checkStartedOn = checkStartedOn;
	}

	public LocalDateTime getCheckFinishedOn() {
		return checkFinishedOn;
	}

	public void setCheckFinishedOn(LocalDateTime checkedOn) {
		this.checkFinishedOn = checkedOn;
	}

	public StorageConsistencyCheckResult getStatus() {
		return status;
	}

	public void setStatus(StorageConsistencyCheckResult status) {
		this.status = status;
	}

	public StorageUnitCheckType getCheckType() {
		return checkType;
	}

	public void setCheckType(StorageUnitCheckType checkType) {
		this.checkType = checkType;
	}

	public Integer getFsFileCount() {
		return fsFileCount;
	}

	public void setFsFileCount(Integer fsFileCount) {
		this.fsFileCount = fsFileCount;
	}

	public Integer getDbFichierCount() {
		return dbFichierCount;
	}

	public void setDbFichierCount(Integer dbFichierCount) {
		this.dbFichierCount = dbFichierCount;
	}

	public Long getFsFileSize() {
		return fsFileSize;
	}

	public void setFsFileSize(Long fsFileSize) {
		this.fsFileSize = fsFileSize;
	}

	public Long getDbFichierSize() {
		return dbFichierSize;
	}

	public void setDbFichierSize(Long dbFichierSize) {
		this.dbFichierSize = dbFichierSize;
	}

	public Integer getMissingFileCount() {
		return missingFileCount;
	}

	public void setMissingFileCount(Integer missingFileCount) {
		this.missingFileCount = missingFileCount;
	}

	public Integer getMissingFichierCount() {
		return missingFichierCount;
	}

	public void setMissingFichierCount(Integer missingFichierCount) {
		this.missingFichierCount = missingFichierCount;
	}

	public Integer getContentMismatchCount() {
		return contentMismatchCount;
	}

	public void setContentMismatchCount(Integer checksumMismatchCount) {
		this.contentMismatchCount = checksumMismatchCount;
	}
}

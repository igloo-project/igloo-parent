package org.igloo.storage.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.bindgen.Bindable;
import org.hibernate.annotations.Type;
import org.igloo.storage.model.atomic.ChecksumType;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.hibernate.StorageHibernateConstants;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

/**
 * Entity that represents a stored file. {@link Fichier} are dispatched in {@link StorageUnit}.
 */
@Entity
@Bindable
public class Fichier extends GenericEntity<Long, Fichier> {

	private static final long serialVersionUID = 2683095626872762980L;

	@Id
	@Basic(optional = false)
	@Column(unique = true, nullable = false, updatable = false)
	private Long id;

	/**
	 * Use to identify associated file on the filesystem.
	 */
	@Basic(optional = false)
	@Column(columnDefinition = "uuid", unique = true, nullable = false, updatable = false)
	private UUID uuid;

	@Basic(optional = false)
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FichierStatus status;

	/**
	 * Used to organize file storage. Cannot be changed after initial attribution.
	 */
	@Type(type = StorageHibernateConstants.TYPE_FICHIER_TYPE)
	@Basic(optional = false)
	@Column(nullable = false)
	private IFichierType type;

	/**
	 * {@link StorageUnit} responsible for file storage. Cannot be changed after initial attribution.
	 */
	@ManyToOne(optional = false)
	private StorageUnit storageUnit;

	/**
	 * Path within storage unit directory<br>
	 * Ex : {@code fichiertype/hash/file.png}
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private String relativePath;

	/**
	 * Raw filename
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private String filename;

	/**
	 * Size in bytes.
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private long size;

	@Basic(optional = false)
	@Column(nullable = false)
	private String checksum;

	@Basic(optional = false)
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ChecksumType checksumType;

	@Basic(optional = false)
	@Column(nullable = false)
	private String mimetype;

	@Basic(optional = false)
	@Column(nullable = false)
	private LocalDateTime creationDate;

	/**
	 * Initialized when {@link #status} is set to {@link FichierStatus#ALIVE}.
	 */
	@Basic
	@Column
	private LocalDateTime validationDate;

	/**
	 * Initialized when {@link #status} is set to {@link FichierStatus#INVALIDATED}.
	 */
	@Basic
	@Column
	private LocalDateTime invalidationDate;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public FichierStatus getStatus() {
		return status;
	}

	public void setStatus(FichierStatus status) {
		this.status = status;
	}

	public IFichierType getType() {
		return type;
	}

	public void setType(IFichierType type) {
		this.type = type;
	}

	public StorageUnit getStorageUnit() {
		return storageUnit;
	}

	public void setStorageUnit(StorageUnit storageUnit) {
		this.storageUnit = storageUnit;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getFilename() {
		return filename;
	}

	public void setName(String name) {
		this.filename = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public ChecksumType getChecksumType() {
		return checksumType;
	}

	public void setChecksumType(ChecksumType checksumType) {
		this.checksumType = checksumType;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public LocalDateTime getValidationDate() {
		return validationDate;
	}

	public void setValidationDate(LocalDateTime validationDate) {
		this.validationDate = validationDate;
	}

	public LocalDateTime getInvalidationDate() {
		return invalidationDate;
	}

	public void setInvalidationDate(LocalDateTime deletionDate) {
		this.invalidationDate = deletionDate;
	}

}

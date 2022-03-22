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
	private IFichierType fichierType;

	/**
	 * {@link StorageUnit} responsible for file storage. Cannot be changed after initial attribution.
	 */
	@ManyToOne(optional = false)
	private StorageUnit storageUnit;

	// TODO MPI : il est obligatoire dans les faits mais vide avant calcul du relativePath
	/**
	 * Path within storage unit directory<br>
	 * Ex : {@code fichiertype/hash/file.png}
	 */
	@Basic
	@Column
	private String relativePath;

	/**
	 * Raw filename
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private String filename;

	// TODO MPI : il est obligatoire dans les faits mais vide avant calcul du relativePath
	/**
	 * Size in bytes.
	 */
	@Basic
	@Column
	private long size;

	// TODO MPI : il est obligatoire dans les faits mais vide avant calcul du relativePath
	@Basic
	@Column
	private String checksum;

	@Basic(optional = false)
	@Column(nullable = false)
	private ChecksumType checksumType;

	@Basic
	@Column
	private String mimetype;

	@Basic(optional = false)
	@Column(nullable = false)
	private LocalDateTime creationDate;

	/**
	 * Initialized when {@link #status} is set to {@link FichierStatus#INVALIDATED}.
	 */
	@Basic
	@Column
	private LocalDateTime deletionDate;

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

	public LocalDateTime getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(LocalDateTime deletionDate) {
		this.deletionDate = deletionDate;
	}

}

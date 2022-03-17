package org.igloo.storage.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	@GeneratedValue
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

	// TODO MPI : doc + exemple fichiertype/hash/uuid.png
	@Basic(optional = false)
	@Column(nullable = false)
	private String relativePath;

	@Basic(optional = false)
	@Column(nullable = false)
	private String name;

	@Basic
	@Column
	private String extension;

	/**
	 * Size in bytes.
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private long size;

	// TODO MPI : devrait être des chaînes de 16 caractères en SHA-256
	@Basic(optional = false)
	@Column(nullable = false)
	private String checksum;

	@Basic(optional = false)
	@Column(nullable = false)
	private ChecksumType checksumType; // TODO MPI : Enum SHA-256 (on utilise Guava {@link Hashing})

	@Basic
	@Column
	private String mimetype;

	@Basic(optional = false)
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	/**
	 * Initialized when {@link #status} is set to {@link FichierStatus#DELETED}.
	 */
	@Basic
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletionDate;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDeletionDate() {
		return deletionDate;
	}

	public void setDeletionDate(Date deletionDate) {
		this.deletionDate = deletionDate;
	}

}

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
import org.igloo.storage.model.atomic.FichierDeletionStatus;
import org.igloo.storage.model.atomic.FichierStatus;
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
	@Column(columnDefinition = "uuid", unique = true, nullable = false, updatable = false)
	private UUID uuid;

	@Basic(optional = false)
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

	@Basic(optional = false)
	private String relativePath;

    @Basic(optional = false)
    private String name;

	@Basic
	private String extension; // TODO MPI : on le laisse vide au cas où il n'y a pas d'extension

	/**
	 * Size in bytes.
	 */
    @Basic(optional = false)
    private long size;

    @Basic(optional = false)
    private String checksum; // TODO MPI : String ?

    @Basic(optional = false)
    private String checksumType; // TODO MPI : String ?

	@Basic
	private String mimetype; // TODO MPI : on le laisse optionnel au cas où on n'arrive pas à déterminer le type ?

	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	/**
	 * Initialized when {@link #status} is set to DELETED.
	 */
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date deletionDate;

	@Basic
	@Enumerated(EnumType.STRING)
	private FichierDeletionStatus deletionStatus;

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

	public String getChecksumType() {
		return checksumType;
	}

	public void setChecksumType(String checksumType) {
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

	public FichierDeletionStatus getDeletionStatus() {
		return deletionStatus;
	}

	public void setDeletionStatus(FichierDeletionStatus deletionStatus) {
		this.deletionStatus = deletionStatus;
	}
}

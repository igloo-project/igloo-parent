package org.igloo.storage.model;

import java.nio.file.Path;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.igloo.storage.model.atomic.StorageFailureStatus;
import org.igloo.storage.model.atomic.StorageFailureType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

@Entity
public class StorageFailure extends GenericEntity<Long, StorageFailure> {

	private static final long serialVersionUID = -4337799097436432065L;

	@Id
	@GeneratedValue
	private Long id;

	@Basic(optional = false)
	private String path;

	@ManyToOne(optional = false)
	private StorageUnit unit;

	@ManyToOne(optional = true)
	private Fichier fichier;

	@Basic(optional = false)
	private LocalDateTime creationTime;

	@Basic(optional = true)
	private LocalDateTime acknowledgedOn;

	@Basic(optional = true)
	private LocalDateTime fixedOn;

	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	private StorageFailureStatus status;

	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	private StorageFailureType type;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public StorageUnit getUnit() {
		return unit;
	}

	public void setUnit(StorageUnit unit) {
		this.unit = unit;
	}

	public Fichier getFichier() {
		return fichier;
	}

	public void setFichier(Fichier fichier) {
		this.fichier = fichier;
	}

	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	public LocalDateTime getAcknowledgedOn() {
		return acknowledgedOn;
	}

	public void setAcknowledgedOn(LocalDateTime acknowledgedOn) {
		this.acknowledgedOn = acknowledgedOn;
	}

	public LocalDateTime getFixedOn() {
		return fixedOn;
	}

	public void setFixedOn(LocalDateTime fixedOn) {
		this.fixedOn = fixedOn;
	}

	public StorageFailureStatus getStatus() {
		return status;
	}

	public void setStatus(StorageFailureStatus status) {
		this.status = status;
	}

	public StorageFailureType getType() {
		return type;
	}

	public void setType(StorageFailureType type) {
		this.type = type;
	}

	public static StorageFailure ofMissingEntity(String path, StorageUnit unit) {
		StorageFailure failure = new StorageFailure();
		failure.setPath(path);
		failure.setCreationTime(LocalDateTime.now());
		failure.setStatus(StorageFailureStatus.ALIVE);
		failure.setType(StorageFailureType.MISSING_ENTITY);
		failure.setUnit(unit);
		return failure;
	}

	public static StorageFailure ofMissingFile(Path path, Fichier fichier, StorageUnit unit) {
		StorageFailure failure = new StorageFailure();
		failure.setPath(path.toString());
		failure.setFichier(fichier);
		failure.setCreationTime(LocalDateTime.now());
		failure.setStatus(StorageFailureStatus.ALIVE);
		failure.setType(StorageFailureType.MISSING_FILE);
		failure.setUnit(unit);
		return failure;
	}

	public static StorageFailure ofChecksumMismatch(Path path, Fichier fichier, StorageUnit unit) {
		StorageFailure failure = new StorageFailure();
		failure.setPath(path.toString());
		failure.setFichier(fichier);
		failure.setCreationTime(LocalDateTime.now());
		failure.setStatus(StorageFailureStatus.ALIVE);
		failure.setType(StorageFailureType.CHECKSUM_MISMATCH);
		failure.setUnit(unit);
		return failure;
	}

}

package org.igloo.storage.model;

import org.bindgen.Bindable;
import org.igloo.storage.model.atomic.FichierStatus;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Bindable
public class StorageUnitStatisticsDatabase implements Serializable {

	/**
	 * Count of total {@link Fichier} on the filesystem in the {@link StorageUnit}
	 */
	@Basic(optional = false)
	private long fichierCount = 0;

	/**
	 * Count of {@link Fichier} with status {@link FichierStatus#DELETED}
	 */
	@Basic(optional = false)
	private long aliveCount = 0;

	/**
	 * Count of {@link Fichier} with status {@link FichierStatus#DELETED}
	 */
	@Basic(optional = false)
	private long deletedCount = 0;

	/**
	 * Count of {@link Fichier} with {@link FichierStatus#DELETED} that should have been deleted
	 */
	@Basic(optional = false)
	private long expiredDeletedCount = 0;

	/**
	 * Count of {@link Fichier} missing in database but wich file is still present in the filesystem
	 */
	@Basic(optional = false)
	private long missingCount = 0;

	public long getFichierCount() {
		return fichierCount;
	}

	public void setFichierCount(long fileCount) {
		this.fichierCount = fileCount;
	}

	public long getAliveCount() {
		return aliveCount;
	}

	public void setAliveCount(long aliveCount) {
		this.aliveCount = aliveCount;
	}

	public long getDeletedCount() {
		return deletedCount;
	}

	public void setDeletedCount(long deletedCount) {
		this.deletedCount = deletedCount;
	}

	public long getExpiredDeletedCount() {
		return expiredDeletedCount;
	}

	public void setExpiredDeletedCount(long expiredDeletedCount) {
		this.expiredDeletedCount = expiredDeletedCount;
	}

	public long getMissingCount() {
		return missingCount;
	}

	public void setMissingCount(long missingCount) {
		this.missingCount = missingCount;
	}
}

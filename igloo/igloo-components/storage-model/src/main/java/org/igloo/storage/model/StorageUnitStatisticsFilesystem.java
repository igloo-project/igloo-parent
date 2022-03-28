package org.igloo.storage.model;

import org.bindgen.Bindable;
import org.igloo.storage.model.atomic.FichierStatus;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Bindable
public class StorageUnitStatisticsFilesystem implements Serializable {

	private static final long serialVersionUID = 5999959241711215523L;

	/**
	 * Size of StorageUnit in byte
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private long size = 0;

	/**
	 * Count of total file on the filesystem in the {@code StorageUnit}
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private long fileCount = 0;

	/**
	 * Count of file missing in the filesystem but wich {@link Fichier} representation is still present in the database
	 * with status {@link FichierStatus#ALIVE}
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private long missingCount = 0;

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getFileCount() {
		return fileCount;
	}

	public void setFileCount(long fileCount) {
		this.fileCount = fileCount;
	}

	public long getMissingCount() {
		return missingCount;
	}

	public void setMissingCount(long missingCount) {
		this.missingCount = missingCount;
	}
}

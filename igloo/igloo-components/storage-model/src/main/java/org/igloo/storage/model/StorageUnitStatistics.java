package org.igloo.storage.model;

import org.bindgen.Bindable;
import org.igloo.storage.model.atomic.IFichierType;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Bindable
public class StorageUnitStatistics extends GenericEntity<Long, StorageUnitStatistics> {

	private static final long serialVersionUID = 6165030207196592898L;

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private StorageUnit storageUnit;

	// TODO MPI : Ca devrait être utile de connaitre les types de fichier présent dans une SU
	// TODO MPI : Peut-être faire des statistiques par type de fichier au sein d'une SU puis d'avoir les stats aggrégées
	@ElementCollection
	private List<IFichierType> fichiertypes;

	@Embedded
	private StorageUnitStatisticsDatabase database = new StorageUnitStatisticsDatabase();

	@Embedded
	private StorageUnitStatisticsFilesystem filesystem = new StorageUnitStatisticsFilesystem();

	/**
	 * Count of {@link Fichier} and file wich size mismatch
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private long sizeMismatchCount = 0;

	/**
	 * Count of {@link Fichier} and file wich checksum mismatch
	 */
	@Basic(optional = false)
	@Column(nullable = false)
	private long checksumMismatchCount = 0;

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

	public StorageUnitStatisticsDatabase getDatabase() {
		if (database == null) {
			database = new StorageUnitStatisticsDatabase();
		}
		return database;
	}

	public void setDatabase(StorageUnitStatisticsDatabase database) {
		this.database = database;
	}

	public StorageUnitStatisticsFilesystem getFilesystem() {
		if (filesystem == null) {
			filesystem = new StorageUnitStatisticsFilesystem();
		}
		return filesystem;
	}

	public void setFilesystem(StorageUnitStatisticsFilesystem filesystem) {
		this.filesystem = filesystem;
	}

	public long getSizeMismatchCount() {
		return sizeMismatchCount;
	}

	public void setSizeMismatchCount(long sizeMismatchCount) {
		this.sizeMismatchCount = sizeMismatchCount;
	}

	public long getChecksumMismatchCount() {
		return checksumMismatchCount;
	}

	public void setChecksumMismatchCount(long checksumMismatchCount) {
		this.checksumMismatchCount = checksumMismatchCount;
	}
}

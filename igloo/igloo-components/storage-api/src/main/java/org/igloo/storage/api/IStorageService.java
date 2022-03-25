package org.igloo.storage.api;

import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * {@link IStorageService} is the main interface to use storage module. It allows to create new {@link StorageUnit}, to
 * add, validate, invalidate, remove and get {@link Fichier} and associated data.
 */
public interface IStorageService {

	/**
	 * Creation of {@link StorageUnit} from type into storage
	 */
	StorageUnit createStorageUnit(@Nonnull IStorageUnitType type);

	/**
	 * Creation of {@link Fichier} with status {@link FichierStatus#TRANSIENT} and storage of associated file from
	 * inputStream into storage. If it is not validated, it will at last be deleted along with associated file.
	 */
	@Nonnull
	Fichier addFichier(@Nonnull String filename, @Nonnull IFichierType fichierType, @Nonnull InputStream inputStream);


	/**
	 * The {@link Fichier} is marked {@link FichierStatus#ALIVE} and {@link Fichier#validationDate} is initialized.
	 */
	void validateFichier(@Nonnull Fichier fichier);

	/**
	 * The {@link Fichier} is marked {@link FichierStatus#INVALIDATED} and {@link Fichier#invalidationDate} is
	 * initialized. The entity will soon be deleted along with associated file.
	 */
	void invalidateFichier(@Nonnull Fichier fichier);

	/**
	 * Deletion of {@link Fichier} and associated file
	 */
	void removeFichier(@Nonnull Fichier fichier);

	/**
	 * Get file associated to {@link Fichier}
	 */
	@Nonnull
	File getFile(@Nonnull Fichier fichier);

	/**
	 * Perform a consistency check on a {@link StorageUnit}. Check can be performed with or without checksum validation.
	 */
	@Nonnull
	List<StorageConsistencyCheck> checkConsistency(StorageUnit unit, boolean checksumValidation);

}

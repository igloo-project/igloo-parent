package org.igloo.storage.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistencyCheck;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.FichierStatus;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;
import org.igloo.storage.model.atomic.StorageUnitStatus;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link IStorageService} is the main interface to use storage module. It allows to create new {@link StorageUnit}, to
 * add, validate, invalidate, remove and get {@link Fichier} and associated data.
 */
@Transactional
public interface IStorageService {

	/**
	 * Creation of {@link StorageUnit} from type into storage
	 */
	@Nonnull
	StorageUnit createStorageUnit(@Nonnull IStorageUnitType type);

	/**
	 * @see IStorageService#addFichier(String, IFichierType, InputStream, GenericEntity)
	 * 
	 * Fichier is associated with a null author.
	 */
	@Nonnull
	Fichier addFichier(@Nonnull String filename, @Nonnull IFichierType fichierType, @Nonnull InputStream inputStream);

	/**
	 * Creation of {@link Fichier} with status {@link FichierStatus#TRANSIENT} and storage of associated file from
	 * inputStream into storage. If it is not validated, it will at last be deleted along with associated file.
	 * 
	 * @see IStorageService#addFichier(String, IFichierType, InputStream, GenericEntity)
	 */
	@Nonnull
	Fichier addFichier(@Nonnull String filename, @Nonnull IFichierType fichierType, @Nonnull InputStream inputStream,
			@Nullable GenericEntity<Long, ?> author);

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

	Fichier getFichierById(@Nonnull Long id);

	/**
	 * Get file associated to {@link Fichier}
	 * 
	 * @throws FileNotFoundException if file cannot be found or is not readable.
	 */
	@Transactional(readOnly=true)
	@Nonnull
	File getFile(@Nonnull Fichier fichier) throws FileNotFoundException;

	/**
	 * Perform a consistency check on a {@link StorageUnit}. Check can be performed with or without checksum validation.
	 */
	@Nonnull
	List<StorageConsistencyCheck> checkConsistency(@Nonnull StorageUnit unit, boolean checksumValidation);

	/**
	 * Copy an existing {@link StorageUnitStatus#ALIVE} unit, switch it to {@link StorageUnitStatus#ARCHIVED}, and
	 * return new {@link StorageUnit}.
	 * 
	 * <code>original</code> can be a detached instance. It will be reloaded.
	 * 
	 * @throws IllegalStateException if original is not {@link StorageUnitStatus#ALIVE}
	 */
	@Nonnull
	StorageUnit splitStorageUnit(@Nonnull StorageUnit original);

}

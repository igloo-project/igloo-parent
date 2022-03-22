package org.igloo.storage.api;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageConsistency;
import org.igloo.storage.model.StorageUnit;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;

public interface IStorageService {

	/**
	 * Creation of {@link StorageUnit} from type into storage
	 */
	StorageUnit createStorageUnit(@Nonnull IStorageUnitType type);

	/**
	 * Creation of {@link Fichier} and storage of associated file from inputStream into storage
	 */
	@Nonnull
	Fichier addFichier(@Nullable String filename, @Nonnull IFichierType fichierType, @Nonnull InputStream inputStream);

	/**
	 * Deletion of {@link Fichier} and associated file
	 */
	void removeFichier(@Nonnull Fichier fichier);

	void invalidateFichier(@Nonnull Fichier fichier);

	/**
	 * Get file associated to {@link Fichier}
	 */
	@Nonnull
	File getFile(@Nonnull Fichier fichier);

	@Nonnull
	List<StorageConsistency> checkConsistency(@Nonnull StorageUnit unit);

}

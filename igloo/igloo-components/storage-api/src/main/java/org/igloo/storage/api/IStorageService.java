package org.igloo.storage.api;

import java.io.File;
import java.io.InputStream;

import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.IFichierType;

import javax.annotation.Nonnull;

public interface IStorageService {

	/**
	 * Creation of {@link Fichier} and storage of associated file from inputStream into storage
	 */
	@Nonnull
	public Fichier addFichier(@Nonnull IFichierType fichierType, @Nonnull InputStream inputStream);

	/**
	 * Deletion of {@link Fichier} and associated file
	 */
	public void removeFichier(@Nonnull Fichier fichier);

	public void invalidateFichier(@Nonnull Fichier fichier);

	/**
	 * Get file associated to {@link Fichier}
	 */
	@Nonnull
	public File getFile(@Nonnull Fichier fichier);

}

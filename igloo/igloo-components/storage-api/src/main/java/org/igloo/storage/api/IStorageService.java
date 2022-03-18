package org.igloo.storage.api;

import java.io.File;
import java.io.InputStream;

import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.atomic.IFichierType;

import javax.annotation.Nonnull;

public interface IStorageService {

	@Nonnull
	public Fichier addFichier(@Nonnull IFichierType fichierType, @Nonnull InputStream inputStream);

	public void removeFichier(@Nonnull Fichier fichier);

	@Nonnull
	public File getFile(@Nonnull Fichier fichier);

}

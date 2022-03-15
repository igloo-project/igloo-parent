package org.igloo.storage.api;

import java.io.File;
import java.io.InputStream;

import javax.persistence.EntityManager;

import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.IFichierType;

public interface IStorageService {

	public Fichier addFichier(EntityManager entityManager, IFichierType fichierType, InputStream inputStream);

	public Fichier removeFichier(Fichier fichier);

	public File getFichier(Fichier fichier);

}

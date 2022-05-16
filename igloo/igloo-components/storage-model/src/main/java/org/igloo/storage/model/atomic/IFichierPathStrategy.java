package org.igloo.storage.model.atomic;

import org.igloo.storage.model.Fichier;

public interface IFichierPathStrategy {

	String getPath(Fichier fichier);

}

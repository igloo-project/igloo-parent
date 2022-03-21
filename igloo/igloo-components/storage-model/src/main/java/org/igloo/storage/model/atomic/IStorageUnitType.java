package org.igloo.storage.model.atomic;

import java.io.Serializable;
import java.util.Set;

import org.igloo.storage.model.Fichier;
import org.igloo.storage.model.StorageUnit;
import org.iglooproject.jpa.business.generic.model.IMappableInterface;

/**
 * <p>Used to handle {@link StorageUnit} selection when we want to store an new {@link Fichier}. There is only
 * one {@link StorageUnitStatus#ALIVE} for any {@link IStorageUnitType}. Due to run conditions, it may be possible that
 * simultaneous transactions store {@link Fichier} inconsistently during {@link StorageUnit} switch.</p>
 * 
 * Each project must implement this interface with an {@link Enum}.
 */
public interface IStorageUnitType extends IMappableInterface, Serializable {

	String getDescription();

	String getPath();

	boolean accept(IFichierType type);

	Set<IFichierType> getAcceptedFichierTypes();

	IFichierPathStrategy getFichierPathStrategy();

}

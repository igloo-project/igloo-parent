package org.igloo.storage.model;

import java.io.Serializable;

/**
 * Type used to organize file storage.
 */
public interface IFichierType extends Serializable {

	String getName();

	String getDescription();

}

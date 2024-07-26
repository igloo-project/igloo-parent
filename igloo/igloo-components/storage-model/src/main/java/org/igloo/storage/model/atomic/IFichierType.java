package org.igloo.storage.model.atomic;

import java.io.Serializable;
import org.iglooproject.jpa.business.generic.model.IMappableInterface;

/** Type used to organize file storage. */
public interface IFichierType extends IMappableInterface, Serializable {

  String getDescription();

  String getPath();
}

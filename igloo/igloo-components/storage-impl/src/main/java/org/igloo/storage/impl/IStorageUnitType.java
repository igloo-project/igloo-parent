package org.igloo.storage.impl;

import org.igloo.storage.model.IFichierType;

public interface IStorageUnitType {

	boolean accept(IFichierType type);

}

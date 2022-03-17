package org.igloo.storage.impl;

import org.igloo.storage.model.IFichierType;
import org.igloo.storage.model.StorageUnit;

public interface IStorageStrategy {

	public StorageUnit select(IFichierType type);

}

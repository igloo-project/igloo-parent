package test;

import java.util.Set;

import org.igloo.storage.model.atomic.IFichierPathStrategy;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;

public enum StorageUnitType implements IStorageUnitType {

	TYPE1,
	TYPE2,
	TYPE3;

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getPath() {
		return null;
	}

	@Override
	public boolean accept(IFichierType type) {
		return true;
	}

	@Override
	public Set<IFichierType> getAcceptedFichierTypes() {
		return Set.of();
	}

	@Override
	public IFichierPathStrategy getFichierPathStrategy() {
		return null;
	}

}

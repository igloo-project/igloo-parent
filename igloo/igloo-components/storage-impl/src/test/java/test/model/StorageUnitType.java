package test.model;

import java.util.Set;

import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;

public enum StorageUnitType implements IStorageUnitType {

	TYPE_1(Set.of(FichierType1.CONTENT1, FichierType1.CONTENT2, FichierType2.CONTENT4)),
	TYPE_2(Set.of(FichierType2.CONTENT3));

	private final Set<IFichierType> acceptedFichierTypes;

	private StorageUnitType(Set<IFichierType> acceptedFichierTypes) {
		this.acceptedFichierTypes = acceptedFichierTypes;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDescription() {
		return name();
	}

	@Override
	public boolean accept(IFichierType type) {
		return acceptedFichierTypes.contains(type);
	}

}

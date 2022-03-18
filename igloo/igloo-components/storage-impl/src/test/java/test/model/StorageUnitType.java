package test.model;

import java.util.Set;

import org.igloo.storage.impl.FichierPathStrategy;
import org.igloo.storage.impl.LegacyFichierPathStrategy;
import org.igloo.storage.model.atomic.IFichierPathStrategy;
import org.igloo.storage.model.atomic.IFichierType;
import org.igloo.storage.model.atomic.IStorageUnitType;

public enum StorageUnitType implements IStorageUnitType {

	TYPE_1("type-1", Set.of(FichierType1.CONTENT1, FichierType1.CONTENT2, FichierType2.CONTENT4), new FichierPathStrategy(1)),
	TYPE_2("type-2", Set.of(FichierType2.CONTENT3), new LegacyFichierPathStrategy(1));

	private final String path;
	private final Set<IFichierType> acceptedFichierTypes;
	private final IFichierPathStrategy fichierPathStrategy;

	private StorageUnitType(String path, Set<IFichierType> acceptedFichierTypes, IFichierPathStrategy fichierPathStrategy) {
		this.path = path;
		this.acceptedFichierTypes = acceptedFichierTypes;
		this.fichierPathStrategy = fichierPathStrategy;
	}

	@Override
	public String getPath() {
		return path;
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

	@Override
	public IFichierPathStrategy getFichierPathStrategy() {
		return fichierPathStrategy;
	}

}

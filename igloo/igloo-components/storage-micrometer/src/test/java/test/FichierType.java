package test;

import org.igloo.storage.model.atomic.IFichierType;

public enum FichierType implements IFichierType {

	FTYPE1,
	FTYPE2,
	FTYPE3;

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

}

package test.model;

import org.igloo.storage.model.IFichierType;

public enum FichierType1 implements IFichierType {
	CONTENT1,
	CONTENT2;

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDescription() {
		return name();
	}
}

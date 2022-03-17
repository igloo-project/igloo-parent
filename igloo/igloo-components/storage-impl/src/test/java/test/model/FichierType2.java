package test.model;

import org.igloo.storage.model.atomic.IFichierType;

public enum FichierType2 implements IFichierType {
	CONTENT3,
	CONTENT4;

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getDescription() {
		return name();
	}
}

package org.igloo.storage.integration;

import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;

public class WicketStorageSettings implements IWicketStorageSettings {

	private final String mountPath;

	public WicketStorageSettings() {
		this("/common/storage/fichier/${" + CommonParameters.ID + "}");
	}

	/**
	 * @param mountPath The path to use to mount resource; it is advised to include {@link CommonParameters#ID}
	 *                  parameter in your path.
	 */
	public WicketStorageSettings(String mountPath) {
		this.mountPath = mountPath;
	}

	@Override
	public String getMountPath() {
		return mountPath;
	}

}

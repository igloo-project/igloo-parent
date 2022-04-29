package org.igloo.storage.integration;

import org.iglooproject.wicket.more.link.descriptor.parameter.CommonParameters;

public class WicketStorageSettings implements IWicketStorageSettings {

	private final String mountPath;
	private final String downloadMountPath;

	public WicketStorageSettings() {
		this("/common/storage/fichier/${" + CommonParameters.ID + "}", "/common/storage/fichier/download/${" + CommonParameters.ID + "}");
	}

	/**
	 * @param mountPath The path to use to mount resource; it is advised to include {@link CommonParameters#ID}
	 *                  parameter in your path.
	 * @param downloadMountPath The path to use to download (Content-Disposition: Attachment) resource; it is advised to
	 *                  include {@link CommonParameters#ID} parameter in your path.
	 */
	public WicketStorageSettings(String mountPath, String downloadMountPath) {
		this.mountPath = mountPath;
		this.downloadMountPath = downloadMountPath;
	}

	@Override
	public String getMountPath() {
		return mountPath;
	}

	@Override
	public String getDownloadMountPath() {
		return downloadMountPath;
	}

}

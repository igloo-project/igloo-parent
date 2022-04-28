package org.igloo.storage.integration;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.protocol.http.WebApplication;
import org.igloo.storage.integration.wicket.FichierFileStorageWebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WicketStorage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WicketStorage.class);

	private static final MetaDataKey<IWicketStorageSettings> STORAGE_SETTINGS_METADATA_KEY = new MetaDataKey<IWicketStorageSettings>() {
		private static final long serialVersionUID = 1L;
	};

	private WicketStorage() {} //NOSONAR

	public static void install(WebApplication app) {
		install(app, new WicketStorageSettings());
	}

	public static void install(WebApplication app, IWicketStorageSettings settings) {
		final IWicketStorageSettings existingSettings = settings(app);
		
		if (existingSettings == null) {
			app.setMetaData(STORAGE_SETTINGS_METADATA_KEY, settings);
			app.mountResource(settings.getMountPath(), FichierFileStorageWebResource.get());
			app.mountResource(settings.getDownloadMountPath(), FichierFileStorageWebResource.attachment());
			LOGGER.info("initialize wicket storage with given settings: {}", settings);
		}
	}

	private static IWicketStorageSettings settings(final Application app) {
		return app.getMetaData(STORAGE_SETTINGS_METADATA_KEY);
	}
}

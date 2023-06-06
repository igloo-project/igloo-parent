package org.igloo.storage.integration;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.protocol.http.WebApplication;
import org.igloo.monitoring.wicket.SpringBackedMonitoringResource;
import org.igloo.storage.integration.wicket.FichierFileStorageWebResource;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WicketStorage {

	private static final Logger LOGGER = LoggerFactory.getLogger(WicketStorage.class);

	private static final MetaDataKey<IWicketStorageSettings> STORAGE_SETTINGS_METADATA_KEY = new MetaDataKey<IWicketStorageSettings>() {
		private static final long serialVersionUID = 1L;
	};

	private WicketStorage() {} //NOSONAR

	public static void install(WebApplication app, IPropertyService propertyService) {
		install(app, new WicketStorageSettings(
				// do not use getAsString; we want the converted value, not the raw value !
				propertyService.get(StoragePropertyIds.WEB_URL),
				propertyService.get(StoragePropertyIds.WEB_DOWNLOAD_URL),
				propertyService.get(StoragePropertyIds.MONITORING_ENABLED) && propertyService.get(StoragePropertyIds.MONITORING_WICKET_ENABLED)
		));
	}

	public static void install(WebApplication app, IWicketStorageSettings settings) {
		final IWicketStorageSettings existingSettings = settings(app);
		
		if (existingSettings == null) {
			app.setMetaData(STORAGE_SETTINGS_METADATA_KEY, settings);
			app.mountResource(settings.getMountPath(), FichierFileStorageWebResource.get());
			app.mountResource(settings.getDownloadMountPath(), FichierFileStorageWebResource.attachment());
			LOGGER.info("initialize wicket storage with given settings: {}", settings);
			
			if (settings.isSupervisionPagesEnabled()) {
				try {
					mountSupervision(app);
				} catch (NoClassDefFoundError e) {
					throw new IllegalStateException("Error mounting storage wicket supervision pages, check storage-micrometer and igloo-monitoring-page dependencies", e);
				}
			}
		}
	}

	private static void mountSupervision(WebApplication app) {
		app.mountResource("/monitoring/storage/failures/", SpringBackedMonitoringResource.get(StorageAutoConfiguration.HEALTH_FAILURES));
		app.mountResource("/monitoring/storage/size/", SpringBackedMonitoringResource.get(StorageAutoConfiguration.HEALTH_SIZE));
	}

	private static IWicketStorageSettings settings(final Application app) {
		return app.getMetaData(STORAGE_SETTINGS_METADATA_KEY);
	}
}

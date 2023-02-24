package org.iglooproject.sass.config;

import org.iglooproject.sass.service.StaticResourceHelper;

public interface ISassConfigurationProvider {

	boolean isAutoprefixerEnabled();

	default boolean useStatic() {
		return false;
	}

	default String getResourcePath() {
		return StaticResourceHelper.DEFAULT_STATIC_SCSS_RESOURCE_PATH;
	}

}

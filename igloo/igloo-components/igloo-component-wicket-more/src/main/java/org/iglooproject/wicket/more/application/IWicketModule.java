package org.iglooproject.wicket.more.application;

import java.util.List;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.settings.JavaScriptLibrarySettings;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.wicket.request.mapper.StaticResourceMapper;

public interface IWicketModule {

	default void updateJavaScriptLibrarySettings(JavaScriptLibrarySettings javaScriptLibrarySettings) {
		// nothing to do
	}

	void updateSelect2ApplicationSettings(org.wicketstuff.select2.ApplicationSettings select2ApplicationSettings);

	void addResourceReplacements(CoreWicketApplication application);

	void updateResourceBundles(ResourceBundles resourceBundles);

	List<StaticResourceMapper> listStaticResources();

	void updateResourceSettings(ResourceSettings resourceSettings);

	void registerImportScopes();

	default StaticResourceMapper staticResourceMapper(final String path, final Class<?> clazz) {
		return new StaticResourceMapper("/static" + path, clazz);
	}

}

package org.iglooproject.wicket.more.application;

import java.util.List;

import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.wicket.more.css.lesscss.service.ILessCssService;
import org.iglooproject.wicket.more.css.scss.service.IScssService;
import org.iglooproject.wicket.request.mapper.StaticResourceMapper;

public interface IWicketModule {

	void addResourceReplacements(CoreWicketApplication application);

	List<StaticResourceMapper> listStaticResources();

	void updateResourceSettings(ResourceSettings resourceSettings);

	void registerLessImportScopes(ILessCssService lessCssService);

	void registerScssImportScopes(IScssService scssService);

	default StaticResourceMapper staticResourceMaper(final String path, final Class<?> clazz) {
		return new StaticResourceMapper("/static" + path, clazz);
	}

}

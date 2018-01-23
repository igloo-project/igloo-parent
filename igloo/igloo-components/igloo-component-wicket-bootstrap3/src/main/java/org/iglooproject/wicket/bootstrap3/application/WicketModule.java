package org.iglooproject.wicket.bootstrap3.application;

import java.util.List;

import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.wicket.bootstrap3.console.resources.CoreWicketConsoleResources;
import org.iglooproject.wicket.bootstrap3.console.template.style.CoreConsoleCssScope;
import org.iglooproject.wicket.bootstrap3.markup.html.template.css.bootstrap.CoreBootstrap3CssScope;
import org.iglooproject.wicket.bootstrap3.markup.html.template.css.bootstrap.fontawesome.CoreFontAwesome4CssScope;
import org.iglooproject.wicket.bootstrap3.markup.html.template.css.bootstrap.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.application.IWicketModule;
import org.iglooproject.wicket.more.css.lesscss.service.ILessCssService;
import org.iglooproject.wicket.more.css.scss.service.IScssService;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.request.mapper.StaticResourceMapper;
import org.springframework.stereotype.Service;
import org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference;

import com.google.common.collect.ImmutableList;

@Service
public class WicketModule implements IWicketModule {

	@Override
	public void addResourceReplacements(CoreWicketApplication application) {
		application.addResourceReplacement(WiQueryCoreThemeResourceReference.get(), JQueryUiCssResourceReference.get());
	}

	@Override
	public List<StaticResourceMapper> listStaticResources() {
		return ImmutableList.of(
				staticResourceMaper("/common", AbstractWebPageTemplate.class),
				staticResourceMaper("/font-awesome", CoreFontAwesome4CssScope.class)
		);
	}

	@Override
	public void updateResourceSettings(ResourceSettings resourceSettings) {
		resourceSettings.getStringResourceLoaders().addAll(
				0, // Override the keys in existing resource loaders with the following
				ImmutableList.of(
						new ClassStringResourceLoader(CoreWicketConsoleResources.class)
				)
		);
	}

	@Override
	public void registerLessImportScopes(ILessCssService lessCssService) {
		lessCssService.registerImportScope("core-bs3", CoreBootstrap3CssScope.class);
		lessCssService.registerImportScope("core-console", CoreConsoleCssScope.class);
		lessCssService.registerImportScope("core-font-awesome", CoreFontAwesome4CssScope.class);
	}

	@Override
	public void registerScssImportScopes(IScssService scssService) {
	}

}

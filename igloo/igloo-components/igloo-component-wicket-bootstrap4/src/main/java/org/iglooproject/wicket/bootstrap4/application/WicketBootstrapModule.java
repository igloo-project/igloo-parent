package org.iglooproject.wicket.bootstrap4.application;

import java.util.List;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.sass.service.IScssService;
import org.iglooproject.wicket.bootstrap4.console.resources.CoreWicketConsoleResources;
import org.iglooproject.wicket.bootstrap4.console.template.style.CoreConsoleCssScope;
import org.iglooproject.wicket.bootstrap4.markup.html.template.css.bootstrap.CoreBootstrap4CssScope;
import org.iglooproject.wicket.bootstrap4.markup.html.template.css.fontawesome.CoreFontAwesomeCssScope;
import org.iglooproject.wicket.bootstrap4.markup.html.template.css.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.BootstrapModalJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.BootstrapModalMoreJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.jqueryui.JQueryUIJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.select2.Select2JavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.select2.Select2MoreJavaScriptResourceReference;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.application.IWicketModule;
import org.iglooproject.wicket.more.markup.html.template.AbstractWebPageTemplate;
import org.iglooproject.wicket.request.mapper.StaticResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wicketstuff.select2.ApplicationSettings;

import com.google.common.collect.ImmutableList;

@Service
public class WicketBootstrapModule implements IWicketModule {

	@Autowired
	private IScssService scssService;

	@Override
	public void updateSelect2ApplicationSettings(ApplicationSettings select2ApplicationSettings) {
		// Don't include css files from wicketstuff-select2.
		// We take care of Select2 css file and Select2 Bootstrap scss files on our side.
		// We also override select2 js file to choose specific version and also fix stuff.
		select2ApplicationSettings
			.setIncludeCss(false)
			.setJavascriptReferenceFull(Select2JavaScriptResourceReference.get());
	}

	@Override
	public void addResourceReplacements(CoreWicketApplication application) {
		application.addResourceReplacement(org.wicketstuff.wiquery.ui.JQueryUIJavaScriptResourceReference.get(), JQueryUIJavaScriptResourceReference.get());
		application.addResourceReplacement(org.wicketstuff.wiquery.ui.themes.WiQueryCoreThemeResourceReference.get(), JQueryUiCssResourceReference.get());
	}

	@Override
	public void updateResourceBundles(ResourceBundles resourceBundles) {
		resourceBundles
			.addJavaScriptBundle(getClass(), "modal-bundle.js",
				BootstrapModalJavaScriptResourceReference.get(),
				BootstrapModalMoreJavaScriptResourceReference.get()
			);
		
		resourceBundles
			.addJavaScriptBundle(getClass(), "select2-bundle.js",
				Select2JavaScriptResourceReference.get(),
				Select2MoreJavaScriptResourceReference.get()
			);
	}

	@Override
	public List<StaticResourceMapper> listStaticResources() {
		return ImmutableList.of(
			staticResourceMapper("/common", AbstractWebPageTemplate.class),
			staticResourceMapper("/font-awesome", CoreFontAwesomeCssScope.class)
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
	public void registerImportScopes() {
		scssService.registerImportScope("core-bs4", CoreBootstrap4CssScope.class);
		scssService.registerImportScope("core-fa", CoreFontAwesomeCssScope.class);
		scssService.registerImportScope("core-console", CoreConsoleCssScope.class);
	}

}

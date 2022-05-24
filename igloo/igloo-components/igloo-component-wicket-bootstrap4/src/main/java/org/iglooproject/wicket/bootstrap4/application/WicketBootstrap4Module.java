package org.iglooproject.wicket.bootstrap4.application;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.sass.service.IScssService;
import org.iglooproject.wicket.bootstrap4.markup.html.template.css.bootstrap.CoreBootstrap4CssScope;
import org.iglooproject.wicket.bootstrap4.markup.html.template.css.jqueryui.JQueryUiCssResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.BootstrapModalJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.bootstrap.modal.BootstrapModalMoreJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap4.markup.html.template.js.jqueryui.JQueryUIJavaScriptResourceReference;
import org.iglooproject.wicket.more.application.CoreWicketApplication;
import org.iglooproject.wicket.more.application.IWicketModule;

public class WicketBootstrap4Module implements IWicketModule {

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
		
		
	}

	@Override
	public void updateResourceSettings(ResourceSettings resourceSettings) {
		// Nothing
	}

	@Override
	public void registerImportScopes(IScssService scssService) {
		scssService.registerImportScope("core-bs4", CoreBootstrap4CssScope.class);
	}

}

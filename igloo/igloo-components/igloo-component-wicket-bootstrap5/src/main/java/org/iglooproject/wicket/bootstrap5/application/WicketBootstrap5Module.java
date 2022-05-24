package org.iglooproject.wicket.bootstrap5.application;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.sass.service.IScssService;
import org.iglooproject.wicket.bootstrap5.markup.html.template.css.bootstrap.CoreBootstrap5CssScope;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.BootstrapJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.confirm.BootstrapConfirmJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.modal.BootstrapModalMoreJavaScriptResourceReference;
import org.iglooproject.wicket.bootstrap5.markup.html.template.js.bootstrap.tab.BootstrapTabMoreJavaScriptResourceReference;
import org.iglooproject.wicket.more.application.IWicketModule;

public class WicketBootstrap5Module implements IWicketModule {

	@Override
	public void updateResourceBundles(ResourceBundles resourceBundles) {
		resourceBundles
			.addJavaScriptBundle(getClass(), "bootstrap-bundle.js",
				BootstrapJavaScriptResourceReference.get(),
				BootstrapModalMoreJavaScriptResourceReference.get(),
				BootstrapTabMoreJavaScriptResourceReference.get(),
				BootstrapConfirmJavaScriptResourceReference.get()
			);
	}

	@Override
	public void registerImportScopes(IScssService scssService) {
		scssService.registerImportScope("core-bs5", CoreBootstrap5CssScope.class);
	}

	@Override
	public void updateResourceSettings(ResourceSettings resourceSettings) {
		// Nothing
	}

}

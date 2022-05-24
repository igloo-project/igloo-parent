package org.iglooproject.wicket.bootstrap5.console;

import java.util.List;

import org.apache.wicket.ResourceBundles;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.sass.service.IScssService;
import org.iglooproject.wicket.bootstrap5.console.resources.CoreWicketConsoleResources;
import org.iglooproject.wicket.bootstrap5.console.template.style.CoreConsoleCssScope;
import org.iglooproject.wicket.more.application.IWicketModule;

public class WicketConsoleModule implements IWicketModule {

	@Override
	public void updateResourceBundles(ResourceBundles resourceBundles) {
		// Nothing
	}

	@Override
	public void updateResourceSettings(ResourceSettings resourceSettings) {
		resourceSettings.getStringResourceLoaders().addAll(
				0, // Override the keys in existing resource loaders with the following
				List.of(
					new ClassStringResourceLoader(CoreWicketConsoleResources.class)
				)
			);
	}

	@Override
	public void registerImportScopes(IScssService scssService) {
		scssService.registerImportScope("core-console", CoreConsoleCssScope.class);
	}

}

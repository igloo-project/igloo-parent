package igloo.cache.console;

import java.util.List;

import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuItem;

import igloo.cache.wicket.page.ConsoleMaintenanceCachesResources;
import igloo.cache.wicket.page.ConsoleMaintenanceCachesPage;
import igloo.console.template.ConsoleConfiguration;
import igloo.console.template.IConsolePageProvider;

public class ConsoleMaintenanceCachesPageProvider implements IConsolePageProvider {

	@Override
	public void install(ConsoleConfiguration consoleConfiguration, ResourceSettings resourceSettings) {
		consoleConfiguration.insertMenu(
			new ConsoleMenuItem("maintenanceCacheMenuItem",
				"console.maintenance.caches", "caches", ConsoleMaintenanceCachesPage.class),
			section -> "maintenanceMenuSection".equals(section.getName()),
			menu -> "maintenanceGestionMenuItem".equals(menu.getName()),
			false,
			true);
		resourceSettings.getStringResourceLoaders().addAll(
				0, // Override the keys in existing resource loaders with the following
				List.of(
					new ClassStringResourceLoader(ConsoleMaintenanceCachesResources.class)
				)
			);
	}

}

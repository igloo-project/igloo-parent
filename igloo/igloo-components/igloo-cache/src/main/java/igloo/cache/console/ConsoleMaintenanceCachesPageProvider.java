package igloo.cache.console;

import igloo.cache.wicket.page.ConsoleMaintenanceCachesPage;
import igloo.cache.wicket.page.ConsoleMaintenanceCachesResources;
import igloo.console.template.ConsoleConfiguration;
import igloo.console.template.IConsolePageProvider;
import java.util.List;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.apache.wicket.settings.ResourceSettings;
import org.iglooproject.wicket.more.console.common.model.ConsoleMenuItem;

public class ConsoleMaintenanceCachesPageProvider implements IConsolePageProvider {

  @Override
  public void install(
      ConsoleConfiguration consoleConfiguration, ResourceSettings resourceSettings) {
    consoleConfiguration.insertMenu(
        new ConsoleMenuItem(
            "maintenanceCacheMenuItem",
            "console.navigation.maintenance.caches",
            "caches",
            ConsoleMaintenanceCachesPage.class),
        section -> "maintenanceMenuSection".equals(section.getName()),
        menu -> "maintenanceManagementMenuItem".equals(menu.getName()),
        false,
        true);
    resourceSettings
        .getStringResourceLoaders()
        .addAll(
            0, // Override the keys in existing resource loaders with the following
            List.of(new ClassStringResourceLoader(ConsoleMaintenanceCachesResources.class)));
  }
}

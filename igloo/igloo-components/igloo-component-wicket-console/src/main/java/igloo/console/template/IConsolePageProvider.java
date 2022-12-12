package igloo.console.template;

import org.apache.wicket.settings.ResourceSettings;

public interface IConsolePageProvider {

	void install(ConsoleConfiguration consoleConfiguration, ResourceSettings resourceSettings);

}

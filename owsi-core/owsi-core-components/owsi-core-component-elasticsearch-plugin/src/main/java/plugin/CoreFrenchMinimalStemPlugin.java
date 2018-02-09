package plugin;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import index.CoreFrenchMinimalStemFilterFactory;

/**
 * Example of a plugin.
 */
public class CoreFrenchMinimalStemPlugin extends Plugin implements ActionPlugin, AnalysisPlugin {

	private final Settings settings;

	public CoreFrenchMinimalStemPlugin(Settings settings) {
		super();
		this.settings = settings;
	}

	@Override
	public Map<String, AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
		final Map<String, AnalysisProvider<TokenFilterFactory>> extra = new HashMap<>();
		extra.put("corefrenchminimalstem", (indexSettings, env, name, settings) -> new CoreFrenchMinimalStemFilterFactory(indexSettings, name, settings));
		return extra;
	}

}

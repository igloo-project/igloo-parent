package plugin;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.plugins.Plugin;

import index.CoreFrenchMinimalStemFilterFactory;

/**
 * Example of a plugin.
 */
public class CoreFrenchMinimalStemPlugin extends Plugin {

    private final Settings settings;

    public CoreFrenchMinimalStemPlugin(Settings settings) {
        this.settings = settings;
    }

    @Override
    public String name() {
        return "jvm-example";
    }

    @Override
    public String description() {
        return "A plugin that extends all extension points";
    }

    public void onModule(AnalysisModule module) {
    	module.addTokenFilter("corefrenchminimalstem", (Class<? extends TokenFilterFactory>) CoreFrenchMinimalStemFilterFactory.class);
    }

}

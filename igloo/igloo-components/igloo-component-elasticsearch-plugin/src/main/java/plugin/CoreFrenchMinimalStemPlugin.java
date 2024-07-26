package plugin;

import index.CoreFrenchMinimalStemFilterFactory;
import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.iglooproject.lucene.analysis.french.CoreFrenchMinimalStemFilter;

public class CoreFrenchMinimalStemPlugin extends Plugin implements ActionPlugin, AnalysisPlugin {

  @Override
  public Map<String, AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
    final Map<String, AnalysisProvider<TokenFilterFactory>> extra = new HashMap<>();
    extra.put(
        CoreFrenchMinimalStemFilter.STEMMER_NAME,
        (indexSettings, env, name, settings) ->
            new CoreFrenchMinimalStemFilterFactory(indexSettings, name, settings));
    return extra;
  }
}

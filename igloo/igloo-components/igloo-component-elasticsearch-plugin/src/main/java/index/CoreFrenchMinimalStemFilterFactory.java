package index;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.iglooproject.lucene.analysis.french.CoreFrenchMinimalStemFilter;

public class CoreFrenchMinimalStemFilterFactory extends AbstractTokenFilterFactory {

  @Inject
  public CoreFrenchMinimalStemFilterFactory(
      IndexSettings indexSettings, @Assisted String name, @Assisted Settings settings) {
    super(indexSettings, name, settings);
  }

  @Override
  public TokenStream create(TokenStream input) {
    return new CoreFrenchMinimalStemFilter(input);
  }
}

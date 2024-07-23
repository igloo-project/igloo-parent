package igloo.hibernateconfig.elasticsearch;

import igloo.hibernateconfig.lucene.CoreLuceneAnalyzersDefinitionProvider;
import org.hibernate.search.analyzer.definition.LuceneAnalysisDefinitionRegistryBuilder;

public class CoreLuceneClientAnalyzersDefinitionProvider
    extends CoreLuceneAnalyzersDefinitionProvider {

  public static final String ANALYZER_NAME_PREFIX = "ES_";

  @Override
  public void register(LuceneAnalysisDefinitionRegistryBuilder builder) {
    registerWithPrefix(ANALYZER_NAME_PREFIX, builder);
  }
}

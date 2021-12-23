package igloo.hibernateconfig.elasticsearch;

import org.apache.lucene.analysis.Analyzer;

public interface LuceneEmbeddedAnalyzerRegistry {

	Analyzer getRegistryAnalyzer(String analyzerName);

}
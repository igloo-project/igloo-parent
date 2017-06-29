package fr.openwide.core.jpa.hibernate.analyzers;

import org.hibernate.search.analyzer.definition.LuceneAnalyzerDefinitionRegistryBuilder;

public class CoreLuceneClientAnalyzersDefinitionProvider extends CoreLuceneAnalyzersDefinitionProvider {

	public static final String ANALYZER_NAME_PREFIX = "ES_";

	@Override
	public void register(LuceneAnalyzerDefinitionRegistryBuilder builder) {
		registerWithPrefix(ANALYZER_NAME_PREFIX, builder);
	}

}

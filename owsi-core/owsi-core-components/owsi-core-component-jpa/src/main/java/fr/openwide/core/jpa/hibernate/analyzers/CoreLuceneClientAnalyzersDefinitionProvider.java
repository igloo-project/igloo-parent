package fr.openwide.core.jpa.hibernate.analyzers;

import org.hibernate.search.analyzer.definition.LuceneAnalyzerDefinitionRegistryBuilder;

public class CoreLuceneClientAnalyzersDefinitionProvider extends CoreLuceneAnalyzersDefinitionProvider {

	@Override
	public void register(LuceneAnalyzerDefinitionRegistryBuilder builder) {
		registerWithPrefix("ES_", builder);
	}

}

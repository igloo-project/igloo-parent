package org.iglooproject.jpa.hibernate.analyzers;

import org.apache.lucene.analysis.Analyzer;

public interface LuceneEmbeddedAnalyzerRegistry {

	Analyzer getAnalyzer(String analyzerName);

}
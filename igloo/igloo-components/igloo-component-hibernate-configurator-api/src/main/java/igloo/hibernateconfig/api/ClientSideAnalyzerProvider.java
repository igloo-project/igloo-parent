package igloo.hibernateconfig.api;

import org.apache.lucene.analysis.Analyzer;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.spi.IndexedTypeIdentifier;

public interface ClientSideAnalyzerProvider {

  Analyzer getAnalyzer(String analyzerName);

  Analyzer getAnalyzer(
      ExtendedSearchIntegrator searchIntegrator, IndexedTypeIdentifier indexedType);
}

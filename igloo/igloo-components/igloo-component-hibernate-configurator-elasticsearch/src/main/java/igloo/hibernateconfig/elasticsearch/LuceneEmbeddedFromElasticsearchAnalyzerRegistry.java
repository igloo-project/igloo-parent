package igloo.hibernateconfig.elasticsearch;

import static org.hibernate.search.util.impl.ClassLoaderHelper.instanceFromClass;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharFilterFactory;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoaderAware;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.search.analyzer.definition.impl.LuceneAnalysisDefinitionRegistryBuilderImpl;
import org.hibernate.search.analyzer.definition.impl.SimpleLuceneAnalysisDefinitionRegistry;
import org.hibernate.search.analyzer.impl.LuceneAnalyzerReference;
import org.hibernate.search.analyzer.impl.ScopedLuceneAnalyzerReference;
import org.hibernate.search.analyzer.impl.SimpleLuceneAnalyzerReference;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.CharFilterDef;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.hibernate.search.cfg.spi.ParameterAnnotationsReader;
import org.hibernate.search.elasticsearch.analyzer.impl.ElasticsearchAnalyzerReference;
import org.hibernate.search.elasticsearch.analyzer.impl.ScopedElasticsearchAnalyzerReference;
import org.hibernate.search.engine.impl.TokenizerChain;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.engine.service.spi.ServiceManager;
import org.hibernate.search.hcore.util.impl.ContextHelper;
import org.hibernate.search.spi.IndexedTypeIdentifier;
import org.hibernate.search.util.impl.HibernateSearchResourceLoader;
import org.hibernate.search.util.impl.PassThroughAnalyzer;

import igloo.hibernateconfig.api.ClientSideAnalyzerProvider;
import igloo.hibernateconfig.lucene.CoreLuceneAnalyzersDefinitionProvider;

public class LuceneEmbeddedFromElasticsearchAnalyzerRegistry implements LuceneEmbeddedAnalyzerRegistry, ClientSideAnalyzerProvider {

	private static final String LUCENE_VERSION_PARAM = "luceneMatchVersion";

	private String luceneMatchVersion = org.hibernate.search.cfg.Environment.DEFAULT_LUCENE_MATCH_VERSION.toString();

	private Map<String, Analyzer> analyzers = new HashMap<>();

	private final EntityManagerFactory entityManagerFactory;

	public LuceneEmbeddedFromElasticsearchAnalyzerRegistry(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public Analyzer getAnalyzer(String analyzerName) {
		if ("default".equals(analyzerName)) {
			return PassThroughAnalyzer.INSTANCE;
		} else {
			return getRegistryAnalyzer(analyzerName);
		}
	}

	@Override
	public Analyzer getAnalyzer(ExtendedSearchIntegrator searchIntegrator, IndexedTypeIdentifier indexedType) {
		ScopedElasticsearchAnalyzerReference scopedAnalyzer = (ScopedElasticsearchAnalyzerReference) searchIntegrator.getAnalyzerReference(indexedType);
		try {
			// these properties are package protected ! Use a bypass !
			ElasticsearchAnalyzerReference globalAnalyzer = (ElasticsearchAnalyzerReference) FieldUtils.getField(ScopedElasticsearchAnalyzerReference.class, "globalAnalyzerReference", true).get(scopedAnalyzer);
			@SuppressWarnings("unchecked")
			Map<String, ElasticsearchAnalyzerReference> analyzers = (Map<String, ElasticsearchAnalyzerReference>) FieldUtils.getField(ScopedElasticsearchAnalyzerReference.class, "scopedAnalyzerReferences", true).get(scopedAnalyzer);
			
			Map<String, LuceneAnalyzerReference> luceneAnalyzers = new HashMap<>();
			for (Entry<String, ElasticsearchAnalyzerReference> analyzer : analyzers.entrySet()) {
				luceneAnalyzers.put(analyzer.getKey(), new SimpleLuceneAnalyzerReference(getAnalyzer(analyzer.getValue().getAnalyzerName(null))));
			}
			LuceneAnalyzerReference luceneGlobalAnalyzer = new SimpleLuceneAnalyzerReference(getAnalyzer(globalAnalyzer.getAnalyzerName(null)));
			return new ScopedLuceneAnalyzerReference.Builder(luceneGlobalAnalyzer, luceneAnalyzers).build().getAnalyzer();
		} catch (IllegalAccessException e) {
			throw new RuntimeException("illegal access on scoped analyzer", e);
		}
	}

	@Override
	public Analyzer getRegistryAnalyzer(String analyzerName) {
		if (analyzers.containsKey(analyzerName)) {
			return analyzers.get(analyzerName);
		} else {
			throw new RuntimeException(String.format("Analyzer %s not found", analyzerName));
		}
	}

	@PostConstruct
	private void init() {
		LuceneAnalysisDefinitionRegistryBuilderImpl builder = new LuceneAnalysisDefinitionRegistryBuilderImpl();
		new CoreLuceneAnalyzersDefinitionProvider().register(builder);
		SimpleLuceneAnalysisDefinitionRegistry registry = builder.build();
		ServiceManager serviceManager = ContextHelper.getSearchIntegratorBySF(entityManagerFactory.unwrap(SessionFactory.class)).getServiceManager();
		ResourceLoader resourceLoader = new HibernateSearchResourceLoader( serviceManager );
		
		for (Entry<String, AnalyzerDef> entry : registry.getAnalyzerDefinitions().entrySet()) {
			try {
				Analyzer analyzer = buildAnalyzer(entry.getValue(), resourceLoader);
				analyzers.put(entry.getKey(), analyzer);
			} catch (IOException e) {
				throw new IllegalStateException(String.format("Error building analyzer %s", entry.getKey()));
			}
		}
	}
	
	//
	// Code after this ligne comes from org.hibernate.search.analyzer.impl.LuceneAnalyzerBuilder
	//
	
	private Analyzer buildAnalyzer(AnalyzerDef analyzerDef, ResourceLoader resourceLoader) throws IOException {
	TokenizerDef tokenizer = analyzerDef.tokenizer();
		TokenizerFactory tokenizerFactory = buildAnalysisComponent(
				TokenizerFactory.class, tokenizer.factory(), tokenizer.params(), resourceLoader );
	
		return buildAnalyzer( tokenizerFactory, analyzerDef.charFilters(), analyzerDef.filters(), resourceLoader );
	}

	private Analyzer buildAnalyzer(TokenizerFactory tokenizerFactory,
			CharFilterDef[] charFilterDefs, TokenFilterDef[] filterDefs, ResourceLoader resourceLoader) throws IOException {
		final int tokenFiltersLength = filterDefs.length;
		TokenFilterFactory[] filters = new TokenFilterFactory[tokenFiltersLength];
		for ( int index = 0; index < tokenFiltersLength; index++ ) {
			TokenFilterDef filterDef = filterDefs[index];
			filters[index] = buildAnalysisComponent( TokenFilterFactory.class,
					filterDef.factory(),
					filterDef.params(), resourceLoader );
		}

		final int charFiltersLength = charFilterDefs.length;
		CharFilterFactory[] charFilters = new CharFilterFactory[charFiltersLength];
		for ( int index = 0; index < charFiltersLength; index++ ) {
			CharFilterDef charFilterDef = charFilterDefs[index];
			charFilters[index] = buildAnalysisComponent( CharFilterFactory.class,
					charFilterDef.factory(), charFilterDef.params(), resourceLoader );
		}

		return new TokenizerChain( charFilters, tokenizerFactory, filters );
	}

	private <T> T buildAnalysisComponent(Class<T> expectedFactoryClass,
			Class<? extends T> factoryClass,
			Parameter[] parameters, ResourceLoader resourceLoader) throws IOException {
		final Map<String, String> tokenMapsOfParameters = getMapOfParameters( parameters, luceneMatchVersion );
		T tokenizerFactory = instanceFromClass(
				expectedFactoryClass,
				factoryClass,
				"Tokenizer factory",
				tokenMapsOfParameters
		);
		injectResourceLoader( tokenizerFactory, tokenMapsOfParameters, resourceLoader );
		return tokenizerFactory;
	}

	private void injectResourceLoader(Object processor, Map<String, String> mapOfParameters, ResourceLoader resourceLoader) throws IOException {
		if ( processor instanceof ResourceLoaderAware ) {
			((ResourceLoaderAware) processor).inform(resourceLoader);
		}
	}

	private static Map<String, String> getMapOfParameters(Parameter[] params, String luceneMatchVersion) {
		Map<String, String> mapOfParams = ParameterAnnotationsReader.toNewMutableMap( params );
		mapOfParams.put( LUCENE_VERSION_PARAM, luceneMatchVersion.toString() );
		return mapOfParams;
	}

}

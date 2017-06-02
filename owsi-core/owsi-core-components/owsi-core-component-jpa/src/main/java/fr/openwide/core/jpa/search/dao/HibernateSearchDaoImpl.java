package fr.openwide.core.jpa.search.dao;

import static fr.openwide.core.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_ELASTICSEARCH_HOST;
import static fr.openwide.core.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_INDEXMANAGER;
import static fr.openwide.core.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE;
import static fr.openwide.core.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.hibernate.CacheMode;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.analyzer.impl.ScopedLuceneAnalyzer;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.hibernate.search.batchindexing.impl.MassIndexerImpl;
import org.hibernate.search.elasticsearch.analyzer.impl.ElasticsearchAnalyzer;
import org.hibernate.search.elasticsearch.analyzer.impl.ScopedElasticsearchAnalyzer;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.hcore.util.impl.HibernateHelper;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.spi.SearchIntegrator;
import org.hibernate.search.util.impl.PassThroughAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.config.spring.provider.IJpaPropertiesProvider;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.hibernate.analyzers.CoreLuceneClientAnalyzersDefinitionProvider;
import fr.openwide.core.spring.property.service.IPropertyService;

@Repository("hibernateSearchDao")
public class HibernateSearchDaoImpl implements IHibernateSearchDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSearchDaoImpl.class);
	
	@Autowired
	private IPropertyService propertyService;
	
	@Autowired
	private IJpaPropertiesProvider jpaPropertiesProvider;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public HibernateSearchDaoImpl() {
	}
	
	@Override
	public Analyzer getAnalyzer(String analyzerName) {
		if (jpaPropertiesProvider.isHibernateSearchElasticSearchEnabled()) {
			if ("default".equals(analyzerName)) {
				return PassThroughAnalyzer.INSTANCE;
			} else {
				return Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().unwrap(SearchIntegrator.class)
						.getAnalyzer(CoreLuceneClientAnalyzersDefinitionProvider.ANALYZER_NAME_PREFIX + analyzerName);
			}
		} else {
			return Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().unwrap(SearchIntegrator.class).getAnalyzer(analyzerName);
		}
	}
	
	@Override
	public Analyzer getAnalyzer(Class<?> entityType) {
		if (jpaPropertiesProvider.isHibernateSearchElasticSearchEnabled()) {
			ExtendedSearchIntegrator searchIntegrator = Search.getFullTextEntityManager(getEntityManager())
					.getSearchFactory().unwrap(ExtendedSearchIntegrator.class);
			ScopedElasticsearchAnalyzer scopedAnalyzer = (ScopedElasticsearchAnalyzer) searchIntegrator.getAnalyzerReference(entityType).getAnalyzer();
			
			try {
				// these properties are package protected !
				ElasticsearchAnalyzer globalAnalyzer = (ElasticsearchAnalyzer) FieldUtils.getField(ScopedElasticsearchAnalyzer.class, "globalAnalyzer", true).get(scopedAnalyzer);
				@SuppressWarnings("unchecked")
				Map<String, ElasticsearchAnalyzer> analyzers = (Map<String, ElasticsearchAnalyzer>) FieldUtils.getField(ScopedElasticsearchAnalyzer.class, "scopedAnalyzers", true).get(scopedAnalyzer);
				
				Map<String, Analyzer> luceneAnalyzers = Maps.newHashMap();
				for (Entry<String, ElasticsearchAnalyzer> analyzer : analyzers.entrySet()) {
					luceneAnalyzers.put(analyzer.getKey(), getAnalyzer(analyzer.getValue().getName(null)));
				}
				return new ScopedLuceneAnalyzer(getAnalyzer(globalAnalyzer.getName(null)) /* parameter is not used */, luceneAnalyzers);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("illegal access on scoped analyzer", e);
			}
		} else {
			return Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().unwrap(SearchIntegrator.class).getAnalyzer(entityType);
		}
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName,
			Integer limit, Integer offset, Sort sort) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern, analyzerName, offset, limit, sort);
	}

	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return search(clazz, fields, searchPattern, analyzerName, (Integer) null, (Integer) null, null);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern,
				getAnalyzer(clazz),
				null, limit, offset, sort);
	}

	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern)
			throws ServiceException {
		return search(clazz, fields, searchPattern, (Integer) null, (Integer) null, null);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName,
			Integer limit, Integer offset, Sort sort) throws ServiceException {
		return search(classes, fields, searchPattern,
				Search.getFullTextEntityManager(entityManager).getSearchFactory().getAnalyzer(analyzerName),
				null, limit, offset, sort);
	}

	@Override
	@Deprecated
	public <T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern,
			String analyzerName) throws ServiceException {
		return search(classes, fields, searchPattern, analyzerName, (Integer) null, (Integer) null, null);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern, analyzerName, additionalLuceneQuery, limit, offset, sort);
	}

	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery) throws ServiceException {
		return search(clazz, fields, searchPattern, analyzerName, additionalLuceneQuery,
				(Integer) null, (Integer) null, null);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery,
			Integer limit, Integer offset, Sort sort) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern,
				Search.getFullTextEntityManager(entityManager).getSearchFactory().getAnalyzer(clazz),
				additionalLuceneQuery, limit, offset, sort);
	}

	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery)
			throws ServiceException {
		return search(clazz, fields, searchPattern, additionalLuceneQuery, (Integer) null, (Integer) null, null);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return search(classes, fields, searchPattern,
				Search.getFullTextEntityManager(entityManager).getSearchFactory().getAnalyzer(analyzerName),
				additionalLuceneQuery, limit, offset, sort);
	}

	@Override
	@Deprecated
	public <T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern,
			String analyzerName, Query additionalLuceneQuery) throws ServiceException {
		return search(classes, fields, searchPattern, analyzerName, additionalLuceneQuery, (Integer) null, (Integer) null, null);
	}
	
	@SuppressWarnings("unchecked")
	@Deprecated
	private <T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, Analyzer analyzer,
			Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		if (!StringUtils.hasText(searchPattern)) {
			return Collections.emptyList();
		}
		
		try {
			FullTextEntityManager fullTextSession = Search.getFullTextEntityManager(entityManager);
			
			MultiFieldQueryParser parser = getMultiFieldQueryParser(fullTextSession, fields, MultiFieldQueryParser.AND_OPERATOR, analyzer);
			
			BooleanQuery.Builder bqBuilder = new BooleanQuery.Builder();
			bqBuilder.add(parser.parse(searchPattern), BooleanClause.Occur.MUST);
			
			if (additionalLuceneQuery != null) {
				bqBuilder.add(additionalLuceneQuery, BooleanClause.Occur.MUST);
			}
			
			FullTextQuery hibernateQuery = fullTextSession.createFullTextQuery(bqBuilder.build(), Iterables.toArray(classes, Class.class));
			if (offset != null) {
				hibernateQuery.setFirstResult(offset);
			}
			if (limit != null) {
				hibernateQuery.setMaxResults(limit);
			}
			if (sort != null) {
				hibernateQuery.setSort(sort);
			} else if (offset != null || limit != null) {
				LOGGER.warn("La requête ne spécifie pas de sort mais spécifie une limite ou un offset.");
			}
			
			return (List<T>) hibernateQuery.getResultList();
		} catch(ParseException e) {
			throw new ServiceException(String.format("Error parsing request: %1$s", searchPattern), e);
		} catch (RuntimeException e) {
			throw new ServiceException(String.format("Error executing search: %1$s for classes: %2$s", searchPattern, classes), e);
		}
	}

	@Override
	public void reindexAll() throws ServiceException {
		reindexClasses(Object.class);
	}
	
	@Override
	public void reindexClasses(Class<?>... classes) throws ServiceException {
		try {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			
			reindexClasses(fullTextEntityManager, getIndexedRootEntities(fullTextEntityManager.getSearchFactory(),
					classes.length > 0 ? classes : new Class<?>[] { Object.class }));
		} catch (RuntimeException | InterruptedException e) {
			throw new ServiceException(e);
		}
	}
	
	protected void reindexClasses(FullTextEntityManager fullTextEntityManager, Set<Class<?>> entityClasses)
			throws InterruptedException {
		int batchSize = propertyService.get(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE);
		int loadThreads = propertyService.get(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS);
		String indexmanager = propertyService.get(HIBERNATE_SEARCH_INDEXMANAGER);
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Targets for indexing job: {}", entityClasses);
		}
		
		for (Class<?> clazz : entityClasses) {
			ProgressMonitor progressMonitor = new ProgressMonitor();
			Thread t = new Thread(progressMonitor);
			if(indexmanager.equals("elasticsearch")){
				LOGGER.info(String.format("Deleting %1$s.", clazz));
				deleteIndexBeforeStart(clazz);
				LOGGER.info(String.format("Reindexing %1$s.", clazz));
				t.start();
				MassIndexer indexer = fullTextEntityManager.createIndexer(clazz);
				indexer.batchSizeToLoadObjects(batchSize)
						.threadsToLoadObjects(loadThreads)
						.purgeAllOnStart(false) // necessary to disable DeleteByQuery plugin
						.cacheMode(CacheMode.NORMAL)
						.progressMonitor(progressMonitor)
						.startAndWait();
			} else {
				LOGGER.info(String.format("Reindexing %1$s.", clazz));
				t.start();
				MassIndexer indexer = fullTextEntityManager.createIndexer(clazz);
				indexer.batchSizeToLoadObjects(batchSize)
						.threadsToLoadObjects(loadThreads)
						.cacheMode(CacheMode.NORMAL)
						.progressMonitor(progressMonitor)
						.startAndWait();
			}
			progressMonitor.stop();
			t.interrupt();
			LOGGER.info(String.format("Reindexing %1$s done.", clazz));
		}
	}
	
	private void deleteIndexBeforeStart(Class<?> clazz) {
		Class<?> clazzIndexed = getClazzIndexed(clazz);
		String elasticsearch_host = propertyService.get(HIBERNATE_SEARCH_ELASTICSEARCH_HOST);
		String url = elasticsearch_host + "/" + clazzIndexed.getName().toLowerCase();
		
		CloseableHttpClient httpClient = null;
		HttpDelete httpDelete = null;
		CloseableHttpResponse httpResponse = null;
		
		try {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(30000)
					.setConnectionRequestTimeout(30000)
					.setConnectTimeout(30000)
					.build();
			
			httpClient = HttpClientBuilder.create()
					.setDefaultRequestConfig(requestConfig)
					.build();			
			
			httpDelete = new HttpDelete(url);
			httpDelete.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
			
			httpResponse = httpClient.execute(httpDelete);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			
			// 200 : succès
			if (responseCode > 199 && responseCode < 300) {
				LOGGER.info(String.format("Deleting %1$s done.", clazzIndexed));
				
			// 400 : erreur client
			} else if (responseCode > 399 && responseCode < 500) {
				LOGGER.info(String.format("Deleting %1$s failed : Code " + responseCode + ", erreur client.", clazzIndexed));
				
			// 500 : erreur serveur
			} else {
				LOGGER.info(String.format("Deleting %1$s failed : Code " + responseCode + ", erreur serveur.", clazzIndexed));
				
			}
			
		} catch (Exception e) {
			LOGGER.error(String.format("Error deleting index %1$s", clazzIndexed), e);
			
		} finally {
			if (httpDelete != null) {
				httpDelete.reset();
			}
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (IOException e) {
					LOGGER.error("Erreur sur la fermeture de la réponse HTTP", e);
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					LOGGER.error("Erreur sur la fermeture du client HTTP", e);
				}
			}
		}
		
	}

	private Class<?> getClazzIndexed(Class<?> clazz) {
		Class<?> clazzTemp = clazz;
		Class<?> clazzIndexed = clazz;
		while (clazzTemp != null) {
			boolean indexed = clazzTemp.isAnnotationPresent(Indexed.class);
			if(indexed){
				clazzIndexed = clazzTemp;
			}
			clazzTemp = clazzTemp.getSuperclass();
		}
		
		return clazzIndexed;
	}

	@Override
	public Set<Class<?>> getIndexedRootEntities(Class<?>... selection) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		
		Set<Class<?>> indexedEntityClasses = new TreeSet<Class<?>>(new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return GenericEntity.DEFAULT_STRING_COLLATOR.compare(o1.getSimpleName(), o2.getSimpleName());
			}
		});
		indexedEntityClasses.addAll(getIndexedRootEntities(fullTextEntityManager.getSearchFactory(), selection));
		
		return indexedEntityClasses;
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(E entity) {
		if (entity != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.index(HibernateHelper.unproxy(entity));
		}
	}
	
	/**
	 * @see MassIndexerImpl#toRootEntities
	 */
	protected Set<Class<?>> getIndexedRootEntities(SearchFactory searchFactory, Class<?>... selection) {
		ExtendedSearchIntegrator searchIntegrator = searchFactory.unwrap(ExtendedSearchIntegrator.class);
		
		Set<Class<?>> entities = new HashSet<Class<?>>();
		
		// first build the "entities" set containing all indexed subtypes of "selection".
		for (Class<?> entityType : selection) {
			Set<Class<?>> targetedClasses = searchIntegrator.getIndexedTypesPolymorphic(new Class[] { entityType });
			if (targetedClasses.isEmpty()) {
				String msg = entityType.getName() + " is not an indexed entity or a subclass of an indexed entity";
				throw new IllegalArgumentException(msg);
			}
			entities.addAll(targetedClasses);
		}
		
		Set<Class<?>> cleaned = new HashSet<Class<?>>();
		Set<Class<?>> toRemove = new HashSet<Class<?>>();
		
		//now remove all repeated types to avoid duplicate loading by polymorphic query loading
		for (Class<?> type : entities) {
			boolean typeIsOk = true;
			for (Class<?> existing : cleaned) {
				if (existing.isAssignableFrom(type)) {
					typeIsOk = false;
					break;
				}
				if (type.isAssignableFrom(existing)) {
					toRemove.add(existing);
				}
			}
			if (typeIsOk) {
				cleaned.add(type);
			}
		}
		cleaned.removeAll(toRemove);

		return cleaned;
	}
	
	@Deprecated
	private MultiFieldQueryParser getMultiFieldQueryParser(FullTextEntityManager fullTextEntityManager, String[] fields, Operator defaultOperator, Analyzer analyzer) {
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
		parser.setDefaultOperator(defaultOperator);
		
		return parser;
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
	
	private static final class ProgressMonitor implements MassIndexerProgressMonitor, Runnable {
		
		private static final Logger LOGGER = LoggerFactory.getLogger(ProgressMonitor.class);
		
		private long documentsAdded;
		private int documentsBuilt;
		private int entitiesLoaded;
		private int totalCount;
		private boolean indexingCompleted;
		private boolean stopped;
		
		@Override
		public void documentsAdded(long increment) {
			this.documentsAdded += increment;
		}
		
		@Override
		public void documentsBuilt(int number) {
			this.documentsBuilt += number;
		}
		
		@Override
		public void entitiesLoaded(int size) {
			this.entitiesLoaded += size;
		}
		
		@Override
		public void addToTotalCount(long count) {
			this.totalCount += count;
		}
		
		@Override
		public void indexingCompleted() {
			this.indexingCompleted = true;
		}
		
		public void stop() {
			this.stopped = true;
			log();
		}
		
		@Override
		public void run() {
			if (LOGGER.isDebugEnabled()) {
				try {
					while (true) {
						log();
						Thread.sleep(15000);
						if (indexingCompleted) {
							LOGGER.debug("Indexing done");
							break;
						}
					}
				} catch (RuntimeException | InterruptedException e) {
					if (!stopped) {
						LOGGER.error("Error ; massindexer monitor stopped", e);
					}
					LOGGER.debug("Massindexer monitor thread interrupted");
				}
			}
		}
		
		private void log() {
			LOGGER.debug(String.format("Indexing %1$d / %2$d (entities loaded: %3$d, documents built: %4$d)",
					documentsAdded, totalCount, entitiesLoaded, documentsBuilt));
		}
	}
	
	@Override
	public void flushToIndexes() {
		Search.getFullTextEntityManager(entityManager).flushToIndexes();
	}
	
}
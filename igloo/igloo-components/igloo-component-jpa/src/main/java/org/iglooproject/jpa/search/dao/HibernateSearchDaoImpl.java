package org.iglooproject.jpa.search.dao;

import static org.iglooproject.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE;
import static org.iglooproject.jpa.property.JpaPropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.lucene.analysis.Analyzer;
import org.hibernate.CacheMode;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.analyzer.impl.ScopedLuceneAnalyzer;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.hibernate.search.batchindexing.impl.MassIndexerImpl;
import org.hibernate.search.elasticsearch.analyzer.impl.ElasticsearchAnalyzer;
import org.hibernate.search.elasticsearch.analyzer.impl.ScopedElasticsearchAnalyzer;
import org.hibernate.search.engine.integration.impl.ExtendedSearchIntegrator;
import org.hibernate.search.hcore.util.impl.HibernateHelper;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.spi.SearchIntegrator;
import org.hibernate.search.util.impl.PassThroughAnalyzer;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.config.spring.provider.IJpaPropertiesProvider;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.hibernate.analyzers.CoreLuceneClientAnalyzersDefinitionProvider;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

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
		
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Targets for indexing job: {}", entityClasses);
		}
		
		for (Class<?> clazz : entityClasses) {
			ProgressMonitor progressMonitor = new ProgressMonitor();
			Thread t = new Thread(progressMonitor);
			LOGGER.info(String.format("Reindexing %1$s.", clazz));
			t.start();
			MassIndexer indexer = fullTextEntityManager.createIndexer(clazz);
			indexer.batchSizeToLoadObjects(batchSize)
					.threadsToLoadObjects(loadThreads)
					.cacheMode(CacheMode.NORMAL)
					.progressMonitor(progressMonitor)
					.startAndWait();
			progressMonitor.stop();
			t.interrupt();
			LOGGER.info(String.format("Reindexing %1$s done.", clazz));
		}
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
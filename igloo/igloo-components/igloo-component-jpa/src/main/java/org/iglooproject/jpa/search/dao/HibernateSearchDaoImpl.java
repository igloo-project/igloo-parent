package org.iglooproject.jpa.search.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.analysis.Analyzer;
import org.hibernate.Hibernate;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository("hibernateSearchDao")
public class HibernateSearchDaoImpl implements IHibernateSearchDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSearchDaoImpl.class);
	
	@Autowired
	private IPropertyService propertyService;
	
	/**
	 * With Elasticsearch used as backend, provides client-side Lucene analyzers. Null otherwise.
	 */
//	@Autowired(required = false)
//	private ClientSideAnalyzerProvider clientSideAnalyzerProvider;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public HibernateSearchDaoImpl() {
	}
	
	@Override
	public Analyzer getAnalyzer(String analyzerName) {
		//TODO: igloo-boot
		return null;
//		if (jpaPropertiesProvider.isHibernateSearchElasticSearchEnabled()) {
//			return clientSideAnalyzerProvider.getAnalyzer(analyzerName);
//		} else {
//			ExtendedSearchIntegrator searchIntegrator = Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().unwrap(ExtendedSearchIntegrator.class);
//			SearchIntegration integration = searchIntegrator.getIntegration(LuceneEmbeddedIndexManagerType.INSTANCE);
//			return integration.getAnalyzerRegistry().getAnalyzerReference(analyzerName).unwrap(LuceneAnalyzerReference.class).getAnalyzer();
//		}
	}
	
	@Override
	public Analyzer getAnalyzer(Class<?> entityType) {
		//TODO: igloo-boot
		return null;
//		SearchFactory searchFactory = Search.getFullTextEntityManager(getEntityManager()).getSearchFactory();
//		ExtendedSearchIntegrator searchIntegrator = searchFactory.unwrap(ExtendedSearchIntegrator.class);
//		IndexedTypeIdentifier indexedType = getIndexBoundType(entityType, searchIntegrator);
//		
//		if (jpaPropertiesProvider.isHibernateSearchElasticSearchEnabled()) {
//			return clientSideAnalyzerProvider.getAnalyzer(searchIntegrator, indexedType);
//		} else {
//			return Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().unwrap(SearchIntegrator.class).getAnalyzer(indexedType);
//		}
	}

	/**
	 * Extracted from ConnectedQueryContextBuilder
	 */
//	private IndexedTypeIdentifier getIndexBoundType(Class<?> entityType, ExtendedSearchIntegrator factory) {
//		IndexedTypeIdentifier realtype = new PojoIndexedTypeIdentifier( entityType );
//		if ( factory.getIndexBinding( realtype ) != null ) {
//			return realtype;
//		}
//
//		IndexedTypeSet indexedSubTypes = factory.getIndexedTypesPolymorphic( realtype.asTypeSet() );
//
//		if ( !indexedSubTypes.isEmpty() ) {
//			return indexedSubTypes.iterator().next();
//		}
//
//		return null;
//	}

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
			if (e instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			throw new ServiceException(e);
		}
	}
	
	protected void reindexClasses(FullTextEntityManager fullTextEntityManager, Set<Class<?>> entityClasses)
			throws InterruptedException {
		//TODO: igloo-boot
		return;
//		int batchSize = propertyService.get(HIBERNATE_SEARCH_REINDEX_BATCH_SIZE);
//		int loadThreads = propertyService.get(HIBERNATE_SEARCH_REINDEX_LOAD_THREADS);
//		
//		if (LOGGER.isInfoEnabled()) {
//			LOGGER.info("Targets for indexing job: {}", entityClasses);
//		}
//		
//		for (Class<?> clazz : entityClasses) {
//			ProgressMonitor progressMonitor = new ProgressMonitor();
//			Thread t = new Thread(progressMonitor);
//			LOGGER.info(String.format("Reindexing %1$s.", clazz));
//			t.start();
//			MassIndexer indexer = fullTextEntityManager.createIndexer(clazz);
//			indexer.batchSizeToLoadObjects(batchSize)
//					.threadsToLoadObjects(loadThreads)
//					.cacheMode(CacheMode.NORMAL)
//					.progressMonitor(progressMonitor)
//					.startAndWait();
//			progressMonitor.stop();
//			t.interrupt();
//			LOGGER.info(String.format("Reindexing %1$s done.", clazz));
//		}
	}

	@Override
	public Set<Class<?>> getIndexedRootEntities(Class<?>... selection) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		
		Set<Class<?>> indexedEntityClasses = new TreeSet<>(new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				// Legacy. French should not be considered as default locale.
				return GenericEntity.STRING_COLLATOR_FRENCH.compare(o1.getSimpleName(), o2.getSimpleName());
			}
		});
		indexedEntityClasses.addAll(getIndexedRootEntities(fullTextEntityManager.getSearchFactory(), selection));
		
		return indexedEntityClasses;
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(E entity) {
		if (entity != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.index(Hibernate.unproxy(entity));
		}
	}
	
	/**
	 * @see MassIndexerImpl#toRootEntities
	 */
	protected Set<Class<?>> getIndexedRootEntities(SearchFactory searchFactory, Class<?>... selection) {
		return Collections.emptySet();
//		ExtendedSearchIntegrator searchIntegrator = searchFactory.unwrap(ExtendedSearchIntegrator.class);
//		
//		Set<Class<?>> entities = new HashSet<>();
//		
//		// first build the "entities" set containing all indexed subtypes of "selection".
//		for (Class<?> entityType : selection) {
//			IndexedTypeSet targetedClasses = searchIntegrator.getIndexedTypesPolymorphic(IndexedTypeSets.fromClass(entityType));
//			if (targetedClasses.isEmpty()) {
//				String msg = entityType.getName() + " is not an indexed entity or a subclass of an indexed entity";
//				throw new IllegalArgumentException(msg);
//			}
//			entities.addAll(targetedClasses.toPojosSet());
//		}
//		
//		Set<Class<?>> cleaned = new HashSet<>();
//		Set<Class<?>> toRemove = new HashSet<>();
//		
//		//now remove all repeated types to avoid duplicate loading by polymorphic query loading
//		for (Class<?> type : entities) {
//			boolean typeIsOk = true;
//			for (Class<?> existing : cleaned) {
//				if (existing.isAssignableFrom(type)) {
//					typeIsOk = false;
//					break;
//				}
//				if (type.isAssignableFrom(existing)) {
//					toRemove.add(existing);
//				}
//			}
//			if (typeIsOk) {
//				cleaned.add(type);
//			}
//		}
//		cleaned.removeAll(toRemove);
//		
//		return cleaned;
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
					if (e instanceof InterruptedException) {
						Thread.currentThread().interrupt();
					}
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

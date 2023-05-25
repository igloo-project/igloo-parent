package org.iglooproject.jpa.search.dao;

import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_REINDEX_BATCH_SIZE;
import static org.iglooproject.jpa.property.JpaSearchPropertyIds.HIBERNATE_SEARCH_REINDEX_LOAD_THREADS;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.hibernate.CacheMode;
import org.hibernate.Hibernate;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.entity.SearchIndexedEntity;
import org.hibernate.search.mapper.orm.session.SearchSession;
//import org.hibernate.search.jpa.Search;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.service.IPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class HibernateSearchDaoImpl implements IHibernateSearchDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSearchDaoImpl.class);
	
	@Autowired
	private IPropertyService propertyService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public HibernateSearchDaoImpl() {
	}
	
	@Override
	public Analyzer getAnalyzer(String analyzerName) {
//		V5MigrationOrmSearchIntegratorAdapter searchIntegrator = Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().unwrap(V5MigrationOrmSearchIntegratorAdapter.class);
//		SearchIntegration integration = searchIntegrator.getIntegration(LuceneEmbeddedIndexManagerType.INSTANCE);
//		return integration.getAnalyzerRegistry().getAnalyzerReference(analyzerName).unwrap(LuceneAnalyzerReference.class).getAnalyzer();
		return new StandardAnalyzer();
	}
	
	@Override
	public Analyzer getAnalyzer(Class<?> entityType) {
//		SearchFactory searchFactory = Search.getFullTextEntityManager(getEntityManager()).getSearchFactory();
//		ExtendedSearchIntegrator searchIntegrator = searchFactory.unwrap(ExtendedSearchIntegrator.class);
//		IndexedTypeIdentifier indexedType = getIndexBoundType(entityType, searchIntegrator);
//		
//		return Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().unwrap(SearchIntegrator.class).getAnalyzer(indexedType);
		return new StandardAnalyzer();
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
			FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
			
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
		return Search.mapping(entityManager.getEntityManagerFactory())
			.allIndexedEntities() // TODO: igloo-boot : là je récupère toutes les entités indexées, c'est peut-être trop bourrin
			.stream()
			.map(SearchIndexedEntity::javaClass)
			.filter(indexedClass -> Set.of(selection).stream().anyMatch(selectionClass -> selectionClass.isAssignableFrom(indexedClass)))
			.collect(Collectors.toCollection(() -> Sets.newTreeSet((o1, o2) -> GenericEntity.STRING_COLLATOR_FRENCH.compare(o1.getSimpleName(), o2.getSimpleName()))));
	}
	
	//TODO: iglo-boot : migration vers api HS 6 OK
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(E entity) {
		if (entity != null) {
			SearchSession session = Search.session(entityManager);
			session.indexingPlan().addOrUpdate(Hibernate.unproxy(entity));
		}
	}
	
	/**
	 * @see MassIndexerImpl#toRootEntities
	 */
	protected Set<Class<?>> getIndexedRootEntities(SearchFactory searchFactory, Class<?>... selection) {
		return getIndexedRootEntities(selection);
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
		org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager).flushToIndexes();
	}
}

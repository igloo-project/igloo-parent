package fr.openwide.core.jpa.search.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.util.Version;
import org.hibernate.CacheMode;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.hibernate.search.engine.spi.SearchFactoryImplementor;
import org.hibernate.search.impl.MassIndexerImpl;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.config.CoreConfigurer;

@Repository("hibernateSearchDao")
public class HibernateSearchDaoImpl implements IHibernateSearchDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSearchDaoImpl.class);
	
	@Autowired
	private CoreConfigurer configurer;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public HibernateSearchDaoImpl() {
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName,
			Integer limit, Integer offset, Sort sort) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern, analyzerName, offset, limit, sort);
	}

	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return search(clazz, fields, searchPattern, analyzerName, (Integer) null, (Integer) null, null);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern,
				Search.getFullTextEntityManager(entityManager).getSearchFactory().getAnalyzer(clazz),
				null, limit, offset, sort);
	}

	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern)
			throws ServiceException {
		return search(clazz, fields, searchPattern, (Integer) null, (Integer) null, null);
	}
	
	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName,
			Integer limit, Integer offset, Sort sort) throws ServiceException {
		return search(classes, fields, searchPattern,
				Search.getFullTextEntityManager(entityManager).getSearchFactory().getAnalyzer(analyzerName),
				null, limit, offset, sort);
	}

	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern,
			String analyzerName) throws ServiceException {
		return search(classes, fields, searchPattern, analyzerName, (Integer) null, (Integer) null, null);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern, analyzerName, additionalLuceneQuery, limit, offset, sort);
	}

	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery) throws ServiceException {
		return search(clazz, fields, searchPattern, analyzerName, additionalLuceneQuery,
				(Integer) null, (Integer) null, null);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery,
			Integer limit, Integer offset, Sort sort) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern,
				Search.getFullTextEntityManager(entityManager).getSearchFactory().getAnalyzer(clazz),
				additionalLuceneQuery, limit, offset, sort);
	}

	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery)
			throws ServiceException {
		return search(clazz, fields, searchPattern, additionalLuceneQuery, (Integer) null, (Integer) null, null);
	}
	
	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return search(classes, fields, searchPattern,
				Search.getFullTextEntityManager(entityManager).getSearchFactory().getAnalyzer(analyzerName),
				additionalLuceneQuery, limit, offset, sort);
	}

	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern,
			String analyzerName, Query additionalLuceneQuery) throws ServiceException {
		return search(classes, fields, searchPattern, analyzerName, additionalLuceneQuery, (Integer) null, (Integer) null, null);
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, Analyzer analyzer,
			Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		if (!StringUtils.hasText(searchPattern)) {
			return Collections.emptyList();
		}
		
		try {
			FullTextEntityManager fullTextSession = Search.getFullTextEntityManager(entityManager);
			
			MultiFieldQueryParser parser = getMultiFieldQueryParser(fullTextSession, fields, MultiFieldQueryParser.AND_OPERATOR, analyzer);
			
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(parser.parse(searchPattern), BooleanClause.Occur.MUST);
			
			if (additionalLuceneQuery != null) {
				booleanQuery.add(additionalLuceneQuery, BooleanClause.Occur.MUST);
			}
			
			FullTextQuery hibernateQuery = fullTextSession.createFullTextQuery(booleanQuery, classes.toArray(new Class<?>[classes.size()]));
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
		} catch (Exception e) {
			throw new ServiceException(String.format("Error executing search: %1$s for classes: %2$s", searchPattern, classes), e);
		}
	}

	@Override
	public void reindexAll() throws ServiceException {
		try {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			
			int batchSize = configurer.getHibernateSearchReindexBatchSize();
			int fetchingThreads = configurer.getHibernateSearchReindexFetchingThreads();
			int loadThreads = configurer.getHibernateSearchReindexLoadThreads();
			
			for (Class<?> clazz : getIndexedRootEntities(fullTextEntityManager.getSearchFactory(), Object.class)) {
				LOGGER.debug(String.format("Reindexing %1$s.", clazz));
				ProgressMonitor progressMonitor = new ProgressMonitor();
				Thread t = new Thread(progressMonitor);
				t.start();
				MassIndexer indexer = fullTextEntityManager.createIndexer(clazz);
				indexer.batchSizeToLoadObjects(batchSize)
						.threadsForSubsequentFetching(fetchingThreads)
						.threadsToLoadObjects(loadThreads)
						.cacheMode(CacheMode.NORMAL)
						.progressMonitor(progressMonitor)
						.startAndWait();
				progressMonitor.stop();
				t.interrupt();
				LOGGER.debug(String.format("Reindexing %1$s done.", clazz));
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * @see MassIndexerImpl#toRootEntities
	 */
	protected Set<Class<?>> getIndexedRootEntities(SearchFactory searchFactory, Class<?>... selection) {
		if (searchFactory instanceof SearchFactoryImplementor) {
			SearchFactoryImplementor searchFactoryImplementor = (SearchFactoryImplementor) searchFactory;
			
			Set<Class<?>> entities = new HashSet<Class<?>>();
			
			// first build the "entities" set containing all indexed subtypes of "selection".
			for (Class<?> entityType : selection) {
				Set<Class<?>> targetedClasses = searchFactoryImplementor.getIndexedTypesPolymorphic(new Class[] { entityType });
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
			
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("Targets for indexing job: {}", cleaned);
			}
	
			return cleaned;
		} else {
			throw new IllegalArgumentException("searchFactory should be a SearchFactoryImplementor");
		}
	}
	
	private MultiFieldQueryParser getMultiFieldQueryParser(FullTextEntityManager fullTextEntityManager, String[] fields, Operator defaultOperator, Analyzer analyzer) {
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_35, fields, analyzer);
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
			this.entitiesLoaded = size;
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
				} catch (Exception e) {
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
}
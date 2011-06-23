package fr.openwide.core.hibernate.search.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.CacheMode;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.engine.SearchFactoryImplementor;
import org.hibernate.search.impl.MassIndexerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.spring.config.CoreConfigurer;

@Repository("hibernateSearchDao")
public class HibernateSearchDaoImpl extends HibernateDaoSupport implements HibernateSearchDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSearchDaoImpl.class);
	
	@Autowired
	private CoreConfigurer configurer;
	
	@Autowired
	public HibernateSearchDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern, analyzerName);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern, Search.getFullTextSession(getSession()).getSearchFactory().getAnalyzer(clazz), null);
	}
	
	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return search(classes, fields, searchPattern, Search.getFullTextSession(getSession()).getSearchFactory().getAnalyzer(analyzerName), null);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern, analyzerName, additionalLuceneQuery);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern, Search.getFullTextSession(getSession()).getSearchFactory().getAnalyzer(clazz), additionalLuceneQuery);
	}
	
	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName,
			Query additionalLuceneQuery) throws ServiceException {
		return search(classes, fields, searchPattern, Search.getFullTextSession(getSession()).getSearchFactory().getAnalyzer(analyzerName), additionalLuceneQuery);
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, Analyzer analyzer, Query additionalLuceneQuery) throws ServiceException {
		if (!StringUtils.hasText(searchPattern)) {
			return Collections.emptyList();
		}
		
		try {
			FullTextSession fullTextSession = Search.getFullTextSession(getSession());
			
			MultiFieldQueryParser parser = getMultiFieldQueryParser(fullTextSession, fields, MultiFieldQueryParser.AND_OPERATOR, analyzer);
			
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(parser.parse(searchPattern), BooleanClause.Occur.MUST);
			
			if (additionalLuceneQuery != null) {
				booleanQuery.add(additionalLuceneQuery, BooleanClause.Occur.MUST);
			}
			
			FullTextQuery hibernateQuery = fullTextSession.createFullTextQuery(booleanQuery, classes.toArray(new Class<?>[classes.size()]));
			
			return (List<T>) hibernateQuery.list();
		} catch(ParseException e) {
			throw new ServiceException(String.format("Error parsing request: %1$s", searchPattern), e);
		} catch (Exception e) {
			throw new ServiceException(String.format("Error executing search: %1$s for classes: %2$s", searchPattern, classes), e);
		}
	}
	
	@Override
	public void reindexAll() throws ServiceException {
		try {
			FullTextSession fullTextSession = Search.getFullTextSession(getSession());
			
			for (Class<?> clazz : getIndexedRootEntities(fullTextSession.getSearchFactory(), Object.class)) {
				fullTextSession.createIndexer(clazz)
						.batchSizeToLoadObjects(configurer.getHibernateSearchReindexBatchSize())
						.threadsForSubsequentFetching(configurer.getHibernateSearchReindexFetchingThreads())
						.threadsToLoadObjects(configurer.getHibernateSearchReindexLoadThreads())
						.cacheMode(CacheMode.NORMAL)
						.startAndWait();
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * @see MassIndexerImpl#toRootEntities
	 */
	protected static Set<Class<?>> getIndexedRootEntities(SearchFactory searchFactory, Class<?>... selection) {
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
			
			LOGGER.info("Targets for indexing job: {}", cleaned);
	
			return cleaned;
		} else {
			throw new IllegalArgumentException("searchFactory should be a SearchFactoryImplementor");
		}
	}
	
	private MultiFieldQueryParser getMultiFieldQueryParser(FullTextSession fullTextSession, String[] fields, Operator defaultOperator, Analyzer analyzer) {
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, analyzer);
		parser.setDefaultOperator(defaultOperator);
		
		return parser;
	}
	
}
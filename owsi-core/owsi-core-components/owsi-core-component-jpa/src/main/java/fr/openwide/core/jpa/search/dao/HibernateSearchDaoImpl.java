package fr.openwide.core.jpa.search.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.util.Version;
import org.hibernate.CacheMode;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import fr.openwide.core.jpa.exception.ServiceException;

@Repository("hibernateSearchDao")
public class HibernateSearchDaoImpl implements HibernateSearchDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public HibernateSearchDaoImpl() {
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException {
		List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>(1);
		classes.add(clazz);
		
		return search(classes, fields, searchPattern, Search.getFullTextEntityManager(entityManager).getSearchFactory().getAnalyzer(clazz));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, Analyzer analyzer) throws ServiceException {
		if (!StringUtils.hasText(searchPattern)) {
			return Collections.emptyList();
		}
		
		try {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			
			MultiFieldQueryParser parser = getMultiFieldQueryParser(fullTextEntityManager, fields, MultiFieldQueryParser.AND_OPERATOR, analyzer);
			
			org.apache.lucene.search.Query luceneQuery = parser.parse(searchPattern);
			FullTextQuery hibernateQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, classes.toArray(new Class<?>[classes.size()]));
			
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
			
			fullTextEntityManager.createIndexer()
					.batchSizeToLoadObjects(30)
					.threadsForSubsequentFetching(8)
					.threadsToLoadObjects(4)
					.cacheMode(CacheMode.NORMAL)
					.startAndWait();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private MultiFieldQueryParser getMultiFieldQueryParser(FullTextEntityManager fullTextEntityManager, String[] fields, Operator defaultOperator, Analyzer analyzer) {
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, analyzer);
		parser.setDefaultOperator(defaultOperator);
		
		return parser;
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
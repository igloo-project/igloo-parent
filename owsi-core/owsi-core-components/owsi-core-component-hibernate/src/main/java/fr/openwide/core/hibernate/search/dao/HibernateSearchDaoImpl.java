package fr.openwide.core.hibernate.search.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.CacheMode;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import fr.openwide.core.hibernate.exception.ServiceException;

@Repository("hibernateSearchDao")
public class HibernateSearchDaoImpl extends HibernateDaoSupport implements HibernateSearchDao {
	
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
		
		return search(classes, fields, searchPattern, Search.getFullTextSession(getSession()).getSearchFactory().getAnalyzer(clazz));
	}
	
	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return search(classes, fields, searchPattern, Search.getFullTextSession(getSession()).getSearchFactory().getAnalyzer(analyzerName));
	}
	
	@SuppressWarnings("unchecked")
	private <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, Analyzer analyzer) throws ServiceException {
		if (!StringUtils.hasText(searchPattern)) {
			return Collections.emptyList();
		}
		
		try {
			FullTextSession fullTextSession = Search.getFullTextSession(getSession());
			
			MultiFieldQueryParser parser = getMultiFieldQueryParser(fullTextSession, fields, MultiFieldQueryParser.AND_OPERATOR, analyzer);
			
			Query luceneQuery = parser.parse(searchPattern);
			FullTextQuery hibernateQuery = fullTextSession.createFullTextQuery(luceneQuery, classes.toArray(new Class<?>[classes.size()]));
			
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
			
			fullTextSession.createIndexer()
					.batchSizeToLoadObjects(30)
					.threadsForSubsequentFetching(8)
					.threadsToLoadObjects(4)
					.cacheMode(CacheMode.NORMAL)
					.startAndWait();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	private MultiFieldQueryParser getMultiFieldQueryParser(FullTextSession fullTextSession, String[] fields, Operator defaultOperator, Analyzer analyzer) {
		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_30, fields, analyzer);
		parser.setDefaultOperator(defaultOperator);
		
		return parser;
	}
	
}
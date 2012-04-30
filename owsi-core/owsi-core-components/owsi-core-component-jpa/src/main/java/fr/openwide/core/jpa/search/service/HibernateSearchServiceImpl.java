package fr.openwide.core.jpa.search.service;

import java.util.List;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.search.dao.IHibernateSearchDao;
import fr.openwide.core.spring.util.lucene.search.LuceneUtils;

@Service("hibernateSearchService")
public class HibernateSearchServiceImpl implements IHibernateSearchService {
	
	@Autowired
	private IHibernateSearchDao hibernateSearchDao;
	
	@Override
	public void reindexAll() throws ServiceException {
		hibernateSearchDao.reindexAll();
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, analyzerName);
	}
	
	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return hibernateSearchDao.search(classes, fields, searchPattern, analyzerName);
	}
	
	@Override
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern));
	}
	
	@Override
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), analyzerName);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, additionalLuceneQuery);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, analyzerName, additionalLuceneQuery);
	}
	
	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(classes, fields, searchPattern, analyzerName, additionalLuceneQuery);
	}
	
	@Override
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), additionalLuceneQuery);
	}
	
	@Override
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), analyzerName, additionalLuceneQuery);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, limit, offset, sort);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, analyzerName, limit, offset, sort);
	}
	
	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(classes, fields, searchPattern, analyzerName, limit, offset, sort);
	}
	
	@Override
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), limit, offset, sort);
	}
	
	@Override
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), analyzerName, limit, offset, sort);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, additionalLuceneQuery, limit, offset, sort);
	}
	
	@Override
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, analyzerName, additionalLuceneQuery, limit, offset, sort);
	}
	
	@Override
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(classes, fields, searchPattern, analyzerName, additionalLuceneQuery, limit, offset, sort);
	}
	
	@Override
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), additionalLuceneQuery, limit, offset, sort);
	}
	
	@Override
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), analyzerName, additionalLuceneQuery, limit, offset, sort);
	}
	
}

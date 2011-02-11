package fr.openwide.core.hibernate.search.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.core.hibernate.search.dao.HibernateSearchDao;
import fr.openwide.core.spring.util.lucene.search.LuceneUtils;

@Service("hibernateSearchService")
public class HibernateSearchServiceImpl implements HibernateSearchService {
	
	@Autowired
	private HibernateSearchDao hibernateSearchDao;
	
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
	public void reindexAll() throws ServiceException {
		hibernateSearchDao.reindexAll();
	}
	
}

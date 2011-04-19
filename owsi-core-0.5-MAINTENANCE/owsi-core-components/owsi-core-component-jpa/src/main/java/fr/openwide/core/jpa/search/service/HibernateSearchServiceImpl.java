package fr.openwide.core.jpa.search.service;

import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.search.dao.HibernateSearchDao;
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
	public <T> List<T> search(List<Class<? extends T>> classes, String[] fields, String searchPattern, Analyzer analyzer) throws ServiceException {
		return hibernateSearchDao.search(classes, fields, searchPattern, analyzer);
	}
	
	@Override
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern));
	}
	
	@Override
	public void reindexAll() throws ServiceException {
		hibernateSearchDao.reindexAll();
	}
	
}

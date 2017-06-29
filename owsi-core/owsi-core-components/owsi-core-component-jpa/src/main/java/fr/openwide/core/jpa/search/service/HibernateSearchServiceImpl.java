package fr.openwide.core.jpa.search.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;
import fr.openwide.core.jpa.business.generic.service.IEntityService;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.search.dao.IHibernateSearchDao;
import fr.openwide.core.spring.util.lucene.search.LuceneUtils;

@Service("hibernateSearchService")
public class HibernateSearchServiceImpl implements IHibernateSearchService {
	
	@Autowired
	private IHibernateSearchDao hibernateSearchDao;
	
	@Autowired
	private IEntityService entityService;
	
	@Override
	public Analyzer getAnalyzer(String analyzerName) {
		return hibernateSearchDao.getAnalyzer(analyzerName);
	}
	
	@Override
	public Analyzer getAnalyzer(Class<?> entityType) {
		return hibernateSearchDao.getAnalyzer(entityType);
	}
	
	@Override
	public void reindexAll() throws ServiceException {
		hibernateSearchDao.reindexAll();
	}
	
	@Override
	public void reindexClasses(Collection<Class<?>> classes) throws ServiceException {
		if (classes != null && !classes.isEmpty()) {
			hibernateSearchDao.reindexClasses(classes.toArray(new Class<?>[ classes.size() ]));
		}
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(E entity) {
		hibernateSearchDao.reindexEntity(entity);
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(GenericEntityReference<K, E> reference) {
		hibernateSearchDao.reindexEntity(entityService.getEntity(reference));
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(Class<E> clazz, K id) {
		hibernateSearchDao.reindexEntity(entityService.getEntity(clazz, id));
	}
	
	@Override
	public Set<Class<?>> getIndexedRootEntities() throws ServiceException {
		return hibernateSearchDao.getIndexedRootEntities(Object.class);
	}
	
	@Override
	public Set<Class<?>> getIndexedRootEntities(Collection<Class<?>> classes) throws ServiceException {
		if (classes != null && !classes.isEmpty()) {
			return hibernateSearchDao.getIndexedRootEntities(classes.toArray(new Class<?>[ classes.size() ]));
		} else {
			return Sets.newHashSet();
		}
	}
	
	@Override
	public void flushToIndexes() {
		hibernateSearchDao.flushToIndexes();
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, analyzerName);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return hibernateSearchDao.search(classes, fields, searchPattern, analyzerName);
	}
	
	@Override
	@Deprecated
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern));
	}
	
	@Override
	@Deprecated
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), analyzerName);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, additionalLuceneQuery);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, analyzerName, additionalLuceneQuery);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(classes, fields, searchPattern, analyzerName, additionalLuceneQuery);
	}
	
	@Override
	@Deprecated
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), additionalLuceneQuery);
	}
	
	@Override
	@Deprecated
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), analyzerName, additionalLuceneQuery);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, limit, offset, sort);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, analyzerName, limit, offset, sort);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(classes, fields, searchPattern, analyzerName, limit, offset, sort);
	}
	
	@Override
	@Deprecated
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), limit, offset, sort);
	}
	
	@Override
	@Deprecated
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), analyzerName, limit, offset, sort);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, additionalLuceneQuery, limit, offset, sort);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, searchPattern, analyzerName, additionalLuceneQuery, limit, offset, sort);
	}
	
	@Override
	@Deprecated
	public <T> List<T> search(Collection<Class<? extends T>> classes, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(classes, fields, searchPattern, analyzerName, additionalLuceneQuery, limit, offset, sort);
	}
	
	@Override
	@Deprecated
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), additionalLuceneQuery, limit, offset, sort);
	}
	
	@Override
	@Deprecated
	public <T> List<T> searchAutocomplete(Class<T> clazz, String[] fields, String searchPattern, String analyzerName, Query additionalLuceneQuery, Integer limit, Integer offset, Sort sort) throws ServiceException {
		return hibernateSearchDao.search(clazz, fields, LuceneUtils.getAutocompleteQuery(searchPattern), analyzerName, additionalLuceneQuery, limit, offset, sort);
	}
}

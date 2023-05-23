package org.iglooproject.jpa.search.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.business.generic.service.IEntityService;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.search.dao.IHibernateSearchDao;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;

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
	
}

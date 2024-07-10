package org.iglooproject.jpa.search.dao;

import java.io.Serializable;
import java.util.Set;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.exception.ServiceException;

public interface IHibernateSearchDao {

	void reindexAll() throws ServiceException;

	void reindexClasses(Class<?>... classes) throws ServiceException;

	Set<Class<?>> getIndexedRootEntities(Class<?>... selection);

	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(E entity);

	void flushToIndexes();

}

package org.iglooproject.jpa.search.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.ServiceException;

public interface IHibernateSearchService extends ITransactionalAspectAwareService {

  void reindexAll() throws ServiceException;

  void reindexClasses(Collection<Class<?>> classes) throws ServiceException;

  <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(
      E entity);

  <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(
      GenericEntityReference<K, E> reference);

  <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> void reindexEntity(
      Class<E> clazz, K id);

  Set<Class<?>> getIndexedRootEntities() throws ServiceException;

  Set<Class<?>> getIndexedRootEntities(Collection<Class<?>> classes) throws ServiceException;

  void flushToIndexes();
}

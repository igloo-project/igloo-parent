package org.iglooproject.jpa.business.generic.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityCollectionReference;
import org.iglooproject.jpa.business.generic.model.GenericEntityReference;
import org.iglooproject.jpa.business.generic.model.IReference;
import org.iglooproject.jpa.business.generic.model.IReferenceable;
import org.iglooproject.jpa.query.IQuery;

public interface IEntityService extends ITransactionalAspectAwareService {

  <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(
      Class<E> clazz, K id);

  <E extends GenericEntity<?, ?>> E getEntity(IReference<E> reference);

  <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E> listEntity(
      Class<E> clazz, Collection<K> ids);

  <E extends GenericEntity<?, ?>> List<E> listEntity(
      GenericEntityCollectionReference<?, E> reference);

  <E extends GenericEntity<?, ?>> IQuery<E> getQuery(
      GenericEntityCollectionReference<?, E> reference);

  /**
   * @param entity A {@link GenericEntity} (that may have been detached from the session) or a
   *     {@link GenericEntityReference}
   * @return An object representing the same entity, but which is guaranteed to be attached to the
   *     session
   */
  <E extends GenericEntity<?, ?>> E getEntity(IReferenceable<E> referenceOrEntity);

  void flush();

  void clear();

  <E extends GenericEntity<?, ?>> List<Class<? extends E>> listAssignableEntityTypes(
      Class<E> superclass);
}

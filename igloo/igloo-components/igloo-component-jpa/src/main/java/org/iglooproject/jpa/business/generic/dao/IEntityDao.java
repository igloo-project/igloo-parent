package org.iglooproject.jpa.business.generic.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityCollectionReference;
import org.iglooproject.jpa.business.generic.model.IReference;

public interface IEntityDao {

  <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(
      Class<E> clazz, K id);

  <E extends GenericEntity<?, ?>> E getEntity(IReference<E> reference);

  <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E> listEntity(
      Class<E> clazz, Collection<K> ids);

  <E extends GenericEntity<?, ?>> List<E> listEntity(
      GenericEntityCollectionReference<?, E> reference);

  void flush();

  void clear();

  <E extends GenericEntity<?, ?>> List<Class<? extends E>> listAssignableEntityTypes(
      Class<E> superclass);
}

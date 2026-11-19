package org.iglooproject.jpa.business.generic.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityCollectionReference;
import org.iglooproject.jpa.business.generic.model.IReference;
import org.jspecify.annotations.Nullable;

public interface IEntityDao {

  <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> @Nullable E getEntity(
      Class<E> clazz, K id);

  <E extends GenericEntity<?, ?>> @Nullable E getEntity(@Nullable IReference<E> reference);

  <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E> listEntity(
      Class<E> clazz, @Nullable Collection<K> ids);

  <E extends GenericEntity<?, ?>> List<E> listEntity(
      GenericEntityCollectionReference<?, E> reference);

  void flush();

  void clear();

  <E extends GenericEntity<?, ?>> List<Class<? extends E>> listAssignableEntityTypes(
      Class<E> superclass);
}

package org.iglooproject.jpa.more.business.referencedata.service;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import java.util.Comparator;
import java.util.List;
import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;

public interface IGenericReferenceDataSubService extends ITransactionalAspectAwareService {

  <E extends GenericReferenceData<?, ?>> E getById(Class<E> clazz, Long id);

  <E extends GenericReferenceData<?, ?>> void create(E entity);

  <E extends GenericReferenceData<?, ?>> void update(E entity);

  <E extends GenericReferenceData<?, ?>> void delete(E entity);

  <E extends GenericReferenceData<?, ?>> long count(Class<E> clazz);

  <E extends GenericReferenceData<?, ?>> long count(Class<E> clazz, EnabledFilter enabledFilter);

  <E extends GenericReferenceData<?, ?>> List<E> list(Class<E> clazz);

  <E extends GenericReferenceData<?, ?>> List<E> list(
      Class<E> clazz, Comparator<? super E> comparator);

  <E extends GenericReferenceData<?, ?>> List<E> list(
      Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator);

  <E extends GenericReferenceData<?, ?>> List<E> listEnabled(Class<E> clazz);

  <E extends GenericReferenceData<?, ?>> List<E> listEnabled(
      Class<E> clazz, Comparator<? super E> comparator);

  /** WARNING: only use this if unique constraints were set on the field of {@code source}. */
  <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> E getByField(
      EntityPath<E> entityPath, SimpleExpression<V> field, V fieldValue);

  /**
   * WARNING: only use this if unique constraints were set on {@code lower(field)} in the {@code
   * source} table.
   */
  <E extends GenericReferenceData<?, ?>> E getByFieldIgnoreCase(
      EntityPath<E> entityPath, StringExpression field, String fieldValue);
}

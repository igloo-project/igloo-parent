package org.iglooproject.jpa.more.business.referencedata.dao;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import java.util.Comparator;
import java.util.List;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;

public interface IGenericReferenceDataSubDao {

  <E extends GenericReferenceData<?, ?>> E getEntity(Class<E> clazz, Long id);

  <E extends GenericReferenceData<?, ?>> E getById(Class<E> clazz, Long id);

  <E extends GenericReferenceData<?, ?>> E getByNaturalId(Class<E> clazz, Object naturalId);

  <E extends GenericReferenceData<?, ?>> void update(E entity);

  <E extends GenericReferenceData<?, ?>> void save(E entity);

  <E extends GenericReferenceData<?, ?>> void delete(E entity);

  <E extends GenericReferenceData<?, ?>> E refresh(E entity);

  <E extends GenericReferenceData<?, ?>> Long count(Class<E> clazz, EnabledFilter enabledFilter);

  <E extends GenericReferenceData<?, ?>> Long count(Class<E> clazz);

  Long count(EntityPath<? extends GenericReferenceData<?, ?>> entityPath);

  <V extends Comparable<?>> Long countByField(
      EntityPath<? extends GenericReferenceData<?, ?>> entityPath,
      SimpleExpression<V> field,
      V fieldValue);

  <E extends GenericReferenceData<?, ?>> List<E> list(Class<E> clazz);

  <E extends GenericReferenceData<?, ?>> List<E> list(
      Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator);

  <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> E getByField(
      EntityPath<E> entityPath, SimpleExpression<V> field, V fieldValue);

  <E extends GenericReferenceData<?, ?>> E getByFieldIgnoreCase(
      EntityPath<E> entityPath, StringExpression field, String fieldValue);

  <E extends GenericReferenceData<?, ?>> List<E> list(EntityPath<E> entityPath);

  <E extends GenericReferenceData<?, ?>> List<E> list(
      EntityPath<E> entityPath, Long limit, Long offset);

  <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> List<E> listByField(
      EntityPath<E> entityPath,
      SimpleExpression<V> field,
      V fieldValue,
      OrderSpecifier<?> orderSpecifier);

  <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> List<E> listByField(
      EntityPath<E> entityPath,
      SimpleExpression<V> field,
      V fieldValue,
      Long limit,
      Long offset,
      OrderSpecifier<?> orderSpecifier);
}

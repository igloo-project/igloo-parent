package org.iglooproject.jpa.more.business.referencedata.dao;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.iglooproject.jpa.business.generic.dao.AbstractEntityDaoImpl;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.referencedata.model.QGenericReferenceData;
import org.springframework.stereotype.Repository;

@Repository
public class GenericReferenceDataDaoImpl extends AbstractEntityDaoImpl<GenericReferenceData<?, ?>>
    implements IGenericReferenceDataDao {

  @Override
  public <E extends GenericReferenceData<?, ?>> E getEntity(Class<E> clazz, Long id) {
    return super.getEntity(clazz, id);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> E getById(Class<E> clazz, Long id) {
    return super.getEntity(clazz, id);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> E getByNaturalId(Class<E> clazz, Object naturalId) {
    return super.getEntityByNaturalId(clazz, naturalId);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> void update(E entity) {
    super.update(entity);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> void save(E entity) {
    super.save(entity);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> void delete(E entity) {
    super.delete(entity);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> E refresh(E entity) {
    return super.refresh(entity);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> Long count(
      Class<E> clazz, EnabledFilter enabledFilter) {
    PathBuilder<GenericReferenceData<?, ?>> pathBuilder = new PathBuilder<>(clazz, "rootAlias");
    QGenericReferenceData entityPath = new QGenericReferenceData(pathBuilder);

    JPAQuery<GenericReferenceData<?, ?>> query;
    if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
      query = queryByPredicate(entityPath, entityPath.enabled.isTrue());
    } else {
      query = queryByPredicate(entityPath, null);
    }

    return query.fetchCount();
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> Long count(Class<E> clazz) {
    PathBuilder<GenericReferenceData<?, ?>> path = new PathBuilder<>(clazz, "rootAlias");
    return count(path);
  }

  @Override
  public Long count(EntityPath<? extends GenericReferenceData<?, ?>> entityPath) {
    return super.count(entityPath);
  }

  @Override
  public <V extends Comparable<?>> Long countByField(
      EntityPath<? extends GenericReferenceData<?, ?>> entityPath,
      SimpleExpression<V> field,
      V fieldValue) {
    return super.countByField(entityPath, field, fieldValue);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> List<E> list(Class<E> clazz) {
    return super.listEntity(clazz);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> List<E> list(
      Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator) {
    PathBuilder<GenericReferenceData<?, ?>> pathBuilder = new PathBuilder<>(clazz, "rootAlias");
    QGenericReferenceData entityPath = new QGenericReferenceData(pathBuilder);

    JPAQuery<GenericReferenceData<?, ?>> query;
    if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
      query = queryByPredicate(entityPath, entityPath.enabled.isTrue());
    } else {
      query = queryByPredicate(entityPath, null);
    }

    @SuppressWarnings("unchecked")
    List<E> entities = (List<E>) query.fetch();

    Collections.sort(entities, comparator);

    return entities;
  }

  @Override
  public <T extends GenericReferenceData<?, ?>> List<T> list(EntityPath<T> entityPath) {
    return super.list(entityPath);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> List<E> list(
      EntityPath<E> entityPath, Long limit, Long offset) {
    return super.list(entityPath, limit, offset);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> E getByField(
      EntityPath<E> entityPath, SimpleExpression<V> field, V fieldValue) {
    return super.getByField(entityPath, field, fieldValue);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> E getByFieldIgnoreCase(
      EntityPath<E> entityPath, StringExpression field, String fieldValue) {
    return super.getByFieldIgnoreCase(entityPath, field, fieldValue);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> List<E> listByField(
      EntityPath<E> entityPath,
      SimpleExpression<V> field,
      V fieldValue,
      OrderSpecifier<?> orderSpecifier) {
    return super.listByField(entityPath, field, fieldValue, orderSpecifier);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> List<E> listByField(
      EntityPath<E> entityPath,
      SimpleExpression<V> field,
      V fieldValue,
      Long limit,
      Long offset,
      OrderSpecifier<?> orderSpecifier) {
    return super.listByField(entityPath, field, fieldValue, limit, offset, orderSpecifier);
  }
}

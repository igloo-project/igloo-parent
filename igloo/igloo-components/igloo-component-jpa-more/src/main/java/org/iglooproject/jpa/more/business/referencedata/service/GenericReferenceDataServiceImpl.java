package org.iglooproject.jpa.more.business.referencedata.service;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import java.util.Comparator;
import java.util.List;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.dao.IGenericReferenceDataDao;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenericReferenceDataServiceImpl implements IGenericReferenceDataService {

  protected final IGenericReferenceDataDao dao;

  @Autowired
  public GenericReferenceDataServiceImpl(IGenericReferenceDataDao dao) {
    super();
    this.dao = dao;
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> E getById(Class<E> clazz, Long id) {
    return dao.getById(clazz, id);
  }

  protected <E extends GenericReferenceData<?, ?>> E getByNaturalId(
      Class<E> clazz, Object naturalId) {
    return dao.getByNaturalId(clazz, naturalId);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> void create(E entity) {
    dao.save(entity);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> void update(E entity) {
    dao.update(entity);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> void delete(E entity) {
    dao.delete(entity);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> long count(Class<E> clazz) {
    return count(clazz, EnabledFilter.ALL);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> long count(
      Class<E> clazz, EnabledFilter enabledFilter) {
    return dao.count(clazz, enabledFilter);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> List<E> list(Class<E> clazz) {
    return list(clazz, null);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> List<E> list(
      Class<E> clazz, Comparator<? super E> comparator) {
    return dao.list(clazz, EnabledFilter.ALL, comparator);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> List<E> list(
      Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator) {
    return dao.list(clazz, enabledFilter, comparator);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> List<E> listEnabled(Class<E> clazz) {
    return listEnabled(clazz, null);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> List<E> listEnabled(
      Class<E> clazz, Comparator<? super E> comparator) {
    return dao.list(clazz, EnabledFilter.ENABLED_ONLY, comparator);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> E getByField(
      EntityPath<E> entityPath, SimpleExpression<V> field, V fieldValue) {
    return dao.getByField(entityPath, field, fieldValue);
  }

  @Override
  public <E extends GenericReferenceData<?, ?>> E getByFieldIgnoreCase(
      EntityPath<E> entityPath, StringExpression field, String fieldValue) {
    return dao.getByFieldIgnoreCase(entityPath, field, fieldValue);
  }
}

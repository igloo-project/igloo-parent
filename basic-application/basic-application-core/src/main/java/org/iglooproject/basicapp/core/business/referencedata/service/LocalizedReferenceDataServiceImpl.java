package org.iglooproject.basicapp.core.business.referencedata.service;

import java.util.Comparator;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.iglooproject.basicapp.core.business.referencedata.dao.ILocalizedReferenceDataDao;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.iglooproject.jpa.more.business.referencedata.service.IGenericLocalizedReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

@Service
public class LocalizedReferenceDataServiceImpl implements ILocalizedReferenceDataService {

	@Autowired
	private IGenericLocalizedReferenceDataService genericLocalizedReferenceDataService;

	@Autowired
	public LocalizedReferenceDataServiceImpl(ILocalizedReferenceDataDao localizedReferenceDataDao) {
		super();
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> E getById(Class<E> clazz, Long id) {
		return genericLocalizedReferenceDataService.getById(clazz, id);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> void create(E entity) {
		genericLocalizedReferenceDataService.create(entity);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> void update(E entity) {
		genericLocalizedReferenceDataService.update(entity);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> void delete(E entity) {
		genericLocalizedReferenceDataService.delete(entity);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> long count(Class<E> clazz) {
		return genericLocalizedReferenceDataService.count(clazz);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> long count(Class<E> clazz, EnabledFilter enabledFilter) {
		return genericLocalizedReferenceDataService.count(clazz, enabledFilter);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> List<E> list(Class<E> clazz) {
		return genericLocalizedReferenceDataService.list(clazz);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> List<E> list(Class<E> clazz, Comparator<? super E> comparator) {
		return genericLocalizedReferenceDataService.list(clazz, comparator);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> List<E> list(Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator) {
		return genericLocalizedReferenceDataService.list(clazz, enabledFilter, comparator);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> List<E> listEnabled(Class<E> clazz) {
		return genericLocalizedReferenceDataService.listEnabled(clazz);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> List<E> listEnabled(Class<E> clazz, Comparator<? super E> comparator) {
		return genericLocalizedReferenceDataService.listEnabled(clazz, comparator);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> E getByField(EntityPath<E> entityPath, SimpleExpression<V> field, V fieldValue) throws NonUniqueResultException {
		return genericLocalizedReferenceDataService.getByField(entityPath, field, fieldValue);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> E getByFieldIgnoreCase(EntityPath<E> entityPath, StringExpression field, String fieldValue) throws NonUniqueResultException {
		return genericLocalizedReferenceDataService.getByFieldIgnoreCase(entityPath, field, fieldValue);
	}

}

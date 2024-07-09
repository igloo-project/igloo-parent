package basicapp.back.business.referencedata.dao;

import java.util.Comparator;
import java.util.List;

import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.referencedata.dao.IGenericReferenceDataDao;
import org.iglooproject.jpa.more.business.referencedata.model.GenericReferenceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

@Repository
public class ReferenceDataDaoImpl implements IReferenceDataDao {

	@Autowired
	private IGenericReferenceDataDao genericReferenceDataDao;

	@Override
	public <E extends GenericReferenceData<?, ?>> E getEntity(Class<E> clazz, Long id) {
		return genericReferenceDataDao.getEntity(clazz, id);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> E getById(Class<E> clazz, Long id) {
		return genericReferenceDataDao.getById(clazz, id);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> E getByNaturalId(Class<E> clazz, Object naturalId) {
		return genericReferenceDataDao.getByNaturalId(clazz, naturalId);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> void update(E entity) {
		genericReferenceDataDao.update(entity);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> void save(E entity) {
		genericReferenceDataDao.save(entity);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> void delete(E entity) {
		genericReferenceDataDao.delete(entity);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> E refresh(E entity) {
		return genericReferenceDataDao.refresh(entity);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> Long count(Class<E> clazz, EnabledFilter enabledFilter) {
		return genericReferenceDataDao.count(clazz, enabledFilter);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> Long count(Class<E> clazz) {
		return genericReferenceDataDao.count(clazz);
	}

	@Override
	public Long count(EntityPath<? extends GenericReferenceData<?, ?>> entityPath) {
		return genericReferenceDataDao.count(entityPath);
	}

	@Override
	public <V extends Comparable<?>> Long countByField(EntityPath<? extends GenericReferenceData<?, ?>> entityPath, SimpleExpression<V> field, V fieldValue) {
		return genericReferenceDataDao.countByField(entityPath, field, fieldValue);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> List<E> list(Class<E> clazz) {
		return genericReferenceDataDao.list(clazz);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> List<E> list(Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator) {
		return genericReferenceDataDao.list(clazz, enabledFilter, comparator);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> E getByField(EntityPath<E> entityPath, SimpleExpression<V> field, V fieldValue) {
		return genericReferenceDataDao.getByField(entityPath, field, fieldValue);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> E getByFieldIgnoreCase(EntityPath<E> entityPath, StringExpression field, String fieldValue) {
		return genericReferenceDataDao.getByFieldIgnoreCase(entityPath, field, fieldValue);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> List<E> list(EntityPath<E> entityPath) {
		return genericReferenceDataDao.list(entityPath);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>> List<E> list(EntityPath<E> entityPath, Long limit, Long offset) {
		return genericReferenceDataDao.list(entityPath, limit, offset);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> List<E> listByField(EntityPath<E> entityPath, SimpleExpression<V> field, V fieldValue, OrderSpecifier<?> orderSpecifier) {
		return genericReferenceDataDao.listByField(entityPath, field, fieldValue, orderSpecifier);
	}

	@Override
	public <E extends GenericReferenceData<?, ?>, V extends Comparable<?>> List<E> listByField(EntityPath<E> entityPath, SimpleExpression<V> field, V fieldValue, Long limit, Long offset, OrderSpecifier<?> orderSpecifier) {
		return genericReferenceDataDao.listByField(entityPath, field, fieldValue, limit, offset, orderSpecifier);
	}

}

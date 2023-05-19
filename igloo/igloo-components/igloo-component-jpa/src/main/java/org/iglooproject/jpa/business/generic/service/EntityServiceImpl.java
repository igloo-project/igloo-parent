package org.iglooproject.jpa.business.generic.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.iglooproject.jpa.business.generic.dao.IEntityDao;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityCollectionReference;
import org.iglooproject.jpa.business.generic.model.IReference;
import org.iglooproject.jpa.business.generic.model.IReferenceable;
import org.iglooproject.jpa.business.generic.query.IEntityReferenceQuery;
import org.iglooproject.jpa.query.IQuery;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityServiceImpl implements IEntityService {
	
	@Autowired
	private IEntityDao entityDao;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private ObjectFactory<IEntityReferenceQuery> entityReferenceQueryProvider;

	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(Class<E> clazz, K id) {
		return entityDao.getEntity(clazz, id);
	}
	
	@Override
	public <E extends GenericEntity<?, ?>> E getEntity(IReference<E> reference) {
		return entityDao.getEntity(reference);
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E> listEntity(Class<E> clazz, Collection<K> ids) {
		return entityDao.listEntity(clazz, ids);
	}
	
	@Override
	public <E extends GenericEntity<?, ?>> List<E> listEntity(GenericEntityCollectionReference<?, E> reference) {
		return entityDao.listEntity(reference);
	}
	
	@Override
	public <E extends GenericEntity<?, ?>> IQuery<E> getQuery(final GenericEntityCollectionReference<?, E> reference) {
		// The query must be defined as an (external) bean so that the version of entityService it uses has been proxified
		@SuppressWarnings("unchecked")
		IEntityReferenceQuery<E> query = entityReferenceQueryProvider.getObject();
		query.setReference(reference);
		return query;
	}

	@Override
	public <E extends GenericEntity<?, ?>> E getEntity(IReferenceable<E> referenceable) {
		return entityDao.getEntity(referenceable.asReference());
	}
	
	@Override
	public void flush() {
		entityDao.flush();
	}

	@Override
	public void clear() {
		entityDao.clear();
	}

	@Override
	public <E extends GenericEntity<?, ?>> List<Class<? extends E>> listAssignableEntityTypes(Class<E> superclass) {
		return entityDao.listAssignableEntityTypes(superclass);
	}

}

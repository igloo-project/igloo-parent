package fr.openwide.core.jpa.business.generic.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.dao.IEntityDao;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityCollectionReference;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;

@Service("entityService")
public class EntityServiceImpl implements IEntityService {
	
	@Autowired
	private IEntityDao entityDao;

	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(Class<E> clazz, K id) {
		return entityDao.getEntity(clazz, id);
	}
	
	@Override
	public <E extends GenericEntity<?, ?>> E getEntity(GenericEntityReference<?, E> reference) {
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
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(E entity) {
		return entityDao.getEntity(GenericEntityReference.of(entity));
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

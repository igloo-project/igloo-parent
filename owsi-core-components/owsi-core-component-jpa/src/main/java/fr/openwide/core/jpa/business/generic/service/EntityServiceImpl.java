package fr.openwide.core.jpa.business.generic.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.jpa.business.generic.dao.IEntityDao;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
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
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(GenericEntityReference<K, E> reference) {
		return entityDao.getEntity(reference);
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

}

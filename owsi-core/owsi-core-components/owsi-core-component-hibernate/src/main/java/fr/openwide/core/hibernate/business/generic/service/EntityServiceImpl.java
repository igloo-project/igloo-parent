package fr.openwide.core.hibernate.business.generic.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.openwide.core.hibernate.business.generic.dao.EntityDao;
import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

@Service("entityService")
public class EntityServiceImpl implements EntityService {
	
	@Autowired
	private EntityDao entityDao;

	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>> E getEntity(
			Class<E> clazz, K id) {
		return entityDao.getEntity(clazz, id);
	}

}

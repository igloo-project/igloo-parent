package fr.openwide.core.hibernate.business.generic.service;

import java.io.Serializable;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

public interface EntityService extends TransactionalAspectAwareService {
	
	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(Class<E> clazz, K id);

}
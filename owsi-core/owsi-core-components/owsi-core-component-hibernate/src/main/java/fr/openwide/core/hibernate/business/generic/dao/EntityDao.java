package fr.openwide.core.hibernate.business.generic.dao;

import java.io.Serializable;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

public interface EntityDao {
	
	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>> E getEntity(Class<E> clazz, K id);
	
}

package fr.openwide.core.jpa.business.generic.dao;

import java.io.Serializable;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public interface IEntityDao {
	
	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(Class<E> clazz, K id);
	
}

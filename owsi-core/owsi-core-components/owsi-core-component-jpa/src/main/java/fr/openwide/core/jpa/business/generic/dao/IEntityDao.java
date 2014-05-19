package fr.openwide.core.jpa.business.generic.dao;

import java.io.Serializable;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;

public interface IEntityDao {
	
	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(Class<E> clazz, K id);
	
	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(GenericEntityReference<K, E> reference);
	
}

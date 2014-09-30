package fr.openwide.core.jpa.business.generic.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityCollectionReference;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;

public interface IEntityService extends ITransactionalAspectAwareService {
	
	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(Class<E> clazz, K id);
	
	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(GenericEntityReference<K, E> reference);

	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E> listEntity(Class<E> clazz, Collection<K> ids);

	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E> listEntity(GenericEntityCollectionReference<K, E> reference);
	
	/**
	 * @param entity An object representing an entity that may have been detached from the session
	 * @return An object representing the same entity, but which is attached to the session
	 */
	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(E entity);
	
	void flush();
	
	void clear();

}
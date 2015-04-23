package fr.openwide.core.jpa.business.generic.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityCollectionReference;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;

public interface IEntityDao {
	
	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(Class<E> clazz, K id);
	
	<E extends GenericEntity<?, ?>> E getEntity(GenericEntityReference<?, E> reference);

	<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E> listEntity(Class<E> clazz, Collection<K> ids);
	
	<E extends GenericEntity<?, ?>> List<E> listEntity(GenericEntityCollectionReference<?, E> reference);
	
	void flush();
	
	void clear();
	
	<E extends GenericEntity<?, ?>> List<Class<? extends E>> listAssignableEntityTypes(Class<E> superclass);
}

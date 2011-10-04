package fr.openwide.core.jpa.more.util.init.dao;

import java.io.Serializable;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public interface IImportDataDao {
	
	<K extends Serializable & Comparable<K>, E extends GenericEntity<?, ?>> E getById(Class<E> clazz, K id);
	
	<E extends GenericEntity<?, ?>> void create(E entity);

}

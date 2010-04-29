package fr.openwide.hibernate.business.generic.dao;

import java.util.List;

import fr.openwide.hibernate.business.generic.model.GenericEntity;

public interface GenericEntityDao<T extends GenericEntity<T>> {

	GenericEntity<?> getEntity(Class<? extends GenericEntity<?>> clazz, Integer id);
	
	T getById(Number id);
	
	T getByField(String fieldName, Object fieldValue);
	
	void save(T entity);
	
	void update(T entity);
	
	void delete(T entity);
	
	T refresh(T entity);
	
	List<T> list();
	
	List<T> listByField(String fieldName, Object fieldValue);
	
	void evict(T object);

	Long count();

}
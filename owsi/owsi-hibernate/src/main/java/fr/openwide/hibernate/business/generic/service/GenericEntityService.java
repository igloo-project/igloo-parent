package fr.openwide.hibernate.business.generic.service;

import java.util.List;

import fr.openwide.hibernate.business.generic.model.GenericEntity;
import fr.openwide.hibernate.exception.SecurityServiceException;
import fr.openwide.hibernate.exception.ServiceException;

public interface GenericEntityService<T> {

	void save(T entity) throws ServiceException, SecurityServiceException;

	void update(T entity) throws ServiceException, SecurityServiceException;

	void create(T entity) throws ServiceException, SecurityServiceException;

	void create(T entity, boolean checkNew) throws ServiceException, SecurityServiceException;

	void delete(T entity);

	T refresh(T entity);

	T getById(Number id);

	List<T> list();

	GenericEntity<?> getEntity(Class<? extends GenericEntity<?>> clazz, Integer id);

	Long count();

}

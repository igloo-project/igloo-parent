package fr.openwide.hibernate.business.generic.service;

import java.util.List;

import fr.openwide.hibernate.business.generic.dao.GenericEntityDao;
import fr.openwide.hibernate.business.generic.model.GenericEntity;
import fr.openwide.hibernate.exception.SecurityServiceException;
import fr.openwide.hibernate.exception.ServiceException;

public abstract class GenericEntityServiceImpl<T extends GenericEntity<T>> implements
		GenericEntityService<T> {

	private GenericEntityDao<T> genericDao;

	public GenericEntityServiceImpl(GenericEntityDao<T> genericDao) {
		this.genericDao = genericDao;
	}

	@Override
	public GenericEntity<?> getEntity(
			Class<? extends GenericEntity<?>> clazz, Integer id) {
		return genericDao.getEntity(clazz, id);
	}

	@Override
	public T getById(Number id) {
		return genericDao.getById(id);
	}

	protected T getByField(String fieldName, String fieldValue) {
		return genericDao.getByField(fieldName, fieldValue);
	}
	
	/**
	 * Direct access to the save method of the DAO
	 * 
	 * In general, shouldn't be used directly, except in the
	 * test environment.
	 * 
	 * @see #create(T)
	 */
	@Override
	public void save(T entity) throws ServiceException,
			SecurityServiceException {
		genericDao.save(entity);
	}
	
	/**
	 * By default, calls the save method but this method can
	 * be overwritten to have a more complex business logic.
	 */
	@Override
	public final void create(T entity) throws ServiceException,
			SecurityServiceException {
		create(entity, true);
	}
	
	@Override
	public final void create(T entity, boolean checkNew) throws ServiceException,
			SecurityServiceException {
		if(!checkNew || entity.isNew()) {
			createEntity(entity);
		} else {
			throw new ServiceException("The entity is not a new entity. You should use update instead of create.");
		}
	}
	
	protected void createEntity(T entity) throws ServiceException, SecurityServiceException {
		save(entity);
	}
	
	@Override
	public final void update(T entity) throws ServiceException,
			SecurityServiceException {
		updateEntity(entity);
	}
	
	protected void updateEntity(T entity) throws ServiceException,
			SecurityServiceException {
		genericDao.update(entity);
	}
	
	@Override
	public void delete(T entity) {
		genericDao.delete(entity);
	}

	@Override
	public T refresh(T entity) {
		return genericDao.refresh(entity);
	}

	@Override
	public List<T> list() {
		return genericDao.list();
	}
	
	protected List<T> listByField(String fieldName, Object fieldValue) {
		return genericDao.listByField(fieldName, fieldValue);
	}
	
	@Override
	public Long count() {
		return genericDao.count();
	}

}
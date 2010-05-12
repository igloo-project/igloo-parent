/*
 * Copyright (C) 2009-2010 Open Wide
 * Contact: contact@openwide.fr
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.openwide.core.hibernate.business.generic.service;

import java.util.List;

import fr.openwide.core.hibernate.business.generic.dao.GenericEntityDao;
import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;

/**
 * <p>Implémentation de {@link GenericEntityService}</p>
 * 
 * @author Open Wide
 *
 * @param <T> type de l'entité
 */
public abstract class GenericEntityServiceImpl<T extends GenericEntity<T>> implements GenericEntityService<T> {

	private GenericEntityDao<T> genericDao;

	/**
	 * Constructeur.
	 *
	 * @param genericDao DAO associé au type d'entité
	 */
	public GenericEntityServiceImpl(GenericEntityDao<T> genericDao) {
		this.genericDao = genericDao;
	}

	@Override
	public GenericEntity<?> getEntity(Class<? extends GenericEntity<?>> clazz, Integer id) {
		return genericDao.getEntity(clazz, id);
	}

	@Override
	public T getById(Number id) {
		return genericDao.getById(id);
	}

	/**
	 * Retourne une entité à partir du nom d'un champ et de sa valeur. Il faut forcément que le champ fasse l'objet
	 * d'une contrainte d'unicité. Cette méthode ne doit pas être utilisée directement et droit être wrappée (par
	 * exemple via un getByPropriete(valeur)).
	 * 
	 * @param fieldName nom du champ
	 * @param fieldValue valeur du champ
	 * @return entité
	 */
	protected T getByField(String fieldName, String fieldValue) {
		return genericDao.getByField(fieldName, fieldValue);
	}
	
	@Override
	public void save(T entity) throws ServiceException,
			SecurityServiceException {
		genericDao.save(entity);
	}
	
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
	
	/**
	 * Met à jour l'entité. Peut éventuellement être surchargée pour implémenter des comportements plus complexes
	 * (listeners...).
	 * 
	 * @param entity entité
	 * @throws ServiceException si problème d'exécution
	 * @throws SecurityServiceException si problème de droit
	 */
	protected void updateEntity(T entity) throws ServiceException,
			SecurityServiceException {
		genericDao.update(entity);
	}
	
	@Override
	public void delete(T entity) {
		genericDao.delete(entity);
	}
	
	@Override
	public void flush() {
		genericDao.flush();
	}
	
	@Override
	public T refresh(T entity) {
		return genericDao.refresh(entity);
	}

	@Override
	public List<T> list() {
		return genericDao.list();
	}
	
	/**
	 * Renvoie la liste des entités dont le champ donné en paramètre a la bonne valeur.
	 * 
	 * @param fieldName nom du champ
	 * @param fieldValue valeur du champ
	 * @return liste d'entités
	 */
	protected List<T> listByField(String fieldName, Object fieldValue) {
		return genericDao.listByField(fieldName, fieldValue);
	}
	
	@Override
	public Long count() {
		return genericDao.count();
	}

}
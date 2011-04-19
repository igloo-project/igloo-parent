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

package fr.openwide.core.jpa.business.generic.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import fr.openwide.core.jpa.business.generic.dao.IGenericEntityDao;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.util.GenericEntityUtils;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;

/**
 * <p>Implémentation de {@link GenericEntityService}</p>
 * 
 * @author Open Wide
 *
 * @param <T> type de l'entité
 */
public abstract class GenericEntityServiceImpl<K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>>
		implements GenericEntityService<K, E> {
	
	/**
	 * Classe de l'entité, déterminé à partir des paramètres generics.
	 */
	private Class<E> objectClass;

	/**
	 * DAO
	 */
	private IGenericEntityDao<K, E> genericDao;

	/**
	 * Constructeur.
	 *
	 * @param genericDao DAO associé au type d'entité
	 */
	@SuppressWarnings("unchecked")
	public GenericEntityServiceImpl(IGenericEntityDao<K, E> genericDao) {
		this.genericDao = genericDao;
		
		this.objectClass = (Class<E>) GenericEntityUtils.getGenericEntityClassFromComponentDefinition(getClass());
	}
	
	/**
	 * Retourne la classe de l'entité.
	 * 
	 * @return classe de l'entité
	 */
	protected final Class<E> getObjectClass() {
		return objectClass;
	}

	@Override
	public E getEntity(Class<? extends E> clazz, K id) {
		return genericDao.getEntity(clazz, id);
	}

	@Override
	public E getById(K id) {
		return genericDao.getById(id);
	}

	/**
	 * Retourne une entité à partir du nom d'un champ et de sa valeur. Il faut forcément que le champ fasse l'objet
	 * d'une contrainte d'unicité. Cette méthode ne doit pas être utilisée directement et droit être wrappée (par
	 * exemple via un getByPropriete(valeur)).
	 * 
	 * @param fieldName le champ sur lequel la condition est appliquée
	 * @param fieldValue valeur du champ
	 * @return entité
	 */
	protected <V> E getByField(SingularAttribute<E, V> fieldName, V fieldValue) {
		return genericDao.getByField(fieldName, fieldValue);
	}
	
	@Override
	public void save(E entity) throws ServiceException,
			SecurityServiceException {
		genericDao.save(entity);
	}
	
	@Override
	public final void create(E entity) throws ServiceException,
			SecurityServiceException {
		createEntity(entity);
	}
	
	protected void createEntity(E entity) throws ServiceException, SecurityServiceException {
		save(entity);
	}
	
	@Override
	public final void update(E entity) throws ServiceException,
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
	protected void updateEntity(E entity) throws ServiceException,
			SecurityServiceException {
		genericDao.update(entity);
	}
	
	@Override
	public void delete(E entity) throws ServiceException, SecurityServiceException {
		genericDao.delete(entity);
	}
	
	@Override
	public void flush() {
		genericDao.flush();
	}
	
	@Override
	public E refresh(E entity) {
		return genericDao.refresh(entity);
	}

	@Override
	public List<E> list() {
		return genericDao.list();
	}
	
	/**
	 * Renvoie la liste des entités dont le champ donné en paramètre a la bonne valeur.
	 * 
	 * @param fieldName le champ sur lequel appliquer la condition
	 * @param fieldValue valeur du champ
	 * @return liste d'entités
	 */
	protected <V> List<E> listByField(SingularAttribute<E, V> fieldName, V fieldValue) {
		return genericDao.listByField(fieldName, fieldValue);
	}
	
	@Override
	public Long count() {
		return genericDao.count();
	}

}
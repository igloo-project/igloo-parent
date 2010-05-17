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

import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;

/**
 * <p>Service racine pour la gestion des entités.</p>
 *
 * @author Open Wide
 *
 * @param <T> type d'entité
 */
public interface GenericEntityService<T> {

	/**
	 * Crée l'entité dans la base de données. Mis à part dans les tests pour faire des sauvegardes simples, utiliser
	 * create() car il est possible qu'il y ait des listeners sur la création d'une entité.
	 * 
	 * @param entity entité
	 */
	void save(T entity) throws ServiceException, SecurityServiceException;
	
	/**
	 * Met à jour l'entité dans la base de données.
	 * 
	 * @param entity entité
	 */
	void update(T entity) throws ServiceException, SecurityServiceException;
	
	/**
	 * Crée l'entité dans la base de données.
	 * 
	 * @param entity entité
	 */
	void create(T entity) throws ServiceException, SecurityServiceException;

	/**
	 * Crée l'entité dans la base de données en vérifiant si elle existe déjà.
	 * 
	 * @param entity entité
	 * @param checkNew vérification
	 */
	void create(T entity, boolean checkNew) throws ServiceException, SecurityServiceException;

	/**
	 * Supprime l'entité de la base de données
	 * 
	 * @param entity entité
	 */
	void delete(T entity);
	
	/**
	 * Rafraîchit l'entité depuis la base de données
	 * 
	 * @param entity entité
	 */
	T refresh(T entity);
	
	/**
	 * Retourne une entité à partir de son id.
	 * 
	 * @param id identifiant
	 * @return entité
	 */
	T getById(Number id);
	
	/**
	 * Renvoie la liste de l'ensemble des entités de ce type.
	 * 
	 * @return liste d'entités
	 */
	List<T> list();
	
	/**
	 * Retourne une entité à partir de sa classe et son id.
	 * 
	 * @param clazz classe
	 * @param id identifiant
	 * @return entité
	 */
	T getEntity(Class<? extends T> clazz, Integer id);
	
	/**
	 * Compte le nombre d'entités de ce type présentes dans la base.
	 * 
	 * @return nombre d'entités
	 */
	Long count();
	
	/**
	 * Flushe la session.
	 */
	void flush();
}

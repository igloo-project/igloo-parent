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

/**
 * <p>DAO racine pour la gestion des entités.</p>
 *
 * @author Open Wide
 *
 * @param <T> type de l'entité à prendre en charge
 */
package fr.openwide.core.hibernate.business.generic.dao;

import java.io.Serializable;
import java.util.List;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

/**
 * <p>DAO racine pour la gestion des entités.</p>
 *
 * @author Open Wide
 *
 * @param <T> type de l'entité à prendre en charge
 */
public interface GenericEntityDao<K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>> {

	/**
	 * Retourne une entité à partir de sa classe et son id.
	 * 
	 * @param clazz classe
	 * @param id identifiant
	 * @return entité
	 */
	E getEntity(Class<? extends E> clazz, K id);
	
	/**
	 * Retourne une entité à partir de son id.
	 * 
	 * @param id identifiant
	 * @return entité
	 */
	E getById(K id);
	
	/**
	 * Retourne une entité à partir du nom d'un champ et de sa valeur. Il faut forcément que le champ fasse l'objet
	 * d'une contrainte d'unicité.
	 * 
	 * @param fieldName nom du champ
	 * @param fieldValue valeur du champ
	 * @return entité
	 */
	E getByField(String fieldName, Object fieldValue);
	
	/**
	 * Crée l'entité dans la base de données.
	 * 
	 * @param entity entité
	 */
	void save(E entity);
	
	/**
	 * Met à jour l'entité dans la base de données.
	 * 
	 * @param entity entité
	 */
	void update(E entity);
	
	/**
	 * Supprime l'entité de la base de données
	 * 
	 * @param entity entité
	 */
	void delete(E entity);
	
	/**
	 * Rafraîchit l'entité depuis la base de données
	 * 
	 * @param entity entité
	 */
	E refresh(E entity);
	
	/**
	 * Renvoie la liste de l'ensemble des entités de ce type.
	 * 
	 * @return liste d'entités
	 */
	List<E> list();
	
	/**
	 * Renvoie la liste des entités dont le champ donné en paramètre a la bonne valeur.
	 * 
	 * @param fieldName nom du champ
	 * @param fieldValue valeur du champ
	 * @return liste d'entités
	 */
	List<E> listByField(String fieldName, Object fieldValue);
	
	/**
	 * Compte le nombre d'entités de ce type présentes dans la base.
	 * 
	 * @return nombre d'entités
	 */
	Long count();
	
	/**
	 * Compte le nombre d'entités de ce type présentes dans la base dont le champ donné en paramètre a la bonne valeur.
	 * 
	 * @param fieldName nom du champ
	 * @param fieldValue valeur du champ
	 * @return nombre d'entités
	 */
	Long countByField(String fieldName, Object fieldValue);
	
	/**
	 * Flushe la session.
	 */
	void flush();

}
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

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;

/**
 * <p>DAO racine pour la gestion des entités.</p>
 *
 * @author Open Wide
 *
 * @param <E> type de l'entité à prendre en charge
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
	 * Compte le nombre d'entités de ce type présentes dans la base.
	 * 
	 * @return nombre d'entités
	 */
	Long count();
	
	/**
	 * Flushe la session.
	 */
	void flush();

	/**
	 * Compte le nombre d'entités respectant la condition attribut = valeur
	 * 
	 * @param <V> le type de la valeur
	 * @param attribute
	 * @param fieldValue
	 * @return le nombre d'entités
	 */
	<V> Long countByField(SingularAttribute<E, V> attribute, V fieldValue);

	/**
	 * Liste les entités respectant la condition de recherche attribut = valeur
	 * 
	 * @param <V> le type de la valeur
	 * @param attribute
	 * @param fieldValue
	 * @return
	 */
	<V> List<E> listByField(SingularAttribute<E, V> attribute, V fieldValue);

	/**
	 * Obtient un objet par la condition attribut = valeur
	 * 
	 * @param <V>
	 * @param attribute
	 * @param fieldValue
	 * @return
	 * @throws NoResultException
	 * @throws {@link NonUniqueResultException}
	 */
	<V> E getByField(SingularAttribute<E, V> attribute, V fieldValue);

	<T extends E> List<T> list(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders);

	Long count(Expression<Boolean> filter);

}
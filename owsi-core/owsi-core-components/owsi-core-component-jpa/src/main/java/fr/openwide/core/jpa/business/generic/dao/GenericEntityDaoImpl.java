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

package fr.openwide.core.hibernate.business.generic.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.business.generic.util.GenericEntityUtils;

/**
 * <p>Implémentation de {@link GenericEntityDao}</p>
 *
 * @author Open Wide
 *
 * @param <T> type de l'entité
 */
public abstract class GenericEntityDaoImpl<K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>>
		implements GenericEntityDao<K, E> {
	
	protected static final String SQL_LIKE_WILDCARD = "%";
	
	@PersistenceContext
	private EntityManager entityManager;
	
	/**
	 * Classe de l'entité, déterminé à partir des paramètres generics.
	 */
	private Class<E> objectClass;
	
	/**
	 * Constructeur.
	 *
	 * @param entityManagerFactory entity manager factory Hibernate injectée par Spring
	 */
	@SuppressWarnings("unchecked")
	public GenericEntityDaoImpl() {
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
		return (E) getEntityManager().find(clazz, id);
	}
	
	@Override
	public E getById(K id) {
		return (E) getEntityManager().find(getObjectClass(), id);
	}
	
	@Override
	public <V> E getByField(SingularAttribute<E, V> attribute, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(getObjectClass());
		Root<E> root = criteria.from(getObjectClass());
		criteria.where(builder.equal(root.get(attribute), fieldValue));
		return (E) buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	@Override
	public void update(E entity) {
		//TODO: http://blog.xebia.com/2009/03/23/jpa-implementation-patterns-saving-detached-entities/
	}
	
	@Override
	public void save(E entity) {
		getEntityManager().persist(entity);
	}
	
	@Override
	public void delete(E entity) {
		getEntityManager().remove(entity);
	}
	
	@Override
	public E refresh(E entity) {
		getEntityManager().refresh(entity);
		
		return entity;
	}
	
	@Override
	public void flush() {
		getEntityManager().flush();
	}
	
	@Override
	public List<E> list() {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(getObjectClass());
		rootCriteriaQuery(builder, criteria, objectClass);
		return getEntityManager().createQuery(criteria).getResultList();
	}
	
	@Override
	public <V> List<E> listByField(SingularAttribute<E, V> attribute, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(getObjectClass());
		
		Root<E> root = rootCriteriaQuery(builder, criteria, getObjectClass());
		criteria.where(builder.equal(root.get(attribute), fieldValue));
		
		return buildTypedQuery(criteria, null, null).getResultList();
	}
	
	@Override
	public <T extends E> List<T> list(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(objectClass);
		rootCriteriaQuery(builder, criteria, objectClass);
		filterCriteriaQuery(criteria, filter);
		TypedQuery<T> query = buildTypedQuery(criteria, limit, offset);
		
		List<T> entities = new ArrayList<T>();
		entities = query.getResultList();
		
		if(orders == null || orders.length == 0) {
			Collections.sort(entities);
		}
		
		return entities;
	}
	
	@Override
	public Long count() {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<E> root = rootCriteriaQuery(builder, criteria, getObjectClass());
		
		criteria.select(builder.count(root));
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	@Override
	public <V> Long countByField(SingularAttribute<E, V> attribute, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<E> root = rootCriteriaQuery(builder, criteria, getObjectClass());
		criteria.select(builder.count(root));
		
		Expression<Boolean> filter = builder.equal(root.get(attribute), fieldValue);
		filterCriteriaQuery(criteria, filter);
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	@Override
	public Long count(Expression<Boolean> filter) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<E> root = rootCriteriaQuery(builder, criteria, getObjectClass());
		criteria.select(builder.count(root));
		
		filterCriteriaQuery(criteria, filter);
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	/**
	 * Crée la requête et applique les conditions de limite / offset et retourne la {@link TypedQuery}
	 * correspondante.
	 * 
	 * @param <T> le type de l'entité retournée
	 * @param criteria
	 * @param limit null si pas de limite
	 * @param offset null si pas d'offset
	 * @return la {@link TypedQuery} avec limite et offset le cas échéant
	 */
	protected <T> TypedQuery<T> buildTypedQuery(CriteriaQuery<T> criteria, Integer limit, Integer offset) {
		TypedQuery<T> query = getEntityManager().createQuery(criteria);
		if (offset != null) {
			query.setFirstResult(offset);
		}
		if (limit != null) {
			query.setMaxResults(limit);
		}
		return query;
		
	}
	
	/**
	 * Applique le filtre sur le criteria si non nul
	 * 
	 * @param criteria
	 * @param filter
	 */
	protected void filterCriteriaQuery(CriteriaQuery<?> criteria, Expression<Boolean> filter) {
		if (filter != null) {
			criteria.where(filter);
		}
	}
	
	/**
	 * Equivalent à select Object from Object. Retourne le root lié à Object permettant ensuite de réaliser des requêtes
	 * sur les attributs de Object.
	 * 
	 * @param builder
	 * @param criteria
	 * @return le Root créé sur la requête
	 */
	protected <T extends E> Root<T> rootCriteriaQuery(CriteriaBuilder builder, CriteriaQuery<?> criteria, Class<T> objectClass) {
		Root<T> root = criteria.from(objectClass);
		return root;
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}
}
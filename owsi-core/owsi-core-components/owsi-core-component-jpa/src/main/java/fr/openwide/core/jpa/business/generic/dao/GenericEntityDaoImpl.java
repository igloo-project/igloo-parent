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

package fr.openwide.core.jpa.business.generic.dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.util.GenericEntityUtils;

/**
 * <p>Implémentation de {@link IGenericEntityDao}</p>
 *
 * @author Open Wide
 *
 * @param <T> type de l'entité
 */
public abstract class GenericEntityDaoImpl<K extends Serializable & Comparable<K>, E extends GenericEntity<K, E>>
		extends JpaDaoSupport
		implements IGenericEntityDao<K, E> {
	
	protected static final String SQL_LIKE_WILDCARD = "%";
	
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
		return super.getEntity(getObjectClass(), id);
	}
	
	@Override
	public E getById(K id) {
		return super.getEntity(getObjectClass(), id);
	}
	
	@Override
	public <V> E getByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.getByField(getObjectClass(), attribute, fieldValue);
	}
	
	@Override
	public void update(E entity) {
		super.update(entity);
	}
	
	@Override
	public void save(E entity) {
		super.save(entity);
	}
	
	@Override
	public void delete(E entity) {
		super.delete(entity);
	}
	
	@Override
	public E refresh(E entity) {
		return super.refresh(entity);
	}
	
	@Override
	public void flush() {
		super.flush();
	}
	
	@Override
	public List<E> list() {
		return super.listEntity(getObjectClass());
	}
	
	@Override
	public <V> List<E> listByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.listEntityByField(getObjectClass(), attribute, fieldValue);
	}
	
	@Override
	public <T extends E> List<T> list(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders) {
		List<T> entities = super.listEntity(objectClass, filter, limit, offset, orders);
		
		if(orders == null || orders.length == 0) {
			Collections.sort(entities);
		}
		
		return entities;
	}
	
	@Override
	public Long count() {
		return super.countEntity(getObjectClass());
	}
	
	@Override
	public <V> Long countByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.countEntityByField(getObjectClass(), attribute, fieldValue);
	}
	
	@Override
	public Long count(Expression<Boolean> filter) {
		return super.countEntity(getObjectClass(), filter);
	}
}
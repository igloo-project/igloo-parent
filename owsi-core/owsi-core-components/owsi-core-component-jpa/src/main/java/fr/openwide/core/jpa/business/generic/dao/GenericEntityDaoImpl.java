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
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;

import com.querydsl.core.types.dsl.PathBuilder;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.IReference;
import fr.openwide.core.jpa.business.generic.util.GenericEntityUtils;

/**
 * <p>Implémentation de {@link IGenericEntityDao}</p>
 *
 * @author Open Wide
 *
 * @param <T> type de l'entité
 */
public abstract class GenericEntityDaoImpl<K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>>
		extends AbstractEntityDaoImpl<E>
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
	public <T extends E> T getById(Class<T> clazz, K id) {
		return super.getEntity(clazz, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends E> T getById(IReference<T> reference) {
		return reference == null ? null : getById(reference.getType(), (K) reference.getId());
	}
	
	@Override
	public E getByNaturalId(Object naturalId) {
		return super.getEntityByNaturalId(getObjectClass(), naturalId);
	}
	
	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <V extends Comparable<?>> E getByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.getEntityByField(getObjectClass(), attribute, fieldValue);
	}
	
	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public E getByFieldIgnoreCase(SingularAttribute<? super E, String> attribute, String fieldValue) {
		return super.getEntityByFieldIgnoreCase(getObjectClass(), attribute, fieldValue);
	}
	
	@Override
	public void update(E entity) { // NOSONAR
		super.update(entity);
	}
	
	@Override
	public void save(E entity) { // NOSONAR
		super.save(entity);
	}
	
	@Override
	public void delete(E entity) { // NOSONAR
		super.delete(entity);
	}
	
	@Override
	public E refresh(E entity) { // NOSONAR
		return super.refresh(entity);
	}
	
	@Override
	public List<E> list() {
		return super.listEntity(getObjectClass());
	}
	
	@Override
	public List<E> list(Long limit, Long offset) {
		PathBuilder<E> pathBuilder = new PathBuilder<E>(getObjectClass(), "rootAlias");
		return super.list(pathBuilder, limit, offset);
	}
	
	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <V extends Comparable<?>> List<E> listByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.listEntityByField(getObjectClass(), attribute, fieldValue);
	}
	
	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <T extends E> List<T> list(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders) {
		return super.listEntity(objectClass, filter, limit, offset, orders);
	}
	
	@Override
	public Long count() {
		return super.countEntity(getObjectClass());
	}
	
	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public <V extends Comparable<?>> Long countByField(SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.countEntityByField(getObjectClass(), attribute, fieldValue);
	}
	
	/**
	 * @deprecated Utiliser QueryDSL
	 */
	@Deprecated
	@Override
	public Long count(Expression<Boolean> filter) {
		return super.countEntity(getObjectClass(), filter);
	}
}
/*
 * Copyright (C) 2009-2011 Open Wide
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

package fr.openwide.core.jpa.more.business.generic.dao;

import java.util.Comparator;
import java.util.List;

import javax.persistence.metamodel.SingularAttribute;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;

public interface IGenericListItemDao {

	<E extends GenericListItem<?>> E getEntity(Class<E> clazz, Long id);

	<E extends GenericListItem<?>> E getById(Class<E> clazz, Long id);
	
	<E extends GenericListItem<?>> E getByNaturalId(Class<E> clazz, Object naturalId);

	<E extends GenericListItem<?>> void update(E entity);

	<E extends GenericListItem<?>> void save(E entity);

	<E extends GenericListItem<?>> void delete(E entity);

	<E extends GenericListItem<?>> E refresh(E entity);

	<E extends GenericListItem<?>> List<E> list(Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator);

	<E extends GenericListItem<?>> List<E> list(Class<E> clazz, EnabledFilter enabledFilter);

	<E extends GenericListItem<?>> List<E> list(Class<E> clazz);

	/**
	 * @deprecated Use {@link #listByField(EntityPath, SimpleExpression, Comparable, OrderSpecifier)} instead.
	 */
	@Deprecated
	<E extends GenericListItem<?>, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter, Comparator<? super E> comparator);

	/**
	 * @deprecated Use {@link #listByField(EntityPath, SimpleExpression, Comparable, OrderSpecifier)} instead.
	 */
	@Deprecated
	<E extends GenericListItem<?>, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter);

	/**
	 * @deprecated Use {@link #listByField(EntityPath, SimpleExpression, Comparable, OrderSpecifier)} instead.
	 */
	@Deprecated
	<E extends GenericListItem<?>, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	<E extends GenericListItem<?>> Long count(Class<E> clazz, EnabledFilter enabledFilter);

	<E extends GenericListItem<?>> Long count(Class<E> clazz);

	/**
	 * @deprecated Use {@link #countByField(EntityPath, SimpleExpression, Comparable)} instead.
	 */
	@Deprecated
	<E extends GenericListItem<?>, V extends Comparable<?>> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue,
			EnabledFilter enabledFilter);

	/**
	 * @deprecated Use {@link #countByField(EntityPath, SimpleExpression, Comparable)} instead.
	 */
	@Deprecated
	<E extends GenericListItem<?>, V extends Comparable<?>> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue);

	/**
	 * @deprecated Use {@link #getByField(EntityPath, SimpleExpression, Comparable)} instead.
	 * 
	 * Get an object with a "key=param" condition.
	 */
	@Deprecated
	<E extends GenericListItem<?>, V extends Comparable<?>>  E getByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue);
	
	/**
	 * @deprecated Use {@link #getByFieldIgnoreCase(EntityPath, StringExpression, String)} instead.
	 * 
	 * Get an object with a "lower(value)=lower(param)" condition.
	 */
	@Deprecated
	<E extends GenericListItem<?>>  E getByFieldIgnoreCase(Class<E> clazz, SingularAttribute<? super E, String> attribute, String fieldValue);

	/**
	 * @deprecated Implement a {@link fr.openwide.core.jpa.more.business.generic.query.IGenericListItemSearchQuery<T, S, Q>).
	 * See in particular {@link fr.openwide.core.jpa.more.business.generic.query.AbstractGenericListItemHibernateSearchSearchQuery<T, S, Q>}
	 */
	@Deprecated
	<E extends GenericListItem<?>> List<E> searchAutocomplete(String searchPattern, Class<E> clazz, Integer limit, Integer offset) throws ServiceException;

	<T extends GenericListItem<?>, V extends Comparable<?>> T getByField(EntityPath<T> entityPath, SimpleExpression<V> field, V fieldValue);

	<T extends GenericListItem<?>> T getByFieldIgnoreCase(EntityPath<T> entityPath,
			StringExpression field, String fieldValue);

	<T extends GenericListItem<?>> List<T> list(EntityPath<T> entityPath);

	<T extends GenericListItem<?>> List<T> list(EntityPath<T> entityPath, Long limit, Long offset);

	<T extends GenericListItem<?>, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath,
			SimpleExpression<V> field, V fieldValue, OrderSpecifier<?> orderSpecifier);

	<T extends GenericListItem<?>, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath,
			SimpleExpression<V> field, V fieldValue, Long limit, Long offset, OrderSpecifier<?> orderSpecifier);

	<V extends Comparable<?>> Long count(EntityPath<? extends GenericListItem<?>> entityPath);

	<V extends Comparable<?>> Long countByField(EntityPath<? extends GenericListItem<?>> entityPath,
			SimpleExpression<V> field, V fieldValue);

}

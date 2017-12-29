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

package org.iglooproject.jpa.more.business.generic.service;

import java.util.Comparator;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.hibernate.search.annotations.Indexed;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

import org.iglooproject.jpa.business.generic.service.ITransactionalAspectAwareService;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.generic.model.EnabledFilter;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;

public interface IGenericListItemService extends ITransactionalAspectAwareService {
	
	<E extends GenericListItem<?>> E getById(Class<E> clazz, Long id);

	<E extends GenericListItem<?>> void create(E entity);

	<E extends GenericListItem<?>> void update(E entity);

	<E extends GenericListItem<?>> void delete(E entity);

	<E extends GenericListItem<?>> List<E> list(Class<E> clazz);

	<E extends GenericListItem<?>> List<E> list(Class<E> clazz, Comparator<? super E> comparator);

	<E extends GenericListItem<?>> List<E> list(Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator);

	<E extends GenericListItem<?>> long count(Class<E> clazz);

	<E extends GenericListItem<?>> List<E> listEnabled(Class<E> clazz);

	<E extends GenericListItem<?>> List<E> listEnabled(Class<E> clazz, Comparator<? super E> comparator);

	/**
	 * WARNING: only use this if unique constraints were set on the label column of {@code source}.
	 */
	<E extends GenericListItem<?>> E getByLabel(Class<E> clazz, String label) throws NonUniqueResultException;
	
	/**
	 * WARNING: only use this if unique constraints were set on the short label column of {@code source}.
	 */
	<E extends GenericListItem<?>> E getByShortLabel(Class<E> clazz, String shortLabel) throws NonUniqueResultException;

	/**
	 * WARNING: only use this if unique constraints were set on {@code lower(label)} in the {@code source} table.
	 */
	<E extends GenericListItem<?>> E getByLabelIgnoreCase(Class<E> clazz, String label) throws NonUniqueResultException;
	
	/**
	 * WARNING: only use this if unique constraints were set on {@code lower(shortLabel)} in the {@code source} table.
	 */
	<E extends GenericListItem<?>> E getByShortLabelIgnoreCase(Class<E> clazz, String shortLabel) throws NonUniqueResultException;

	/**
	 * WARNING: only use this if unique constraints were set on the field of {@code source}.
	 */
	<E extends GenericListItem<?>, V extends Comparable<?>> E getByField(EntityPath<E> entityPath, SimpleExpression<V> field, V fieldValue) throws NonUniqueResultException;

	/**
	 * WARNING: only use this if unique constraints were set on {@code lower(field)} in the {@code source} table.
	 */
	<E extends GenericListItem<?>> E getByFieldIgnoreCase(EntityPath<E> entityPath, StringExpression field, String fieldValue) throws NonUniqueResultException;

	/**
	 * WARNING: only works on classes that were annotated with {@link Indexed}.
	 * 
	 * @deprecated Implement a {@link org.iglooproject.jpa.more.business.generic.query.IGenericListItemSearchQuery<T, S, Q>).
	 * See in particular {@link org.iglooproject.jpa.more.business.generic.query.AbstractGenericListItemHibernateSearchSearchQuery<T, S, Q>}
	 */
	@Deprecated
	<E extends GenericListItem<?>> List<E> searchAutocomplete(String searchPattern, Class<E> clazz, int limit, int offset)
			throws ServiceException;

}

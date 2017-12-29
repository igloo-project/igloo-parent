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

package org.iglooproject.jpa.more.business.generic.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;

import org.iglooproject.jpa.business.generic.dao.AbstractEntityDaoImpl;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.generic.model.EnabledFilter;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.jpa.more.business.generic.model.GenericListItemBinding;
import org.iglooproject.jpa.more.business.generic.model.QGenericListItem;
import org.iglooproject.jpa.more.business.generic.util.GenericListItemComparator;
import org.iglooproject.jpa.search.service.IHibernateSearchService;
import org.iglooproject.spring.util.lucene.search.LuceneUtils;

@Component("genericListItemDao")
public class GenericListItemDaoImpl extends AbstractEntityDaoImpl<GenericListItem<?>> implements IGenericListItemDao {

	@Autowired
	private IHibernateSearchService hibernateSearchService;
	
	/**
	 * Constructeur.
	 */
	public GenericListItemDaoImpl() {
	}
	
	@Override
	public <E extends GenericListItem<?>> E getEntity(Class<E> clazz, Long id) {
		return super.getEntity(clazz, id);
	}
	
	@Override
	public <E extends GenericListItem<?>> E getById(Class<E> clazz, Long id) {
		return super.getEntity(clazz, id);
	}
	
	@Override
	public <E extends GenericListItem<?>> E getByNaturalId(Class<E> clazz, Object naturalId) {
		return super.getEntityByNaturalId(clazz, naturalId);
	}

	@Deprecated
	@Override
	public <E extends GenericListItem<?>, V extends Comparable<?>> E getByField(Class<E> clazz, SingularAttribute<? super E, V> attribute,
			V fieldValue) {
		return super.getEntityByField(clazz, attribute, fieldValue);
	}

	@Deprecated
	@Override
	public <E extends GenericListItem<?>> E getByFieldIgnoreCase(Class<E> clazz, SingularAttribute<? super E, String> attribute,
			String fieldValue) {
		return super.getEntityByFieldIgnoreCase(clazz, attribute, fieldValue);
	}
	
	@Override
	public <E extends GenericListItem<?>> void update(E entity) { // NOSONAR
		super.update(entity);
	}
	
	@Override
	public <E extends GenericListItem<?>> void save(E entity) { // NOSONAR
		super.save(entity);
	}
	
	@Override
	public <E extends GenericListItem<?>> void delete(E entity) { // NOSONAR
		super.delete(entity);
	}
	
	@Override
	public <E extends GenericListItem<?>> E refresh(E entity) { // NOSONAR
		return super.refresh(entity);
	}
	
	@Override
	public <E extends GenericListItem<?>> List<E> list(Class<E> clazz) {
		return super.listEntity(clazz);
	}

	@Override
	public <E extends GenericListItem<?>> List<E> list(Class<E> clazz, EnabledFilter enabledFilter) {
		return list(clazz, enabledFilter, null);
	}

	@Override
	public <E extends GenericListItem<?>> List<E> list(Class<E> clazz, EnabledFilter enabledFilter, Comparator<? super E> comparator) {
		PathBuilder<GenericListItem<?>> pathBuilder = new PathBuilder<GenericListItem<?>>(clazz, "rootAlias");
		QGenericListItem entityPath = new QGenericListItem(pathBuilder);
		
		JPAQuery<GenericListItem<?>> query;
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			query = queryByPredicate(entityPath, entityPath.enabled.isTrue());
		} else {
			query = queryByPredicate(entityPath, null);
		}
		
		@SuppressWarnings("unchecked")
		List<E> entities = (List<E>) query.fetch();
		
		Collections.sort(entities, comparator);
		
		return entities;
	}

	@Deprecated
	@Override
	public <E extends GenericListItem<?>,V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue) {
		return listByField(clazz, field, fieldValue, EnabledFilter.ALL, null);
	}

	@Deprecated
	@Override
	public <E extends GenericListItem<?>, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter) {
		return listByField(clazz, field, fieldValue, enabledFilter, null);
	}

	@Deprecated
	@Override
	public <E extends GenericListItem<?>, V extends Comparable<?>> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter, Comparator<? super E> comparator) {
		Pair<EntityPath<E>, JPAQuery<E>> queryItem = queryByField(clazz, field, fieldValue, enabledFilter);
		List<E> entities = queryItem.getValue1().fetch();
		Collections.sort(entities, comparator);
		
		return entities;
	}

	@Deprecated
	private <E extends GenericListItem<?>, V extends Comparable<?>> Pair<EntityPath<E>, JPAQuery<E>> queryByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter) {
		PathBuilder<E> pathBuilder = new PathBuilder<E>(clazz, "entityAlias");
		QGenericListItem entityPath = new QGenericListItem(pathBuilder);
		JPAQuery<E> query;
		if (field != null) {
			query = queryEntityByField(pathBuilder, field.getBindableJavaType(), field.getName(), fieldValue);
		} else {
			query = queryByPredicate(pathBuilder, null);
		}
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			// l'appel au where ajoute la condition aux conditions précédentes
			query = query.where(entityPath.enabled.isTrue());
		}
		
		return Pair.with((EntityPath<E>) pathBuilder, query);
	}

	@Deprecated
	protected <E extends GenericListItem<?>> List<E> list(Class<E> objectClass, Expression<Boolean> filter, javax.persistence.criteria.Order order, Integer limit, Integer offset) {
		List<E> entities = super.listEntity(objectClass, filter, limit, offset, order);
		if (order == null) {
			Collections.sort(entities, GenericListItemComparator.get());
		}
		return entities;
	}
	
	@Override
	public <E extends GenericListItem<?>> Long count(Class<E> clazz) {
		PathBuilder<GenericListItem<?>> path = new PathBuilder<GenericListItem<?>>(clazz, "rootAlias");
		return count(path);
	}

	@Override
	public <E extends GenericListItem<?>> Long count(Class<E> clazz, EnabledFilter enabledFilter) {
		Pair<EntityPath<E>, JPAQuery<E>> queryItem = queryByField(clazz, null, null, enabledFilter);
		return queryItem.getValue1().distinct().fetchCount();
	}

	@Deprecated
	@Override
	public <E extends GenericListItem<?>, V extends Comparable<?>> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.countEntityByField(clazz, attribute, fieldValue);
	}

	@Deprecated
	@Override
	public <E extends GenericListItem<?>, V extends Comparable<?>> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue, EnabledFilter enabledFilter) {
		Pair<EntityPath<E>, JPAQuery<E>> queryItem = queryByField(clazz, attribute, fieldValue, enabledFilter);
		return queryItem.getValue1().distinct().fetchCount();
	}

	@Deprecated
	public <E extends GenericListItem<?>> Long count(Class<E> objectClass, Expression<Boolean> filter) {
		return super.countEntity(objectClass, filter);
	}

	@Override
	@Deprecated
	public <E extends GenericListItem<?>> List<E> searchAutocomplete(String searchPattern, Class<E> clazz,
			Integer limit, Integer offset) throws ServiceException {
		return searchAutocomplete(searchPattern, clazz, null, limit, offset);
	}

	@Deprecated
	protected final <E extends GenericListItem<?>> List<E> searchAutocomplete(String searchPattern, Class<E> clazz,
			String[] fields, Integer limit, Integer offset) throws ServiceException {
		GenericListItemBinding<E> binding = new GenericListItemBinding<E>();
		String labelBindingPath = binding.label().getPath();
		
		QueryBuilder queryBuilder = Search.getFullTextEntityManager(getEntityManager()).getSearchFactory().buildQueryBuilder()
				.forEntity(clazz).get();
		
		Query luceneQuery = queryBuilder.keyword().onField(binding.enabled().getPath()).matching(true).createQuery();
		
		String[] actualFields = fields;
		if (actualFields == null || actualFields.length == 0) {
			actualFields = new String[] {
					labelBindingPath,
					binding.code().getPath()
			};
		}
		
		return hibernateSearchService.search(clazz,
				actualFields,
				LuceneUtils.getAutocompleteQuery(searchPattern),
				luceneQuery,
				limit, offset,
				new Sort(new SortField(GenericListItem.LABEL_SORT_FIELD_NAME, SortField.Type.STRING))
		);
	}

	@Override
	public <T extends GenericListItem<?>, V extends Comparable<?>> T getByField(EntityPath<T> entityPath,
			SimpleExpression<V> field, V fieldValue) {
		return super.getByField(entityPath, field, fieldValue);
	}

	@Override
	public <T extends GenericListItem<?>> T getByFieldIgnoreCase(EntityPath<T> entityPath,
			StringExpression field, String fieldValue) {
		return super.getByFieldIgnoreCase(entityPath, field, fieldValue);
	}

	@Override
	public <T extends GenericListItem<?>> List<T> list(EntityPath<T> entityPath) {
		return super.list(entityPath);
	}

	@Override
	public <T extends GenericListItem<?>> List<T> list(EntityPath<T> entityPath, Long limit, Long offset) {
		return super.list(entityPath, limit, offset);
	}

	@Override
	public <T extends GenericListItem<?>, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath,
			SimpleExpression<V> field, V fieldValue, OrderSpecifier<?> orderSpecifier) {
		return super.listByField(entityPath, field, fieldValue, orderSpecifier);
	}

	@Override
	public <T extends GenericListItem<?>, V extends Comparable<?>> List<T> listByField(EntityPath<T> entityPath,
			SimpleExpression<V> field, V fieldValue, Long limit, Long offset, OrderSpecifier<?> orderSpecifier) {
		return super.listByField(entityPath, field, fieldValue, limit, offset, orderSpecifier);
	}

	@Override
	public <V extends Comparable<?>> Long count(EntityPath<? extends GenericListItem<?>> entityPath) {
		return super.count(entityPath);
	}

	@Override
	public <V extends Comparable<?>> Long countByField(EntityPath<? extends GenericListItem<?>> entityPath,
			SimpleExpression<V> field, V fieldValue) {
		return super.countByField(entityPath, field, fieldValue);
	}

}

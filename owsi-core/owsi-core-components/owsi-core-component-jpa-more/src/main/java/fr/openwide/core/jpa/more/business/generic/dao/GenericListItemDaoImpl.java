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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.openwide.core.jpa.business.generic.dao.JpaDaoSupport;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.model.EnabledFilter;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItemBinding;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem_;
import fr.openwide.core.jpa.more.business.generic.util.GenericListItemComparator;
import fr.openwide.core.jpa.search.service.IHibernateSearchService;
import fr.openwide.core.spring.util.lucene.search.LuceneUtils;

@Component("genericListItemDao")
public class GenericListItemDaoImpl extends JpaDaoSupport implements IGenericListItemDao {
	
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
	public <E extends GenericListItem<?>> E getByNaturalId(Class<E> clazz, String naturalId) {
		return super.getEntityByNaturalId(clazz, naturalId);
	}

	@Override
	public <E extends GenericListItem<?>, V> E getByField(Class<E> clazz, SingularAttribute<? super E, V> attribute,
			V fieldValue) {
		return super.getEntityByField(clazz, attribute, fieldValue);
	}
	
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
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(clazz);
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			criteria.where(builder.equal(root.get(GenericListItem_.enabled), true));
		}
		
		List<E> entities = buildTypedQuery(criteria, null, null).getResultList();
		
		Collections.sort(entities, comparator);
		
		return entities;
	}
	
	@Override
	public <E extends GenericListItem<?>,V> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue) {
		return listByField(clazz, field, fieldValue, EnabledFilter.ALL, null);
	}
	
	@Override
	public <E extends GenericListItem<?>, V> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter) {
		return listByField(clazz, field, fieldValue, enabledFilter, null);
	}
	
	@Override
	public <E extends GenericListItem<?>, V> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue,
			EnabledFilter enabledFilter, Comparator<? super E> comparator) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(clazz);
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		filterCriteriaQuery(criteria, builder.equal(root.get(field), fieldValue));
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			filterCriteriaQuery(criteria, builder.equal(root.get(GenericListItem_.enabled), true));
		}
		
		List<E> entities = buildTypedQuery(criteria, null, null).getResultList();
		
		Collections.sort(entities, comparator);
		
		return entities;
	}
	
	protected <E extends GenericListItem<?>> List<E> list(Class<E> objectClass, Expression<Boolean> filter, Order order, Integer limit, Integer offset) {
		List<E> entities = super.listEntity(objectClass, filter, limit, offset, order);
		if (order == null) {
			Collections.sort(entities, GenericListItemComparator.INSTANCE);
		}
		return entities;
	}
	
	@Override
	public <E extends GenericListItem<?>> Long count(Class<E> clazz) {
		return count(clazz, EnabledFilter.ALL);
	}
	
	@Override
	public <E extends GenericListItem<?>> Long count(Class<E> clazz, EnabledFilter enabledFilter) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			filterCriteriaQuery(criteria, builder.equal(root.get(GenericListItem_.enabled), true));
		}
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	@Override
	public <E extends GenericListItem<?>, V> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.countEntityByField(clazz, attribute, fieldValue);
	}
	
	@Override
	public <E extends GenericListItem<?>, V> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue, EnabledFilter enabledFilter) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<E> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		filterCriteriaQuery(criteria, builder.equal(root.get(attribute), fieldValue));
		if (EnabledFilter.ENABLED_ONLY.equals(enabledFilter)) {
			filterCriteriaQuery(criteria, builder.equal(root.get(GenericListItem_.enabled), true));
		}
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	
	public <E extends GenericListItem<?>> Long count(Class<E> objectClass, Expression<Boolean> filter) {
		return super.countEntity(objectClass, filter);
	}

	@Override
	public <E extends GenericListItem<?>> List<E> searchAutocomplete(String searchPattern, Class<E> clazz,
			Integer limit, Integer offset) throws ServiceException {
		return searchAutocomplete(searchPattern, clazz, null, limit, offset);
	}
	
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
				new Sort(new SortField(GenericListItem.LABEL_SORT_FIELD_NAME, SortField.STRING))
		);
	}
	
}

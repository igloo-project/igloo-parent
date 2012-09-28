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
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.stereotype.Component;

import fr.openwide.core.jpa.business.generic.dao.JpaDaoSupport;
import fr.openwide.core.jpa.more.business.generic.model.GenericListItem;
import fr.openwide.core.jpa.more.business.generic.util.GenericListItemComparator;

@Component("genericListItemDao")
public class GenericListItemDaoImpl extends JpaDaoSupport implements IGenericListItemDao {
	
	/**
	 * Constructeur.
	 */
	public GenericListItemDaoImpl() {
	}
	
	@Override
	public <E extends GenericListItem<?>> E getEntity(Class<E> clazz, Integer id) {
		return super.getEntity(clazz, id);
	}
	
	@Override
	public <E extends GenericListItem<?>> E getById(Class<E> clazz, Integer id) {
		return super.getEntity(clazz, id);
	}
	
	@Override
	public <E extends GenericListItem<?>> E getByNaturalId(Class<E> clazz, String naturalId) {
		return super.getEntityByNaturalId(clazz, naturalId);
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
	public <E extends GenericListItem<?>,V> List<E> listByField(Class<E> clazz, SingularAttribute<? super E, V> field, V fieldValue) {
		return super.listEntityByField(clazz, field, fieldValue);
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
		return super.countEntity(clazz);
	}
	
	@Override
	public <E extends GenericListItem<?>, V> Long countByField(Class<E> clazz, SingularAttribute<? super E, V> attribute, V fieldValue) {
		return super.countEntityByField(clazz, attribute, fieldValue);
	}
	
	
	public <E extends GenericListItem<?>> Long count(Class<E> objectClass, Expression<Boolean> filter) {
		return super.countEntity(objectClass, filter);
	}
	
}

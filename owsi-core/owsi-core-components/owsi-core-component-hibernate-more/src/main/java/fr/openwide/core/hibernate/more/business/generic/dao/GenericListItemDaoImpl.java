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

package fr.openwide.core.hibernate.more.business.generic.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import fr.openwide.core.hibernate.more.business.generic.model.GenericListItem;
import fr.openwide.core.hibernate.more.business.generic.util.GenericListItemComparator;

@Component("genericListItemDao")
public class GenericListItemDaoImpl extends HibernateDaoSupport implements GenericListItemDao {
	
	/**
	 * Constructeur.
	 *
	 * @param sessionFactory session factory Hibernate injectée par Spring
	 */
	@Autowired
	public GenericListItemDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <E extends GenericListItem<?>> E getEntity(Class<E> clazz, Integer id) {
		return (E) getSession().get(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <E extends GenericListItem<?>> E getById(Class<E> clazz, Integer id) {
		return (E) getSession().get(clazz, id);
	}
	
	@Override
	public <E extends GenericListItem<?>> void update(E entity) {
		getSession().update(entity);
	}
	
	@Override
	public <E extends GenericListItem<?>> void save(E entity) {
		getSession().save(entity);
	}
	
	@Override
	public <E extends GenericListItem<?>> void delete(E entity) {
		getSession().delete(entity);
	}
	
	@Override
	public <E extends GenericListItem<?>> E refresh(E entity) {
		getSession().refresh(entity);
		
		return entity;
	}
	
	@Override
	public <E extends GenericListItem<?>> List<E> list(Class<E> clazz) {
		return list(clazz, null, null, null, null);
	}
	
	@Override
	public <E extends GenericListItem<?>> List<E> listByField(Class<E> clazz, String fieldName, Object fieldValue) {
		Criterion filter = Restrictions.eq(fieldName, fieldValue);
		return list(clazz, filter, null, null, null);
	}
	
	/**
	 * Retourne une liste d'entités correspondant aux paramètres. N'a pas vocation à être appelée directement.
	 * 
	 * @param objectClass classe de l'entité
	 * @param filter filtre Hibernate
	 * @param order ordre de tri
	 * @param limit limit
	 * @param offset offset
	 * @return liste d'entités
	 */
	@SuppressWarnings("unchecked")
	public <E extends GenericListItem<?>> List<E> list(Class<E> objectClass, Criterion filter, Order order, Integer limit, Integer offset) {
		List<E> entities = new ArrayList<E>();
		try {
			Criteria criteria = buildCriteria(objectClass, null, filter, order, limit, offset);
			
			entities = criteria.list();
			
			if (order == null) {
				Collections.sort(entities, GenericListItemComparator.INSTANCE);
			}
			
			return entities;
		} catch(DataAccessException e) {
			return entities;
		}
	}
	
	@Override
	public <E extends GenericListItem<?>> Long count(Class<E> clazz) {
		return count(clazz, null, null, null, null);
	}
	
	@Override
	public <E extends GenericListItem<?>> Long countByField(Class<E> clazz, String fieldName, Object fieldValue) {
		Criterion filter = Restrictions.eq(fieldName, fieldValue);
		return count(clazz, filter, null, null, null);
	}
	
	/**
	 * Compte le nombre d'entités correspondant aux paramètres. N'a pas vocation à être appelée directement.
	 * 
	 * @param objectClass classe de l'entité
	 * @param filter filtre Hibernate
	 * @param order ordre de tri
	 * @param limit limit
	 * @param offset offset
	 * @return nombre d'entités
	 */
	public <E extends GenericListItem<?>> Long count(Class<E> objectClass, Criterion filter, Order order, Integer limit, Integer offset) {
		Criteria criteria = buildCriteria(objectClass, Projections.rowCount(), filter, order, limit, offset);
		
		Long count = (Long) criteria.uniqueResult();
		
		return count;
	}
	
	/**
	 * Construit un criteria Hibernate à partir de l'ensemble des paramètres.
	 * 
	 * @param objectClass classe de l'entité
	 * @param projection projection (au sens de l'API Criteria Hibernate)
	 * @param filter filtre Hibernate
	 * @param order ordre de tri
	 * @param limit limit
	 * @param offset offset
	 * @return criteria
	 */
	protected <E extends GenericListItem<?>> Criteria buildCriteria(Class<E> objectClass, Projection projection, Criterion filter, Order order, Integer limit, Integer offset) {
		Criteria criteria = getSession().createCriteria(objectClass);
		if(projection != null) {
			criteria.setProjection(projection);
		}
		if(filter != null) {
			criteria.add(filter);
		}
		if(limit != null) {
			criteria.setMaxResults(limit);
		}
		if(offset != null) {
			criteria.setFirstResult(offset);
		}
		if(order != null) {
			criteria.addOrder(order);
		}
		return criteria;
	}
	
}

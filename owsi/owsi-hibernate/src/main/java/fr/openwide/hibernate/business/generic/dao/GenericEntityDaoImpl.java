package fr.openwide.hibernate.business.generic.dao;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.SessionImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import fr.openwide.hibernate.business.generic.model.GenericEntity;

public abstract class GenericEntityDaoImpl<T extends GenericEntity<T>> extends HibernateDaoSupport implements GenericEntityDao<T> {
	
	protected static final String SQL_LIKE_WILDCARD = "%";
	
	private Class<T> objectClass;
	
	@SuppressWarnings("unchecked")
	public GenericEntityDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
		
		int retriesCount = 0;
		Class<?> clazz = getClass();
		while(!(clazz.getGenericSuperclass() instanceof ParameterizedType) && (retriesCount < 5)) {
			clazz = clazz.getSuperclass();
			retriesCount ++;
		}
		objectClass = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	protected final Class<T> getObjectClass() {
		return objectClass;
	}
	
	public GenericEntity<?> getEntity(Class<? extends GenericEntity<?>> clazz, Integer id) {
		return (GenericEntity<?>) getSession().get(clazz, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getById(Number id) {
		return (T) getSession().get(getObjectClass(), id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T getByField(String fieldName, Object fieldValue) {
		return (T) getSession().createCriteria(getObjectClass()).add(Restrictions.eq(fieldName, fieldValue)).uniqueResult();
	}
	
	@Override
	public void update(T entity) {
		getSession().update(entity);
	}
	
	@Override
	public void save(T entity) {
		if(entity.getId() == null) {
			getSession().save(entity);
		} else {
			((SessionImpl) getSession()).save(entity, entity.getId());
		}
	}
	
	@Override
	public void delete(T entity) {
		getSession().delete(entity);
	}
	
	@Override
	public T refresh(T entity) {
		getSession().refresh(entity);
		
		return entity;
	}
	
	@Override
	public List<T> list() {
		return list(getObjectClass(), null, null, null, null);
	}
	
	@Override
	public List<T> listByField(String fieldName, Object fieldValue) {
		Criterion filter = Restrictions.eq(fieldName, fieldValue);
		return list(getObjectClass(), filter, null, null, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> list(Class<? extends T> objectClass, Criterion filter, Order order, Integer limit, Integer offset) {
		List<T> entities = new LinkedList<T>();
		try {
			Criteria criteria = buildCriteria(objectClass, null, filter, order, limit, offset);
			
			entities = criteria.list();
			
			if(order == null) {
				Collections.sort(entities);
			}
			
			return entities;
		} catch(DataAccessException e) {
			return entities;
		}
	}
	
	@Override
	public Long count() {
		return count(getObjectClass(), null, null, null, null);
	}
	
	public Long count(Class<? extends T> objectClass, Criterion filter, Order order, Integer limit, Integer offset) {
		Criteria criteria = buildCriteria(objectClass, Projections.rowCount(), filter, order, limit, offset);
		
		Long count = (Long) criteria.uniqueResult();
		
		return count;
	}
	
	protected Criteria buildCriteria(Class<? extends T> objectClass, Projection projection, Criterion filter, Order order, Integer limit, Integer offset) {
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
	
	public void evict(T object) {
		getSession().flush();
		getSession().evict(object);
	}
	
}
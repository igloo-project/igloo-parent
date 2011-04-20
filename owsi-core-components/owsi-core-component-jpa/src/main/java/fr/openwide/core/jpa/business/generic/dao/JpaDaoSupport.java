package fr.openwide.core.jpa.business.generic.dao;

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

public class JpaDaoSupport {

	@PersistenceContext
	private EntityManager entityManager;
	
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
	
	protected void filterCriteriaQuery(CriteriaQuery<?> criteria, Expression<Boolean> filter) {
		if (filter != null) {
			criteria.where(filter);
		}
	}
	
	protected <T> Root<T> rootCriteriaQuery(CriteriaBuilder builder, CriteriaQuery<?> criteria, Class<T> objectClass) {
		Root<T> root = criteria.from(objectClass);
		return root;
	}
	
	public <T,K> T getEntity(Class<T> clazz, K id) {
		return getEntityManager().find(clazz, id);
	}
	
	public <T,K> T getById(Class<T> clazz, K id) {
		return getEntityManager().find(clazz, id);
	}
	
	public <T,V> T getByField(Class<T> clazz, SingularAttribute<? super T, V> attribute, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		Root<T> root = criteria.from(clazz);
		criteria.where(builder.equal(root.get(attribute), fieldValue));
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	protected <T> void update(T entity) {
		//TODO: http://blog.xebia.com/2009/03/23/jpa-implementation-patterns-saving-detached-entities/
	}
	
	protected <T> void save(T entity) {
		getEntityManager().persist(entity);
	}
	
	protected <T> void delete(T entity) {
		getEntityManager().remove(entity);
	}
	
	protected <T> T refresh(T entity) {
		getEntityManager().refresh(entity);
		
		return entity;
	}
	
	protected void flush() {
		getEntityManager().flush();
	}
	
	protected <T> List<T> listEntity(Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		rootCriteriaQuery(builder, criteria, clazz);
		return getEntityManager().createQuery(criteria).getResultList();
	}
	
	protected <T, V> List<T> listEntityByField(Class<T> clazz, SingularAttribute<? super T, V> attribute, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(clazz);
		
		Root<T> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.where(builder.equal(root.get(attribute), fieldValue));
		
		return buildTypedQuery(criteria, null, null).getResultList();
	}
	
	protected <T> List<T> listEntity(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset, Order... orders) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(objectClass);
		rootCriteriaQuery(builder, criteria, objectClass);
		filterCriteriaQuery(criteria, filter);
		criteria.orderBy(orders);
		TypedQuery<T> query = buildTypedQuery(criteria, limit, offset);
		
		List<T> entities = query.getResultList();
		
		return entities;
	}
	
	protected <T> List<T> listEntity(Class<T> objectClass, Expression<Boolean> filter, Integer limit, Integer offset) {
		return listEntity(objectClass, filter, limit, offset, new Order[] {});
	}
	
	protected <T> Long countEntity(Class<T> clazz) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<T> root = rootCriteriaQuery(builder, criteria, clazz);
		
		criteria.select(builder.count(root));
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	protected <T, V> Long countEntityByField(Class<T> clazz, SingularAttribute<? super T, V> attribute, V fieldValue) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<T> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		Expression<Boolean> filter = builder.equal(root.get(attribute), fieldValue);
		filterCriteriaQuery(criteria, filter);
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	protected <T> Long countEntity(Class<T> clazz, Expression<Boolean> filter) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		
		Root<T> root = rootCriteriaQuery(builder, criteria, clazz);
		criteria.select(builder.count(root));
		
		filterCriteriaQuery(criteria, filter);
		
		return buildTypedQuery(criteria, null, null).getSingleResult();
	}
	
	protected EntityManager getEntityManager() {
		return entityManager;
	}

}

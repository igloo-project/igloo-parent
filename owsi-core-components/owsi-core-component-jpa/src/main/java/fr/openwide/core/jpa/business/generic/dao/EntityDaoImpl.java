package fr.openwide.core.jpa.business.generic.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityReference;

@Repository("entityDao")
public class EntityDaoImpl implements IEntityDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(
			Class<E> clazz, K id) {
		return entityManager.find(clazz, id);
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(GenericEntityReference<K, E> reference) {
		return getEntity(reference.getEntityClass(), reference.getEntityId());
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E> listEntity(Class<E> clazz, Collection<K> ids) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(clazz);
		Root<E> root = criteria.from(clazz);
		criteria.where(((CriteriaBuilderImpl)builder).in(root.<K>get("id"), ids));
		
		List<E> entities = entityManager.createQuery(criteria).getResultList();
		
		Collections.sort(entities, null);
		
		return entities;
	}
	
	@Override
	public void flush() {
		entityManager.flush();
	}
	
	@Override
	public void clear() {
		entityManager.clear();
	}

}

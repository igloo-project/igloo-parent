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
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.model.GenericEntityCollectionReference;
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
	public <E extends GenericEntity<?, ?>> E getEntity(GenericEntityReference<?, E> reference) {
		return entityManager.find(reference.getType(), reference.getId());
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E> listEntity(Class<E> clazz, Collection<K> ids) {
		return doListEntity(clazz, ids);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <E extends GenericEntity<?, ?>> List<E> listEntity(GenericEntityCollectionReference<?, E> reference) {
		return doListEntity((Class<E>)reference.getEntityClass(), reference.getEntityIdList());
	}
	
	private <E extends GenericEntity<?, ?>> List<E> doListEntity(Class<E> clazz, Collection<?> ids) {
		if (ids == null || ids.isEmpty()) {
			return Lists.newArrayListWithCapacity(0);
		}
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<E> criteria = builder.createQuery(clazz);
		Root<E> root = criteria.from(clazz);
		criteria.where(((CriteriaBuilderImpl)builder).in(root.get("id"), ids.toArray()));
		
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

	@Override
	@SuppressWarnings("unchecked")
	public <E extends GenericEntity<?, ?>> List<Class<? extends E>> listAssignableEntityTypes(Class<E> superclass) {
		List<Class<? extends E>> classes = Lists.newLinkedList();
		Metamodel metamodel = entityManager.getMetamodel();
		for (EntityType<?> entity : metamodel.getEntities()) {
			Class<?> clazz = entity.getBindableJavaType();
			if (superclass.isAssignableFrom(clazz)) {
				classes.add((Class<? extends E>) clazz);
			}
		}
		return classes;
	}

}

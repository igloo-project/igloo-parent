package org.iglooproject.jpa.business.generic.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.business.generic.model.GenericEntityCollectionReference;
import org.iglooproject.jpa.business.generic.model.IReference;
import org.iglooproject.jpa.business.generic.model.QGenericEntity;

import com.google.common.collect.Lists;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;

public class EntityDaoImpl implements IEntityDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> E getEntity(
			Class<E> clazz, K id) {
		return entityManager.find(clazz, id);
	}
	
	@Override
	public <E extends GenericEntity<?, ?>> E getEntity(IReference<E> reference) {
		return reference == null ? null : entityManager.find(reference.getType(), reference.getId());
	}
	
	@Override
	public <K extends Serializable & Comparable<K>, E extends GenericEntity<K, ?>> List<E>
			listEntity(Class<E> clazz, Collection<K> ids) {
		return listEntityUnsafe(clazz, ids);
	}
	
	@Override
	public <E extends GenericEntity<?, ?>> List<E> listEntity(GenericEntityCollectionReference<?, E> reference) {
		return listEntityUnsafe(reference.getEntityClass(), reference.getEntityIdList());
	}
	
	@SuppressWarnings("unchecked")
	private <K extends Serializable & Comparable<K>, E extends GenericEntity<?, ?>>
			List<E> listEntityUnsafe(Class<? extends E> clazz, Collection<?> ids) {
		if (ids == null || ids.isEmpty()) {
			return Lists.newArrayListWithCapacity(0);
		}
		
		PathBuilder<E> path = new PathBuilder<>(clazz, clazz.getSimpleName());
		QGenericEntity qGenericEntity = new QGenericEntity(path);
		
		return new JPAQuery<E>(entityManager).select(path)
				.from(path)
				.where(qGenericEntity.id.in((Collection<? extends K>)ids))
				.orderBy(qGenericEntity.id.asc())
				.fetch();
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

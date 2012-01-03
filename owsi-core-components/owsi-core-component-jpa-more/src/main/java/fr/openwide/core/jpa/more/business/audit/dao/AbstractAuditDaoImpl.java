/*
 * Copyright (C) 2008-2009 Open Wide
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
package fr.openwide.core.jpa.more.business.audit.dao;

import java.util.Calendar;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Hibernate;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.audit.model.AbstractAudit;
import fr.openwide.core.jpa.more.business.audit.model.AbstractAudit_;

/**
 * <p>
 * Implémentation du DAO {@link IAuditDao}.
 * </p>
 * 
 * @author Open Wide
 */
public abstract class AbstractAuditDaoImpl<T extends AbstractAudit> extends GenericEntityDaoImpl<Integer, T> 
	implements IAuditDao<T> {

	/**
	 * Constructeur.
	 */
	public AbstractAuditDaoImpl() {
	}

	@Override
	public GenericEntity<?, ?> getGenericEntity(Class<? extends GenericEntity<?, ?>> clazz, Integer id) {
		return (GenericEntity<?, ?>) getEntity(clazz, id);
	}

	@Override
	public List<T> listByContextOrObject(GenericEntity<?, ?> entity) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getObjectClass());
		Root<T> root = cq.from(getObjectClass());
		
		cq.select(root);
		
		cq.where(cb.or(
				cb.and(
						cb.equal(root.get(AbstractAudit_.contextClass), Hibernate.getClass(entity).getName()),
						cb.equal(root.get(AbstractAudit_.contextId), entity.getId())
				),
				cb.and(
						cb.equal(root.get(AbstractAudit_.objectClass), Hibernate.getClass(entity).getName()),
						cb.equal(root.get(AbstractAudit_.objectId), entity.getId())
				)
		));
		
		cq.orderBy(cb.desc(root.get(AbstractAudit_.date)));
		
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	public List<T> listBySubject(GenericEntity<?, ?> subject) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getObjectClass());
		Root<T> root = cq.from(getObjectClass());
		
		cq.select(root);
		
		cq.where(cb.and(
				cb.equal(root.get(AbstractAudit_.subjectClass), Hibernate.getClass(subject).getName()),
				cb.equal(root.get(AbstractAudit_.subjectId), subject.getId())
		));
		
		cq.orderBy(cb.desc(root.get(AbstractAudit_.date)));
		
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	public List<T> listToDelete(Integer daysToKeep) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getObjectClass());
		Root<T> root = cq.from(getObjectClass());
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -daysToKeep);
		
		cq.select(root);
		
		cq.where(cb.lessThan(root.get(AbstractAudit_.date), calendar.getTime()));
		
		return getEntityManager().createQuery(cq).getResultList();
	}
}
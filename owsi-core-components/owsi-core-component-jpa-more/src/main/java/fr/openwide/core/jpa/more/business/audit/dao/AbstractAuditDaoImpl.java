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

import org.hibernate.Hibernate;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.path.BeanPath;
import com.mysema.query.types.path.PathBuilder;

import fr.openwide.core.jpa.business.generic.dao.GenericEntityDaoImpl;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.audit.model.AbstractAudit;
import fr.openwide.core.jpa.more.business.audit.model.QAbstractAudit;

/**
 * <p>
 * Implémentation du DAO {@link IAuditDao}.
 * </p>
 * 
 * @author Open Wide
 */
public abstract class AbstractAuditDaoImpl<T extends AbstractAudit> extends GenericEntityDaoImpl<Long, T> 
	implements IAbstractAuditDao<T> {

	/**
	 * Constructeur.
	 */
	public AbstractAuditDaoImpl() {
	}

	@Override
	public <E extends GenericEntity<?, ?>> E getGenericEntity(Class<E> clazz, Long id) {
		return getEntity(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listByContextOrObject(GenericEntity<Long, ?> entity) {
		PathBuilder<? extends AbstractAudit> path = new PathBuilder<AbstractAudit>(getObjectClass(), "abstractAudit");
		QAbstractAudit qAbstractAudit = new QAbstractAudit(path);
		
		return new JPAQuery(getEntityManager()).from(qAbstractAudit)
				.where(
						(
								qAbstractAudit.contextClass.eq(Hibernate.getClass(entity).getName())
								.and(qAbstractAudit.contextId.eq(entity.getId()))
						).or(
								qAbstractAudit.objectClass.eq(Hibernate.getClass(entity).getName())
								.and(qAbstractAudit.objectId.eq(entity.getId()))
						)
				).orderBy(qAbstractAudit.date.desc()).list((BeanPath<T>) qAbstractAudit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listBySubject(GenericEntity<Long, ?> subject) {
		PathBuilder<? extends AbstractAudit> path = new PathBuilder<AbstractAudit>(getObjectClass(), "abstractAudit");
		QAbstractAudit qAbstractAudit = new QAbstractAudit(path);
		
		return new JPAQuery(getEntityManager()).from(qAbstractAudit)
				.where(
						qAbstractAudit.subjectClass.eq(Hibernate.getClass(subject).getName())
						.and(qAbstractAudit.subjectId.eq(subject.getId()))
				).orderBy(qAbstractAudit.date.desc()).list((BeanPath<T>) qAbstractAudit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> listToDelete(Integer daysToKeep) {
		PathBuilder<T> path = new PathBuilder<T>(getObjectClass(), "abstractAudit");
		QAbstractAudit qAbstractAudit = new QAbstractAudit(path);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -daysToKeep);
		
		return new JPAQuery(getEntityManager()).from(qAbstractAudit)
				.where(qAbstractAudit.date.before(calendar.getTime()))
				.list((BeanPath<T>) qAbstractAudit);
	}
}
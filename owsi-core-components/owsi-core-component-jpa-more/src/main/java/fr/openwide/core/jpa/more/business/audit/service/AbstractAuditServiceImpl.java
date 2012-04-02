/*
 * Copyright (C) 2008-2011 Open Wide
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
package fr.openwide.core.jpa.more.business.audit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.GenericEntityServiceImpl;
import fr.openwide.core.jpa.more.business.audit.dao.IAuditDao;
import fr.openwide.core.jpa.more.business.audit.model.AbstractAudit;

/**
 * <p>Implémentation du service {@link IAuditService}.</p>
 */
public abstract class AbstractAuditServiceImpl<T extends AbstractAudit> extends GenericEntityServiceImpl<Long, T> 
		implements IAuditService<T> {

	/**
	 * DAO du journal d'activité.
	 */
	private IAuditDao<T> auditDao;

	/**
	 * Constructeur.
	 * 
	 * @param auditDao DAO du journal d'activité injecté par Spring
	 */
	@Autowired
	public AbstractAuditServiceImpl(IAuditDao<T> auditDao) {
		super(auditDao);
		this.auditDao = auditDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericEntity<?, ?> getContextEntity(T audit) {
		try {
			Class<GenericEntity<?, ?>> clazz = (Class<GenericEntity<?, ?>>) Class.forName(audit.getContextClass());
			return auditDao.getGenericEntity(clazz, audit.getContextId());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getContextDisplayName(T audit) {
		String displayName = audit.getContextDisplayName();
		if (displayName != null) {
			GenericEntity<?, ?> context = getContextEntity(audit);
			if (context != null) {
				displayName = context.getDisplayName();
			}
		}
		return displayName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericEntity<?, ?> getSubjectEntity(T audit) {
		try {
			Class<GenericEntity<?, ?>> clazz = (Class<GenericEntity<?, ?>>) Class.forName(audit.getSubjectClass());
			return auditDao.getGenericEntity(clazz, audit.getSubjectId());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getSubjectDisplayName(T audit) {
		String displayName = audit.getSubjectDisplayName();
		if (displayName != null) {
			GenericEntity<?, ?> subject = getSubjectEntity(audit);
			if (subject != null) {
				displayName = subject.getDisplayName();
			}
		}
		return displayName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericEntity<?, ?> getObjectEntity(T audit) {
		try {
			Class<GenericEntity<?, ?>> clazz = (Class<GenericEntity<?, ?>>) Class.forName(audit.getObjectClass());
			return auditDao.getGenericEntity(clazz, audit.getObjectId());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getObjectDisplayName(T audit) {
		String displayName = audit.getObjectDisplayName();
		if (displayName != null) {
			GenericEntity<?, ?> object = getObjectEntity(audit);
			if (object != null) {
				displayName = object.getDisplayName();
			}
		}
		return displayName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public GenericEntity<?, ?> getSecondaryObjectEntity(T audit) {
		try {
			Class<GenericEntity<?, ?>> clazz = (Class<GenericEntity<?, ?>>) Class.forName(audit
					.getSecondaryObjectClass());
			return auditDao.getGenericEntity(clazz, audit.getSecondaryObjectId());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getSecondaryObjectDisplayName(T audit) {
		String displayName = audit.getSecondaryObjectDisplayName();
		if (displayName != null) {
			GenericEntity<?, ?> secondaryObject = getSecondaryObjectEntity(audit);
			if (secondaryObject != null) {
				displayName = secondaryObject.getDisplayName();
			}
		}
		return displayName;
	}

	@Override
	public List<T> listByContextOrObject(GenericEntity<?, ?> entity) {
		return auditDao.listByContextOrObject(entity);
	}

	@Override
	public List<T> listBySubject(GenericEntity<?, ?> subject) {
		return auditDao.listBySubject(subject);
	}

	@Override
	public List<T> listToDelete(Integer daysToKeep) {
		return auditDao.listToDelete(daysToKeep);
	}
}
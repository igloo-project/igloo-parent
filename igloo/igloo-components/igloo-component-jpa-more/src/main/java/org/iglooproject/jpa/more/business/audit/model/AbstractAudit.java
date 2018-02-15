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
package org.iglooproject.jpa.more.business.audit.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Normalizer;
import org.hibernate.search.annotations.SortableField;
import org.iglooproject.commons.util.CloneUtils;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.audit.model.util.AbstractAuditFeature;
import org.iglooproject.jpa.search.util.HibernateSearchAnalyzer;
import org.iglooproject.jpa.search.util.HibernateSearchNormalizer;

/**
 * <p>
 * Entrée dans le journal d'activité gardant une trace des actions réalisées par
 * les utilisateurs.
 * </p>
 */
@MappedSuperclass
public abstract class AbstractAudit<Action> extends GenericEntity<Long, AbstractAudit<?>> {
	private static final long serialVersionUID = 8453330231866625186L;

	public static final String DATE = "date";
	public static final String DATE_SORT = "dateSort";

	/**
	 * Identifiant technique.
	 */
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Service source de l'Audit.
	 */
	@Basic(optional = false)
	private String service;

	/**
	 * Méthode source de l'Audit.
	 */
	@Basic(optional = false)
	private String method;

	/**
	 * Classe de l'objet contexte.
	 */
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private String contextClass;

	/**
	 * Identifiant de l'objet contexte.
	 */
	@Field
	private Long contextId;

	/**
	 * Nom de l'objet contexte.
	 */
	private String contextDisplayName;

	/**
	 * Classe du sujet ayant effectué l'action.
	 */
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private String subjectClass;

	/**
	 * Identifiant du sujet ayant effectué l'action.
	 */
	@Field
	private Long subjectId;

	/**
	 * Nom et prénom du sujet ayant effectué l'action.
	 */
	private String subjectDisplayName;

	/**
	 * Classe de l'objet.
	 */
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private String objectClass;

	/**
	 * Identifiant de l'objet.
	 */
	@Field
	private Long objectId;

	/**
	 * Nom de l'objet.
	 */
	private String objectDisplayName;

	/**
	 * Classe de l'objet secondaire.
	 */
	@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private String secondaryObjectClass;

	/**
	 * Identifiant de l'objet secondaire.
	 */
	@Field
	private Long secondaryObjectId;

	/**
	 * Nom de l'objet secondaire.
	 */
	private String secondaryObjectDisplayName;

	/**
	 * Message contenant des informations complémentaires à mémoriser.
	 */
	@Column
	@Type(type = "org.iglooproject.jpa.hibernate.usertype.StringClobType")
	private String message;

	/**
	 * Date et heure de création.
	 */
	@Basic(optional = false)
	@Fields({
		@Field(name = DATE, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT)),
		@Field(name = DATE_SORT, normalizer = @Normalizer(definition = HibernateSearchNormalizer.TEXT))
	})
	@SortableField(forField = DATE_SORT)
	private Date date;

	public AbstractAudit() {
		super();
	}

	public AbstractAudit(String service, String method, GenericEntity<Long, ?> subject, AbstractAuditFeature feature, Action action,
			String message) {
		this(new Date(), service, method, null, subject, feature, action, message, null, null);
	}

	public AbstractAudit(String service, String method, GenericEntity<Long, ?> subject, AbstractAuditFeature feature, Action action,
			String message, GenericEntity<Long, ?> object) {
		this(new Date(), service, method, null, subject, feature, action, message, object, null);
	}

	public AbstractAudit(String service, String method, GenericEntity<Long, ?> subject, AbstractAuditFeature feature, Action action,
			String message, GenericEntity<Long, ?> object, GenericEntity<Long, ?> secondaryObject) {
		this(new Date(), service, method, null, subject, feature, action, message, object, secondaryObject);
	}

	public AbstractAudit(String service, String method, GenericEntity<Long, ?> context, GenericEntity<Long, ?> subject,
			AbstractAuditFeature feature, Action action, String message) {
		this(new Date(), service, method, context, subject, feature, action, message, null, null);
	}

	public AbstractAudit(String service, String method, GenericEntity<Long, ?> context, GenericEntity<Long, ?> subject,
			AbstractAuditFeature feature, Action action, String message, GenericEntity<Long, ?> object) {
		this(new Date(), service, method, context, subject, feature, action, message, object, null);
	}

	public AbstractAudit(String service, String method, GenericEntity<Long, ?> context, GenericEntity<Long, ?> subject,
			AbstractAuditFeature feature, Action action, String message, GenericEntity<Long, ?> object,
			GenericEntity<Long, ?> secondaryObject) {
		this(new Date(), service, method, context, subject, feature, action, message, object, secondaryObject);
	}

	public AbstractAudit(Date date, String service, String method, GenericEntity<Long, ?> context, GenericEntity<Long, ?> subject,
			AbstractAuditFeature feature, Action action, String message) {
		this(date, service, method, context, subject, feature, action, message, null, null);
	}

	public AbstractAudit(Date date, String service, String method, GenericEntity<Long, ?> context, GenericEntity<Long, ?> subject,
			AbstractAuditFeature feature, Action action, String message, GenericEntity<Long, ?> object) {
		this(date, service, method, context, subject, feature, action, message, object, null);
	}

	public AbstractAudit(Date date, String service, String method, GenericEntity<Long, ?> context, GenericEntity<Long, ?> subject,
			AbstractAuditFeature feature, Action action, String message, GenericEntity<Long, ?> object,
			GenericEntity<Long, ?> secondaryObject) {
		super();

		setDate(date);
		setService(service);
		setMethod(method);
		if (context != null) {
			setContextClass(Hibernate.getClass(context).getName());
			setContextId(context.getId());
			setContextDisplayName(context.getDisplayName());
		}
		if (subject != null) {
			setSubjectClass(Hibernate.getClass(subject).getName());
			setSubjectId(subject.getId());
			setSubjectDisplayName(subject.getDisplayName());
		}
		setAction(action);
		setFeature(feature);
		setMessage(message);
		if (object != null) {
			setObjectClass(Hibernate.getClass(object).getName());
			setObjectId(object.getId());
			setObjectDisplayName(object.getDisplayName());
		}
		if (secondaryObject != null) {
			setSecondaryObjectClass(Hibernate.getClass(secondaryObject).getName());
			setSecondaryObjectId(secondaryObject.getId());
			setSecondaryObjectDisplayName(secondaryObject.getDisplayName());
		}
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setContextClass(String contextClass) {
		this.contextClass = contextClass;
	}

	public String getContextClass() {
		return contextClass;
	}

	public void setContextId(Long contextId) {
		this.contextId = contextId;
	}

	public Long getContextId() {
		return contextId;
	}

	public void setContextDisplayName(String contextDisplayName) {
		this.contextDisplayName = contextDisplayName;
	}

	public String getContextDisplayName() {
		return contextDisplayName;
	}

	public String getSubjectClass() {
		return subjectClass;
	}

	public void setSubjectClass(String subjectClass) {
		this.subjectClass = subjectClass;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public void setSubjectDisplayName(String subjectDisplayName) {
		this.subjectDisplayName = subjectDisplayName;
	}

	public String getSubjectDisplayName() {
		return subjectDisplayName;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public void setObjectDisplayName(String objectDisplayName) {
		this.objectDisplayName = objectDisplayName;
	}

	public String getObjectDisplayName() {
		return objectDisplayName;
	}

	public void setSecondaryObjectClass(String secondaryObjectClass) {
		this.secondaryObjectClass = secondaryObjectClass;
	}

	public String getSecondaryObjectClass() {
		return secondaryObjectClass;
	}

	public void setSecondaryObjectId(Long secondaryObjectId) {
		this.secondaryObjectId = secondaryObjectId;
	}

	public Long getSecondaryObjectId() {
		return secondaryObjectId;
	}

	public void setSecondaryObjectDisplayName(String secondaryObjectDisplayName) {
		this.secondaryObjectDisplayName = secondaryObjectDisplayName;
	}

	public String getSecondaryObjectDisplayName() {
		return secondaryObjectDisplayName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return CloneUtils.clone(date);
	}

	public void setDate(Date date) {
		this.date = CloneUtils.clone(date);
	}

	/**
	 * Accesseurs de l'action concernée.
	 */
	public abstract Action getAction();
	
	public abstract void setAction(Action action);

	/**
	 * Accesseurs de la fonction concernée.
	 */
	public AbstractAuditFeature getFeature() {
		return null;
	}

	public void setFeature(AbstractAuditFeature feature) {
		if (feature != null) {
			throw new UnsupportedOperationException();
		}
	}

	@Override
	public String getNameForToString() {
		return getService() + "." + getMethod();
	}

	@Override
	public String getDisplayName() {
		return toString();
	}
}

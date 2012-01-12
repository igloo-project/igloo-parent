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
package fr.openwide.core.jpa.more.business.audit.model;

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
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.bridge.builtin.IntegerNumericFieldBridge;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.more.business.audit.model.util.AbstractAuditAction;
import fr.openwide.core.jpa.more.business.audit.model.util.AbstractAuditFeature;
import fr.openwide.core.jpa.search.util.HibernateSearchAnalyzer;

/**
 * <p>
 * Entrée dans le journal d'activité gardant une trace des actions réalisées par
 * les utilisateurs.
 * </p>
 */
@MappedSuperclass
public abstract class AbstractAudit extends GenericEntity<Integer, AbstractAudit> {
	private static final long serialVersionUID = 8453330231866625186L;

	public static final String DATE_SORT_FIELD_NAME = "date_sort";

	/**
	 * Identifiant technique.
	 */
	@Id
	@GeneratedValue
	private Integer id;

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
	@Field(bridge = @FieldBridge(impl = IntegerNumericFieldBridge.class), index = Index.UN_TOKENIZED,
			analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Integer contextId;

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
	@Field(bridge = @FieldBridge(impl = IntegerNumericFieldBridge.class), index = Index.UN_TOKENIZED,
			analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Integer subjectId;

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
	@Field(bridge = @FieldBridge(impl = IntegerNumericFieldBridge.class), index = Index.UN_TOKENIZED,
			analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Integer objectId;

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
	@Field(bridge = @FieldBridge(impl = IntegerNumericFieldBridge.class), index = Index.UN_TOKENIZED,
			analyzer = @Analyzer(definition = HibernateSearchAnalyzer.KEYWORD))
	private Integer secondaryObjectId;

	/**
	 * Nom de l'objet secondaire.
	 */
	private String secondaryObjectDisplayName;

	/**
	 * Message contenant des informations complémentaires à mémoriser.
	 */
	@Column
	@Type(type = "org.hibernate.type.StringClobType")
	private String message;

	/**
	 * Date et heure de création.
	 */
	@Basic(optional = false)
	@Fields({
		@Field(analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT)),
		@Field(name = DATE_SORT_FIELD_NAME, analyzer = @Analyzer(definition = HibernateSearchAnalyzer.TEXT_SORT))
	})
	private Date date;

	public AbstractAudit() {
		super();
	}

	public AbstractAudit(String service, String method, GenericEntity<Integer, ?> subject, AbstractAuditFeature feature, AbstractAuditAction action,
			String message) {
		this(new Date(), service, method, null, subject, feature, action, message, null, null);
	}

	public AbstractAudit(String service, String method, GenericEntity<Integer, ?> subject, AbstractAuditFeature feature, AbstractAuditAction action,
			String message, GenericEntity<Integer, ?> object) {
		this(new Date(), service, method, null, subject, feature, action, message, object, null);
	}

	public AbstractAudit(String service, String method, GenericEntity<Integer, ?> subject, AbstractAuditFeature feature, AbstractAuditAction action,
			String message, GenericEntity<Integer, ?> object, GenericEntity<Integer, ?> secondaryObject) {
		this(new Date(), service, method, null, subject, feature, action, message, object, secondaryObject);
	}

	public AbstractAudit(String service, String method, GenericEntity<Integer, ?> context, GenericEntity<Integer, ?> subject,
			AbstractAuditFeature feature, AbstractAuditAction action, String message) {
		this(new Date(), service, method, context, subject, feature, action, message, null, null);
	}

	public AbstractAudit(String service, String method, GenericEntity<Integer, ?> context, GenericEntity<Integer, ?> subject,
			AbstractAuditFeature feature, AbstractAuditAction action, String message, GenericEntity<Integer, ?> object) {
		this(new Date(), service, method, context, subject, feature, action, message, object, null);
	}

	public AbstractAudit(String service, String method, GenericEntity<Integer, ?> context, GenericEntity<Integer, ?> subject,
			AbstractAuditFeature feature, AbstractAuditAction action, String message, GenericEntity<Integer, ?> object,
			GenericEntity<Integer, ?> secondaryObject) {
		this(new Date(), service, method, context, subject, feature, action, message, object, secondaryObject);
	}

	public AbstractAudit(Date date, String service, String method, GenericEntity<Integer, ?> context, GenericEntity<Integer, ?> subject,
			AbstractAuditFeature feature, AbstractAuditAction action, String message) {
		this(date, service, method, context, subject, feature, action, message, null, null);
	}

	public AbstractAudit(Date date, String service, String method, GenericEntity<Integer, ?> context, GenericEntity<Integer, ?> subject,
			AbstractAuditFeature feature, AbstractAuditAction action, String message, GenericEntity<Integer, ?> object) {
		this(date, service, method, context, subject, feature, action, message, object, null);
	}

	public AbstractAudit(Date date, String service, String method, GenericEntity<Integer, ?> context, GenericEntity<Integer, ?> subject,
			AbstractAuditFeature feature, AbstractAuditAction action, String message, GenericEntity<Integer, ?> object,
			GenericEntity<Integer, ?> secondaryObject) {
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public void setContextId(Integer contextId) {
		this.contextId = contextId;
	}

	public Integer getContextId() {
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

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
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

	public Integer getObjectId() {
		return objectId;
	}

	public void setObjectId(Integer objectId) {
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

	public void setSecondaryObjectId(Integer secondaryObjectId) {
		this.secondaryObjectId = secondaryObjectId;
	}

	public Integer getSecondaryObjectId() {
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
	public abstract AbstractAuditAction getAction();
	
	public abstract void setAction(AbstractAuditAction action);

	/**
	 * Accesseurs de la fonction concernée.
	 */
	public abstract AbstractAuditFeature getFeature();
	
	public abstract void setFeature(AbstractAuditFeature feature);

	@Override
	public String getNameForToString() {
		return getService() + "." + getMethod();
	}

	@Override
	public String getDisplayName() {
		return toString();
	}
}

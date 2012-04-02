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

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.business.generic.service.IGenericEntityService;
import fr.openwide.core.jpa.more.business.audit.model.AbstractAudit;

/**
 * <p>Service concernant les {@link AbstractAudit}.</p>
 */
public interface IAuditService<T extends AbstractAudit> extends IGenericEntityService<Long, T> {

	/**
	 * Renvoie le sujet d'un Audit.
	 * 
	 * @param audit l'Audit concerné
	 * @return le sujet de l'Audit
	 */
	GenericEntity<?, ?> getSubjectEntity(T audit);

	/**
	 * Renvoie le nom du sujet d'un Audit.
	 * 
	 * @param audit l'Audit concerné
	 * @return le nom du sujet de l'Audit
	 */
	String getSubjectDisplayName(T audit);

	/**
	 * Renvoie l'objet d'un Audit.
	 * 
	 * @param audit l'Audit concerné
	 * @return l'objet de l'Audit
	 */
	GenericEntity<?, ?> getObjectEntity(T audit);

	/**
	 * Renvoie le nom de l'objet d'un Audit.
	 * 
	 * @param audit l'Audit concerné
	 * @return le nom de l'objet de l'Audit
	 */
	String getObjectDisplayName(T audit);

	/**
	 * Renvoie l'objet secondaire d'un Audit.
	 * 
	 * @param audit l'Audit concerné
	 * @return l'objet secondaire de l'Audit
	 */
	GenericEntity<?, ?> getSecondaryObjectEntity(T audit);

	/**
	 * Renvoie le nom de l'objet secondaire d'un Audit.
	 * 
	 * @param audit l'Audit concerné
	 * @return le nom de l'objet secondaire de l'Audit
	 */
	String getSecondaryObjectDisplayName(T audit);

	/**
	 * Renvoie la liste des Audits émis par ce sujet.
	 * 
	 * @param subject le sujet concerné
	 * @return les Audits concernés
	 */
	List<T> listBySubject(GenericEntity<?, ?> subject);

	/**
	 * Renvoie la liste des Audits dont le contexte ou l'objet concernent l'entité indiquée.
	 * 
	 * @param entity entité concernée par les Audits
	 * @return liste des Audits correspondants
	 */
	List<T> listByContextOrObject(GenericEntity<?, ?> entity);

	/**
	 * Renvoie le contexte d'un Audit.
	 * 
	 * @param audit l'Audit concerné
	 * @return le contexte de l'Audit
	 */
	GenericEntity<?, ?> getContextEntity(T audit);

	/**
	 * Renvoie le nom du contexte d'un Audit.
	 * 
	 * @param audit l'Audit concerné
	 * @return le nom du contexte de l'Audit
	 */
	String getContextDisplayName(T audit);

	/**
	 * Retourne la liste des lignes d'audit qui sont plus vieilles qu'un certain nombre de jours.
	 * Ce sont les lignes qu'on va supprimer de la base.
	 * 
	 * @param daysToKeep nombre de jours de conservation
	 * @return liste des lignes qu'on va supprimer
	 */
	List<T> listToDelete(Integer daysToKeep);
	
}
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
package org.iglooproject.jpa.more.business.audit.dao;

import java.util.List;

import org.iglooproject.jpa.business.generic.dao.IGenericEntityDao;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.more.business.audit.model.AbstractAudit;

/**
 * <p>DAO concernant les {@link AbstractAudit}.</p>
 */
public interface IAbstractAuditDao<T extends AbstractAudit<?>> extends IGenericEntityDao<Long, T> {

	/**
	 * Renvoie l'entité correspondant à la classe et l'identifiant demandés.
	 * 
	 * @param clazz classe de l'entité
	 * @param id identifiant de l'entité
	 * @return entité
	 */
	<E extends GenericEntity<?, ?>> E getGenericEntity(Class<E> clazz, Long id);

	/**
	 * Renvoie la liste des Audits émis par cet utilisateur.
	 * 
	 * @param subject utilisateur ayant émis les Audits
	 * @return liste des Audits correspondants
	 */
	List<T> listBySubject(GenericEntity<Long, ?> subject);

	/**
	 * Renvoie la liste des Audits dont le contexte ou l'objet concernent
	 * l'entité indiquée.
	 * 
	 * @param entity
	 *            entité concernée par les Audits
	 * @return liste des Audits correspondants
	 */
	List<T> listByContextOrObject(GenericEntity<Long, ?> entity);


	/**
	 * Retourne la liste des lignes d'audit qui sont plus vieilles qu'un certain
	 * nombre de jours. Ce sont les lignes qu'on va supprimer de la base.
	 * 
	 * @param daysToKeep nombre de jours de conservation
	 * @return liste des lignes qu'on va supprimer
	 */
	List<T> listToDelete(Integer daysToKeep);

}
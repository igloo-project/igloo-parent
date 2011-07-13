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
						cb.equal(root.get(AbstractAudit_.contextClass), entity.getClass().getName()),
						cb.equal(root.get(AbstractAudit_.contextId), entity.getId())
				),
				cb.and(
						cb.equal(root.get(AbstractAudit_.objectClass), entity.getClass().getName()),
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
				cb.equal(root.get(AbstractAudit_.subjectClass), subject.getClass().getName()),
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

	/**
	 * Ajoute un filtre sur le sujet de l'audit, au prédicat passé en paramètre.
	 * 
	 * @param filter prédicat sur lequel le filtre doit-être ajouté
	 * @param subjectClass classe du sujet (obligatoire)
	 * @param subjectId id du sujet
	 * @param subjectDisplayName nom du sujet
	 */
//	private void addSubjectCriteria(Predicate filter, String subjectClass, Integer subjectId, String subjectDisplayName) {
//		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//		Root<AbstractAudit> root = cb.createQuery(AbstractAudit.class).from(AbstractAudit.class);
//		
//		if (StringUtils.hasText(subjectClass)) {
//			filter = cb.and(filter, cb.equal(root.get(AbstractAudit_.subjectClass), subjectClass));
//			if (subjectId != null) {
//				filter = cb.and(filter, cb.equal(root.get(AbstractAudit_.subjectId), subjectId));
//			} else if (StringUtils.hasText(subjectDisplayName)) {
//				// TODO : équivalent pour OWSI-Core (à descendre dans SQM?)
//				criteria.add(SagivRestrictions.regex("subjectDisplayName", subjectDisplayName));
//			}
//		}
//	}

	/**
	 * Ajoute un filtre sur la caméra au prédicat passé en paramètre.
	 * 
	 * @param filter prédicat sur lequel le filtre doit-être ajouté
	 * @param cameraId id de la caméra (prioritaire sur le displayName)
	 * @param cameraDisplayName displayName de la caméra
	 */
	// TODO : descendre dans SQM?
//	private void addCameraCriteria(Predicate filter, Class<?> cameraClass, Integer cameraId, String cameraDisplayName) {
//		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//		Root<AbstractAudit> root = cb.createQuery(AbstractAudit.class).from(AbstractAudit.class);
//		
//		Predicate cameraFilter = null;
//		if (cameraId != null || StringUtils.hasLength(cameraDisplayName)) {
//			Predicate cameraObjectFilter = cb.equal(root.get(AbstractAudit_.objectClass), cameraClass.getName());
//			Predicate cameraContextFilter  = cb.equal(root.get(AbstractAudit_.contextClass), cameraClass.getName());
//			Predicate cameraSecondaryObjectFilter  = cb.equal(root.get(AbstractAudit_.secondaryObjectClass), cameraClass.getName());
//			
//			if (cameraId != null) {
//				cameraObjectFilter = cb.and(cameraObjectFilter, cb.equal(root.get(AbstractAudit_.objectId), cameraId));
//				cameraContextFilter = cb.and(cameraObjectFilter, cb.equal(root.get(AbstractAudit_.contextId), cameraId));
//				cameraSecondaryObjectFilter = cb.and(cameraObjectFilter, cb.equal(root.get(AbstractAudit_.secondaryObjectId), cameraId));
//			} else {
//				// TODO : équivalent pour OWSI-Core (à descendre dans SQM?)
//				cameraObjectCriterion = Restrictions.and(cameraObjectCriterion,
//						SagivRestrictions.regex("objectDisplayName", cameraDisplayName));
//				cameraContextCriterion = Restrictions.and(cameraContextCriterion,
//						SagivRestrictions.regex("contextDisplayName", cameraDisplayName));
//				cameraSecondaryObjectCriterion = Restrictions.and(cameraSecondaryObjectCriterion,
//						Restrictions.eq("secondaryObjectDisplayName", cameraDisplayName));
//			}
//			
//			cameraFilter = cb.or(cameraObjectFilter, cameraContextFilter);
//			cameraFilter = cb.or(cameraFilter, cameraSecondaryObjectFilter);
//		}
//
//		if (cameraFilter != null) {
//			filter = cb.and(filter, cameraFilter);
//		}
//	}

	/**
	 * Ajoute des contraintes de dates au prédicat. La méthode est null safe et
	 * ne peut prendre qu'un seul des 2 paramètres.
	 * 
	 * @param filter prédicat sur lequel le filtre doit-être ajouté
	 * @param debut
	 *			date de début (on ne récupère que les événements plus récents
	 *			que cette date)
	 * @param fin
	 *			date de fin (on ne récupère que les événements plus anciens
	 *			que cette date)
	 */
//	private void addDateCriteria(Predicate filter, Date debut, Date fin) {
//		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//		Root<AbstractAudit> root = cb.createQuery(AbstractAudit.class).from(AbstractAudit.class);
//		
//		if (debut != null) {
//			filter = cb.and(filter, cb.greaterThan(root.get(AbstractAudit_.date), debut));
//		}
//		if (fin != null) {
//			filter = cb.and(filter, cb.lessThan(root.get(AbstractAudit_.date), fin));
//		}
//	}

	/**
	 * Ajoute un filtre sur une fonction à un prédicat.
	 * 
	 * @param filter prédicat sur lequel le filtre doit-être ajouté
	 * @param auditFeature fonction
	 */
//	private void addFeatureCriteria(Predicate filter, AbstractAuditFeature auditFeature) {
//		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//		Root<AbstractAudit> root = cb.createQuery(AbstractAudit.class).from(AbstractAudit.class);
//		
//		if (auditFeature != null) {
//			filter = cb.and(filter, cb.equal(root.get(AbstractAudit_.get), auditFeature));
//		}
//	}
}
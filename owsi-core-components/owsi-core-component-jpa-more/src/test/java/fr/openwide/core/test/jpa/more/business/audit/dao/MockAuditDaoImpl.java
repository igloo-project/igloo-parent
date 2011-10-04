package fr.openwide.core.test.jpa.more.business.audit.dao;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import fr.openwide.core.jpa.more.business.audit.dao.AbstractAuditDaoImpl;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAudit;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditAction;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditActionEnum;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditFeature;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditFeatureEnum;

@Repository("mockAuditDao")
public class MockAuditDaoImpl extends AbstractAuditDaoImpl<MockAudit> implements IMockAuditDao {

	@Override
	public MockAuditAction getAuditActionByEnum(MockAuditActionEnum auditActionEnum) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<MockAuditAction> cq = cb.createQuery(MockAuditAction.class);
		Root<MockAuditAction> root = cq.from(MockAuditAction.class);
		
		cq.select(root);
		
		cq.where(cb.equal(root.get("auditActionEnum"), auditActionEnum));
		
		cq.orderBy(cb.asc(root.get("position")));
		
		TypedQuery<MockAuditAction> query = getEntityManager().createQuery(cq);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			return query.getResultList().iterator().next();
		}
	}
	
	@Override
	public MockAuditFeature getAuditFeatureByEnum(MockAuditFeatureEnum auditFeatureEnum) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<MockAuditFeature> cq = cb.createQuery(MockAuditFeature.class);
		Root<MockAuditFeature> root = cq.from(MockAuditFeature.class);
		
		cq.select(root);
		
		cq.where(cb.equal(root.get("auditFeatureEnum"), auditFeatureEnum));
		
		cq.orderBy(cb.asc(root.get("position")));
		
		TypedQuery<MockAuditFeature> query = getEntityManager().createQuery(cq);
		
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (NonUniqueResultException e) {
			return query.getResultList().iterator().next();
		}
	}
}

package fr.openwide.core.test.jpa.more.business;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAudit;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditAction;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditActionEnum;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditFeature;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditFeatureEnum;
import fr.openwide.core.test.jpa.more.business.entity.model.TestEntity;

public class TestAuditService extends AbstractJpaMoreTestCase {

	@Test
	public void testCreate() throws ServiceException, SecurityServiceException {
		TestEntity subject = new TestEntity("Mr. Franck Black");
		testEntityService.create(subject);
		TestEntity context = new TestEntity("context");
		testEntityService.create(context);
		TestEntity object = new TestEntity("object");
		testEntityService.create(object);
		TestEntity secondaryObject = new TestEntity("secondaryObject");
		testEntityService.create(secondaryObject);
		
		MockAuditFeature feature = auditService.getAuditFeatureByEnum(MockAuditFeatureEnum.TEST_FEATURE_1);
		
		MockAuditAction action = auditService.getAuditActionByEnum(MockAuditActionEnum.TEST_ACTION_1);
		
		MockAudit audit1 = new MockAudit(
				"service",
				"method",
				context,
				subject,
				feature,
				action,
				"message",
				object,
				secondaryObject
		);
		
		auditService.create(audit1);
		
		List<MockAudit> audits = auditService.list();
		
		Assert.assertNotNull(audit1.getId());
		Assert.assertEquals(1, audits.size());
		Assert.assertTrue(audits.contains(audit1));
		
		Assert.assertEquals(feature, audit1.getFeature());
		Assert.assertEquals(action, audit1.getAction());
		
		Assert.assertEquals(context.getId(), audit1.getContextId());
		Assert.assertEquals(context.getLabel(), audit1.getContextDisplayName());
		Assert.assertEquals(TestEntity.class.getName(), audit1.getContextClass());
		
		Assert.assertEquals(subject.getId(), audit1.getSubjectId());
		Assert.assertEquals(subject.getLabel(), audit1.getSubjectDisplayName());
		Assert.assertEquals(TestEntity.class.getName(), audit1.getSubjectClass());
		
		Assert.assertEquals(object.getId(), audit1.getObjectId());
		Assert.assertEquals(object.getLabel(), audit1.getObjectDisplayName());
		Assert.assertEquals(TestEntity.class.getName(), audit1.getObjectClass());
		
		Assert.assertEquals(secondaryObject.getId(), audit1.getSecondaryObjectId());
		Assert.assertEquals(secondaryObject.getLabel(), audit1.getSecondaryObjectDisplayName());
		Assert.assertEquals(TestEntity.class.getName(), audit1.getSecondaryObjectClass());
	}
}

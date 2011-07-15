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

		MockAuditFeature feature = auditService
				.getAuditFeatureByEnum(MockAuditFeatureEnum.TEST_FEATURE_1);

		MockAuditAction action = auditService
				.getAuditActionByEnum(MockAuditActionEnum.TEST_ACTION_1);

		MockAudit audit = new MockAudit("service", "method", context, subject,
				feature, action, "message", object, secondaryObject);

		auditService.create(audit);

		List<MockAudit> audits = auditService.list();

		Assert.assertNotNull(audit.getId());

		Assert.assertEquals(audit.getService(), "service");
		Assert.assertEquals(audit.getMethod(), "method");
		Assert.assertEquals(1, audits.size());
		Assert.assertTrue(audits.contains(audit));

		Assert.assertEquals(feature, audit.getFeature());
		Assert.assertEquals(action, audit.getAction());

		Assert.assertEquals(context.getId(), audit.getContextId());
		Assert.assertEquals(context.getLabel(), audit.getContextDisplayName());
		Assert.assertEquals(TestEntity.class.getName(), audit.getContextClass());

		Assert.assertEquals(subject.getId(), audit.getSubjectId());
		Assert.assertEquals(subject.getLabel(), audit.getSubjectDisplayName());
		Assert.assertEquals(TestEntity.class.getName(), audit.getSubjectClass());

		Assert.assertEquals(object.getId(), audit.getObjectId());
		Assert.assertEquals(object.getLabel(), audit.getObjectDisplayName());
		Assert.assertEquals(TestEntity.class.getName(), audit.getObjectClass());

		Assert.assertEquals(secondaryObject.getId(),
				audit.getSecondaryObjectId());
		Assert.assertEquals(secondaryObject.getLabel(),
				audit.getSecondaryObjectDisplayName());
		Assert.assertEquals(TestEntity.class.getName(),
				audit.getSecondaryObjectClass());
	}

	@Test
	public void testGetters() throws ServiceException, SecurityServiceException {
		TestEntity subject = new TestEntity("Mr. Franck Black");
		testEntityService.create(subject);
		TestEntity context = new TestEntity("context");
		testEntityService.create(context);
		TestEntity object = new TestEntity("object");
		testEntityService.create(object);
		TestEntity secondaryObject = new TestEntity("secondaryObject");
		testEntityService.create(secondaryObject);

		MockAudit audit = new MockAudit(
				"service",
				"method",
				context,
				subject,
				auditService
						.getAuditFeatureByEnum(MockAuditFeatureEnum.TEST_FEATURE_1),
				auditService
						.getAuditActionByEnum(MockAuditActionEnum.TEST_ACTION_1),
				"message", object, secondaryObject);

		Assert.assertEquals(auditService.getContextEntity(audit), context);
		Assert.assertEquals(auditService.getSubjectEntity(audit), subject);
		Assert.assertEquals(auditService.getObjectEntity(audit), object);
		Assert.assertEquals(auditService.getSecondaryObjectEntity(audit),
				secondaryObject);
		Assert.assertEquals(auditService.getContextDisplayName(audit),
				context.getDisplayName());
		Assert.assertEquals(auditService.getSubjectDisplayName(audit),
				subject.getDisplayName());
		Assert.assertEquals(auditService.getObjectDisplayName(audit),
				object.getDisplayName());
		Assert.assertEquals(auditService.getSecondaryObjectDisplayName(audit),
				secondaryObject.getDisplayName());

		testEntityService.delete(context);
		testEntityService.delete(subject);
		testEntityService.delete(object);
		testEntityService.delete(secondaryObject);

		Assert.assertEquals(auditService.getContextDisplayName(audit),
				context.getDisplayName());
		Assert.assertEquals(auditService.getSubjectDisplayName(audit),
				subject.getDisplayName());
		Assert.assertEquals(auditService.getObjectDisplayName(audit),
				object.getDisplayName());
		Assert.assertEquals(auditService.getSecondaryObjectDisplayName(audit),
				secondaryObject.getDisplayName());
	}

	@Test
	public void testFilters() throws ServiceException, SecurityServiceException {
		TestEntity subject1 = new TestEntity("subject1");
		testEntityService.create(subject1);
		TestEntity subject2 = new TestEntity("subject2");
		testEntityService.create(subject2);
		TestEntity subject3 = new TestEntity("subject3");
		testEntityService.create(subject3);
		
		TestEntity context1 = new TestEntity("context1");
		testEntityService.create(context1);
		TestEntity context2 = new TestEntity("context2");
		testEntityService.create(context2);
		
		TestEntity object1 = new TestEntity("object1");
		testEntityService.create(object1);
		TestEntity object2 = new TestEntity("object2");
		testEntityService.create(object2);
		
		TestEntity secondaryObject = new TestEntity("secondaryObject");
		testEntityService.create(secondaryObject);
		
		MockAudit audit1 = new MockAudit(
				"service",
				"method",
				context2,
				subject1,
				auditService.getAuditFeatureByEnum(MockAuditFeatureEnum.TEST_FEATURE_1),
				auditService.getAuditActionByEnum(MockAuditActionEnum.TEST_ACTION_1),
				"message",
				object1,
				null
		);
		MockAudit audit2 = new MockAudit(
				"service",
				"method",
				context2,
				subject1,
				auditService.getAuditFeatureByEnum(MockAuditFeatureEnum.TEST_FEATURE_1),
				auditService.getAuditActionByEnum(MockAuditActionEnum.TEST_ACTION_1),
				"message",
				object1,
				null
		);
		MockAudit audit3 = new MockAudit(
				"service",
				"method",
				context1,
				subject3,
				auditService.getAuditFeatureByEnum(MockAuditFeatureEnum.TEST_FEATURE_2),
				auditService.getAuditActionByEnum(MockAuditActionEnum.TEST_ACTION_2),
				"message",
				object1,
				secondaryObject
		);
		MockAudit audit4 = new MockAudit(
				"service",
				"method",
				context1,
				subject2,
				auditService.getAuditFeatureByEnum(MockAuditFeatureEnum.TEST_FEATURE_3),
				auditService.getAuditActionByEnum(MockAuditActionEnum.TEST_ACTION_3),
				"message",
				object2,
				null
		);
		
		auditService.create(audit1);
		auditService.create(audit2);
		auditService.create(audit3);
		auditService.create(audit4);
		
		List<MockAudit> audits;
		
		audits = auditService.listBySubject(subject1);
		Assert.assertEquals(2, audits.size());
		Assert.assertTrue(audits.contains(audit1));
		Assert.assertTrue(audits.contains(audit2));
		
		audits = auditService.listByContextOrObject(context1);
		Assert.assertEquals(2, audits.size());
		Assert.assertTrue(audits.contains(audit3));
		Assert.assertTrue(audits.contains(audit4));
		
		audits = auditService.listByContextOrObject(object2);
		Assert.assertEquals(1, audits.size());
		Assert.assertTrue(audits.contains(audit4));
		
		audits = auditService.listByContextOrObject(secondaryObject);
		Assert.assertEquals(0, audits.size());
	}
}

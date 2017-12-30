package org.iglooproject.test.jpa.more.business;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.generic.dao.GenericListItemDaoImpl;
import org.iglooproject.jpa.more.business.generic.model.GenericListItem;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditAction;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditActionEnum;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditFeature;
import org.iglooproject.test.jpa.more.business.audit.model.MockAuditFeatureEnum;
import org.iglooproject.test.jpa.more.business.audit.model.QMockAuditAction;

public class TestGenericListItemDao extends AbstractJpaMoreTestCase {

	@Autowired
	private GenericListItemDaoImpl genericListItemDao;

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanFeaturesAndActions();
		super.cleanAll();
	}
	
	private void cleanFeaturesAndActions() throws ServiceException, SecurityServiceException {
		for (GenericListItem<?> genericListItem : genericListItemService.list(MockAuditFeature.class)) {
			genericListItemService.delete(genericListItem);
		}
		
		for (GenericListItem<?> genericListItem : genericListItemService.list(MockAuditAction.class)) {
			genericListItemService.delete(genericListItem);
		}
	}

	@Override
	public void init() throws ServiceException, SecurityServiceException {
		super.init();
		initFeaturesAndActions();
	}

	private void initFeaturesAndActions() {
		for (MockAuditFeatureEnum auditFeatureEnum : MockAuditFeatureEnum.values()) {
			MockAuditFeature auditFeature = new MockAuditFeature(auditFeatureEnum.name(), auditFeatureEnum, 1);
			genericListItemService.create(auditFeature);
		}
		
		for (MockAuditActionEnum auditActionEnum : MockAuditActionEnum.values()) {
			MockAuditAction auditAction = new MockAuditAction(auditActionEnum.name(), auditActionEnum, 1);
			genericListItemService.create(auditAction);
		}
	}

	@Test
	public void testGet() throws ServiceException, SecurityServiceException {
		cleanAll();
		MockAuditAction mockAuditAction = new MockAuditAction("mockAuditAction", MockAuditActionEnum.TEST_ACTION_1, 2);
		genericListItemService.create(mockAuditAction);
		
		{
			MockAuditAction mockAuditAction1 = genericListItemDao.getById(MockAuditAction.class, mockAuditAction.getId());
			
			Assert.assertEquals(mockAuditAction, mockAuditAction1);
		}
		
		{
			MockAuditAction mockAuditAction1 = genericListItemDao.getById(MockAuditAction.class, mockAuditAction.getId());
			MockAuditAction mockAuditAction2 = genericListItemDao.getByField(
					QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.position, 2);
			MockAuditAction mockAuditAction3 = genericListItemDao.getByField(
					QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.auditActionEnum, MockAuditActionEnum.TEST_ACTION_1);
			MockAuditAction mockAuditAction4 = genericListItemDao.getByFieldIgnoreCase(QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.label, "Mockauditaction");
			MockAuditAction mockAuditAction5 = genericListItemDao.getByField(
					QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.label, "Mockauditaction");
			
			Assert.assertEquals(mockAuditAction, mockAuditAction1);
			Assert.assertEquals(mockAuditAction, mockAuditAction2);
			Assert.assertEquals(mockAuditAction, mockAuditAction3);
			Assert.assertEquals(mockAuditAction, mockAuditAction4);
			Assert.assertEquals(null, mockAuditAction5);
		}
	}

	@Test
	public void testCount() throws ServiceException, SecurityServiceException {
		Assert.assertEquals(new Long(3), genericListItemDao.count(MockAuditAction.class));
		
		Assert.assertEquals(new Long(3), genericListItemDao.count(QMockAuditAction.mockAuditAction));
		Assert.assertEquals(new Long(1), genericListItemDao.countByField(
				QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.auditActionEnum, MockAuditActionEnum.TEST_ACTION_1));
		Assert.assertEquals(new Long(1), genericListItemDao.countByField(
				QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.auditActionEnum, MockAuditActionEnum.TEST_ACTION_2));
		Assert.assertEquals(new Long(1), genericListItemDao.countByField(
				QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.auditActionEnum, MockAuditActionEnum.TEST_ACTION_3));
		Assert.assertEquals(new Long(3), genericListItemDao.countByField(
				QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.position, 1));
		Assert.assertEquals(new Long(0), genericListItemDao.countByField(
				QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.position, 2));
	}

	@Test
	public void testList() throws ServiceException, SecurityServiceException {
		cleanAll();
		
		{
			List<MockAuditAction> emptyList = genericListItemDao.list(MockAuditAction.class);
			Assert.assertEquals(0, emptyList.size());
		}
		
		{
			List<MockAuditAction> emptyList = genericListItemDao.list(QMockAuditAction.mockAuditAction);
			Assert.assertEquals(0, emptyList.size());
		}
		
		{
			List<MockAuditAction> emptyListByField = genericListItemDao.listByField(
					QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.position, 1, QMockAuditAction.mockAuditAction.id.desc());
			Assert.assertEquals(0, emptyListByField.size());
		}
		
		MockAuditAction mockAuditAction1 = new MockAuditAction("mockAuditAction1", MockAuditActionEnum.TEST_ACTION_1, 1);
		genericListItemService.create(mockAuditAction1);
		
		MockAuditAction mockAuditAction2 = new MockAuditAction("mockAuditAction2", MockAuditActionEnum.TEST_ACTION_1, 2);
		genericListItemService.create(mockAuditAction2);
		
		MockAuditAction mockAuditAction3 = new MockAuditAction("mockAuditAction3", MockAuditActionEnum.TEST_ACTION_2, 1);
		genericListItemService.create(mockAuditAction3);
		
		MockAuditAction mockAuditAction4 = new MockAuditAction("mockAuditAction4", MockAuditActionEnum.TEST_ACTION_2, 2);
		genericListItemService.create(mockAuditAction4);
		
		MockAuditAction mockAuditAction5 = new MockAuditAction("mockAuditAction5", MockAuditActionEnum.TEST_ACTION_2, 1);
		genericListItemService.create(mockAuditAction5);
		
		{
			List<MockAuditAction> list1 = genericListItemDao.list(MockAuditAction.class);
			Assert.assertEquals(5, list1.size());
			Assert.assertTrue(list1.contains(mockAuditAction1));
			Assert.assertTrue(list1.contains(mockAuditAction2));
			Assert.assertTrue(list1.contains(mockAuditAction3));
			Assert.assertTrue(list1.contains(mockAuditAction4));
			Assert.assertTrue(list1.contains(mockAuditAction5));
		}
		
		{
			List<MockAuditAction> list1 = genericListItemDao.list(QMockAuditAction.mockAuditAction);
			Assert.assertEquals(5, list1.size());
			Assert.assertTrue(list1.contains(mockAuditAction1));
			Assert.assertTrue(list1.contains(mockAuditAction2));
			Assert.assertTrue(list1.contains(mockAuditAction3));
			Assert.assertTrue(list1.contains(mockAuditAction4));
			Assert.assertTrue(list1.contains(mockAuditAction5));
		}
		
		{
			List<MockAuditAction> list2 = genericListItemDao.listByField(
					QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.auditActionEnum, MockAuditActionEnum.TEST_ACTION_1, QMockAuditAction.mockAuditAction.position.asc());
			Assert.assertEquals(2, list2.size());
			Assert.assertEquals(list2.get(0), mockAuditAction1);
			Assert.assertEquals(list2.get(1), mockAuditAction2);
		}
		
		{
			List<MockAuditAction> list3 = genericListItemDao.listByField(
					QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.position, 1, QMockAuditAction.mockAuditAction.id.desc());
			Assert.assertEquals(3, list3.size());
			Assert.assertTrue(list3.contains(mockAuditAction1));
			Assert.assertTrue(list3.contains(mockAuditAction3));
			Assert.assertTrue(list3.contains(mockAuditAction5));
		}
		
		{
			List<MockAuditAction> list4 = genericListItemDao.listByField(
					QMockAuditAction.mockAuditAction, QMockAuditAction.mockAuditAction.auditActionEnum, MockAuditActionEnum.TEST_ACTION_3, QMockAuditAction.mockAuditAction.id.desc());
			Assert.assertEquals(0, list4.size());
		}
	}

}

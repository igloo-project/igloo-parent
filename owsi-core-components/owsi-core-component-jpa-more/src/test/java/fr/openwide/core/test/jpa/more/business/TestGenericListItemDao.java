package fr.openwide.core.test.jpa.more.business;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.generic.dao.IGenericListItemDao;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditAction;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditActionEnum;
import fr.openwide.core.test.jpa.more.business.audit.model.MockAuditAction_;

public class TestGenericListItemDao extends AbstractJpaMoreTestCase {

	@Autowired
	private IGenericListItemDao genericListItemDao;

	@Test
	public void testGet() throws ServiceException, SecurityServiceException {
		cleanAll();
		MockAuditAction mockAuditAction = new MockAuditAction("mockAuditAction", MockAuditActionEnum.TEST_ACTION_1, 2);
		genericListItemService.create(mockAuditAction);
		
		MockAuditAction mockAuditAction1 = genericListItemDao.getById(MockAuditAction.class, mockAuditAction.getId());
		MockAuditAction mockAuditAction2 = genericListItemDao.getByField(MockAuditAction.class, MockAuditAction_.position, 2);
		MockAuditAction mockAuditAction3 = genericListItemDao.getByField(MockAuditAction.class, MockAuditAction_.auditActionEnum, MockAuditActionEnum.TEST_ACTION_1);
		MockAuditAction mockAuditAction4 = genericListItemDao.getByFieldIgnoreCase(MockAuditAction.class, MockAuditAction_.label, "Mockauditaction");
		MockAuditAction mockAuditAction5 = genericListItemDao.getByField(MockAuditAction.class, MockAuditAction_.label, "Mockauditaction");
		
		Assert.assertEquals(mockAuditAction, mockAuditAction1);
		Assert.assertEquals(mockAuditAction, mockAuditAction2);
		Assert.assertEquals(mockAuditAction, mockAuditAction3);
		Assert.assertEquals(mockAuditAction, mockAuditAction4);
		Assert.assertEquals(null, mockAuditAction5);
	}

	@Test
	public void testCount() throws ServiceException, SecurityServiceException {
		Assert.assertEquals(new Long(3), genericListItemDao.count(MockAuditAction.class));
		
		Assert.assertEquals(new Long(1),
				genericListItemDao.countByField(MockAuditAction.class, MockAuditAction_.auditActionEnum, MockAuditActionEnum.TEST_ACTION_1));
		
		Assert.assertEquals(new Long(1),
				genericListItemDao.countByField(MockAuditAction.class, MockAuditAction_.auditActionEnum, MockAuditActionEnum.TEST_ACTION_2));
		
		Assert.assertEquals(new Long(1),
				genericListItemDao.countByField(MockAuditAction.class, MockAuditAction_.auditActionEnum, MockAuditActionEnum.TEST_ACTION_3));
		
		Assert.assertEquals(new Long(3),
				genericListItemDao.countByField(MockAuditAction.class, MockAuditAction_.position, 1));
		
		Assert.assertEquals(new Long(0),
				genericListItemDao.countByField(MockAuditAction.class, MockAuditAction_.position, 2));
	}

	@Test
	public void testList() throws ServiceException, SecurityServiceException {
		cleanAll();
		List<MockAuditAction> emptyList = genericListItemDao.list(MockAuditAction.class);
		Assert.assertEquals(0, emptyList.size());
		
		List<MockAuditAction> emptyListByField = genericListItemDao.listByField(MockAuditAction.class, MockAuditAction_.position, 1);
		Assert.assertEquals(0, emptyListByField.size());
		
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
		
		List<MockAuditAction> list1 = genericListItemDao.list(MockAuditAction.class);
		Assert.assertEquals(5, list1.size());
		Assert.assertTrue(list1.contains(mockAuditAction1));
		Assert.assertTrue(list1.contains(mockAuditAction2));
		Assert.assertTrue(list1.contains(mockAuditAction3));
		Assert.assertTrue(list1.contains(mockAuditAction4));
		Assert.assertTrue(list1.contains(mockAuditAction5));
		
		List<MockAuditAction> list2 = genericListItemDao.listByField(MockAuditAction.class, MockAuditAction_.auditActionEnum, MockAuditActionEnum.TEST_ACTION_1);
		Assert.assertEquals(2, list2.size());
		Assert.assertTrue(list2.contains(mockAuditAction1));
		Assert.assertTrue(list2.contains(mockAuditAction2));
		
		List<MockAuditAction> list3 = genericListItemDao.listByField(MockAuditAction.class, MockAuditAction_.position, 1);
		Assert.assertEquals(3, list3.size());
		Assert.assertTrue(list3.contains(mockAuditAction1));
		Assert.assertTrue(list3.contains(mockAuditAction3));
		Assert.assertTrue(list3.contains(mockAuditAction5));
		
		List<MockAuditAction> list4 = genericListItemDao.listByField(MockAuditAction.class, MockAuditAction_.auditActionEnum, MockAuditActionEnum.TEST_ACTION_3);
		Assert.assertEquals(0, list4.size());
	}

}

package org.iglooproject.test.jpa.more.business;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.more.business.task.service.IQueuedTaskHolderService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestQueuedTaskHolderService extends AbstractJpaMoreTestCase {

	@Autowired
	private IQueuedTaskHolderService queuedTaskHolderService;

	@Ignore
	@Test
	public void testGetListConsumable() throws ServiceException, SecurityServiceException {
		queuedTaskHolderService.getListConsumable("whatever");
	}

}

package org.iglooproject.test.jpa.more.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.junit.AbstractTestCase;
import org.iglooproject.jpa.more.business.generic.service.IGenericListItemService;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IConfigurablePropertyService;
import org.iglooproject.test.jpa.more.business.entity.service.ITestEntityService;
import org.iglooproject.test.jpa.more.config.spring.JpaMoreTestConfig;

@ContextConfiguration(classes = JpaMoreTestConfig.class)
public abstract class AbstractJpaMoreTestCase extends AbstractTestCase {

	@Autowired
	protected IGenericListItemService genericListItemService;

	@Autowired
	protected ITestEntityService testEntityService;

	@Autowired
	protected IConfigurablePropertyService propertyService;
	
	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(testEntityService);
		mutablePropertyDao.cleanInTransaction();
	}
}

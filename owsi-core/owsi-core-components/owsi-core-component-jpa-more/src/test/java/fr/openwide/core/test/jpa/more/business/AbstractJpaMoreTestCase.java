package fr.openwide.core.test.jpa.more.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.junit.AbstractTestCase;
import fr.openwide.core.jpa.more.business.generic.service.IGenericListItemService;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.service.IConfigurablePropertyService;
import fr.openwide.core.test.jpa.more.business.entity.service.ITestEntityService;
import fr.openwide.core.test.jpa.more.config.spring.JpaMoreTestConfig;

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

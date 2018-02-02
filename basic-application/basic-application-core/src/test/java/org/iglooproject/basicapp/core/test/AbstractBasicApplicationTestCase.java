package org.iglooproject.basicapp.core.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import org.iglooproject.basicapp.core.business.user.service.IUserGroupService;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.basicapp.core.test.config.spring.BasicApplicationCoreTestCommonConfig;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.spring.config.ExtendedTestApplicationContextInitializer;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.test.jpa.junit.AbstractTestCase;

@ContextConfiguration(
		classes = BasicApplicationCoreTestCommonConfig.class,
		initializers = ExtendedTestApplicationContextInitializer.class
)
public abstract class AbstractBasicApplicationTestCase extends AbstractTestCase {

	@Autowired
	protected IUserService userService;
	
	@Autowired
	protected IUserGroupService userGroupService;
	
	@Autowired
	protected IAuthorityService authorityService;
	
	@Autowired
	protected IPropertyService propertyService;
	
	@Autowired
	private IMutablePropertyDao mutablePropertyDao;
	
	@Override
	public void init() throws ServiceException, SecurityServiceException {
		super.init();
		initAuthorities();
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(userService);
		cleanEntities(userGroupService);
		cleanEntities(authorityService);
		mutablePropertyDao.cleanInTransaction();
	}

	private void initAuthorities() throws ServiceException, SecurityServiceException {
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_AUTHENTICATED));
	}
}

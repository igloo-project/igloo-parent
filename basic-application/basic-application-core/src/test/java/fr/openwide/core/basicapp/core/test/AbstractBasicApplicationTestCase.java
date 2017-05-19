package fr.openwide.core.basicapp.core.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.basicapp.core.business.user.service.IUserGroupService;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.test.config.spring.BasicApplicationCoreTestCommonConfig;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.junit.AbstractTestCase;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.authority.service.IAuthorityService;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.spring.config.ExtendedTestApplicationContextInitializer;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.spring.property.service.IPropertyService;

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

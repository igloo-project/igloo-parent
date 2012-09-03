package fr.openwide.core.basicapp.core.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.business.user.service.IUserGroupService;
import fr.openwide.core.basicapp.core.business.user.service.IUserService;
import fr.openwide.core.basicapp.core.test.config.spring.BasicApplicationCoreTestCommonConfig;
import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.junit.AbstractTestCase;
import fr.openwide.core.jpa.security.business.authority.model.Authority;
import fr.openwide.core.jpa.security.business.authority.service.IAuthorityService;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;

@ContextConfiguration(classes = BasicApplicationCoreTestCommonConfig.class)
public abstract class AbstractBasicApplicationTestCase extends AbstractTestCase {

	@Autowired
	protected IUserService userService;
	
	@Autowired
	protected IUserGroupService userGroupService;
	
	@Autowired
	protected IAuthorityService authorityService;

	public void init() throws ServiceException, SecurityServiceException {
		super.init();
		initAuthorities();
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanUsers();
		cleanUserGroups();
		cleanAuthorities();
	}

	protected void cleanUsers() throws ServiceException, SecurityServiceException {
		for (User user : userService.list()) {
			userService.delete(user);
		}
	}

	protected void cleanUserGroups() throws ServiceException, SecurityServiceException {
		for (UserGroup userGroup : userGroupService.list()) {
			userGroupService.delete(userGroup);
		}
	}

	protected void cleanAuthorities() throws ServiceException, SecurityServiceException {
		for (Authority authority : authorityService.list()) {
			authorityService.delete(authority);
		}
	}

	private void initAuthorities() throws ServiceException, SecurityServiceException {
		authorityService.create(new Authority(CoreAuthorityConstants.ROLE_AUTHENTICATED));
	}
}

package org.iglooproject.basicapp.core.security.service;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.service.IUserService;
import org.iglooproject.jpa.security.service.CoreAuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class BasicApplicationAuthenticationServiceImpl extends CoreAuthenticationServiceImpl implements IBasicApplicationAuthenticationService {

	@Autowired
	private IUserService userService;

	@Override
	public User getUser() {
		return userService.getAuthenticatedUser();
	}

}

package basicapp.back.security.service;

import org.iglooproject.jpa.security.service.CoreAuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import basicapp.back.business.user.model.User;
import basicapp.back.business.user.service.IUserService;

public class BasicApplicationAuthenticationServiceImpl extends CoreAuthenticationServiceImpl implements IBasicApplicationAuthenticationService {

	@Autowired
	private IUserService userService;

	@Override
	public User getUser() {
		return userService.getAuthenticatedUser();
	}

}

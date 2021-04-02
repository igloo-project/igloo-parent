package org.igloo.spring.autoconfigure.security.stub;

import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.service.ISecurityUserService;
import org.springframework.stereotype.Service;

@Service
public class StubSecurityUserServiceImpl<U extends GenericUser<U, ?>> implements ISecurityUserService<U> {

	@Override
	public U getByUsername(String username) {
		return null;
	}

	@Override
	public U getByUsernameCaseInsensitive(String username) {
		return null;
	}

}

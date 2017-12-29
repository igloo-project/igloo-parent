package org.iglooproject.showcase.core.business.user.search;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import org.iglooproject.showcase.core.business.user.model.User;

@Component
@Scope("prototype")
public class UserSearchQueryImpl extends AbstractUserSearchQueryImpl<User> implements IUserSearchQuery {

	protected UserSearchQueryImpl() {
		super(User.class);
	}

}
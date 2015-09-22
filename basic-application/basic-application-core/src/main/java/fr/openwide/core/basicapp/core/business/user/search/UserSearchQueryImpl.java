package fr.openwide.core.basicapp.core.business.user.search;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.openwide.core.basicapp.core.business.user.model.User;

@Component
@Scope("prototype")
public class UserSearchQueryImpl extends AbstractUserSearchQueryImpl<User> implements IUserSearchQuery {

	protected UserSearchQueryImpl() {
		super(User.class);
	}

}
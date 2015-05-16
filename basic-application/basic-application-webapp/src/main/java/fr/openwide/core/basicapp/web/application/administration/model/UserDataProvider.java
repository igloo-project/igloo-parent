package fr.openwide.core.basicapp.web.application.administration.model;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.search.IUserSearchQuery;

public class UserDataProvider extends AbstractUserDataProvider<User> {
	
	private static final long serialVersionUID = -8540890431031886412L;
	
	public UserDataProvider() {
		super(User.class);
	}

	@Override
	protected IUserSearchQuery createSearchQuery() {
		return createSearchQuery(IUserSearchQuery.class);
	}
}

package org.iglooproject.basicapp.web.application.administration.model;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.search.IUserSearchQuery;
import org.iglooproject.wicket.more.application.CoreWicketApplication;

public class UserDataProvider extends AbstractUserDataProvider<User> {

	private static final long serialVersionUID = -8540890431031886412L;

	public UserDataProvider() {
		super(User.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IUserSearchQuery<User> createSearchQuery() {
		return (IUserSearchQuery<User>) CoreWicketApplication.get().getApplicationContext().getBean(IUserSearchQuery.class, User.class);
	}

	

}

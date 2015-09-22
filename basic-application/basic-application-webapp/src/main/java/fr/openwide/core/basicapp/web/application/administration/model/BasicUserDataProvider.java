package fr.openwide.core.basicapp.web.application.administration.model;

import fr.openwide.core.basicapp.core.business.user.model.BasicUser;
import fr.openwide.core.basicapp.core.business.user.search.IBasicUserSearchQuery;

public class BasicUserDataProvider extends AbstractUserDataProvider<BasicUser> {
	
	private static final long serialVersionUID = -8540890431031886412L;
	
	public BasicUserDataProvider() {
		super(BasicUser.class);
	}

	@Override
	protected IBasicUserSearchQuery createSearchQuery() {
		return createSearchQuery(IBasicUserSearchQuery.class);
	}
}

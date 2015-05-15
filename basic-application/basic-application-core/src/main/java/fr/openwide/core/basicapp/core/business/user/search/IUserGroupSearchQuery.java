package fr.openwide.core.basicapp.core.business.user.search;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.jpa.more.business.search.query.ISearchQuery;

public interface IUserGroupSearchQuery extends ISearchQuery<UserGroup, UserGroupSort> {
	
	IUserGroupSearchQuery user(User user);
	
	IUserGroupSearchQuery name(String name);
	
}

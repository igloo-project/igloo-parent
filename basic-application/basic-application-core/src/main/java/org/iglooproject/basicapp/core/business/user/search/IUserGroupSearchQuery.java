package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;

public interface IUserGroupSearchQuery extends ISearchQuery<UserGroup, UserGroupSort> {

  IUserGroupSearchQuery user(User user);

  IUserGroupSearchQuery name(String name);
}

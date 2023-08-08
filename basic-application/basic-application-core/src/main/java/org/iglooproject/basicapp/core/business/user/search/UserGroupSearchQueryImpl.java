package org.iglooproject.basicapp.core.business.user.search;

import java.util.Set;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.search.query.OldAbstractHibernateSearchSearchQuery;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

@Component
@Scope("prototype")
public class UserGroupSearchQueryImpl extends OldAbstractHibernateSearchSearchQuery<UserGroup, UserGroupSort> implements IUserGroupSearchQuery {

	public UserGroupSearchQueryImpl() {
		super(UserGroup.class, UserGroupSort.NAME);
	}

	@Override
	public IUserGroupSearchQuery user(User user) {
		Set<Long> ids = Sets.newHashSet();
		for (UserGroup userGroup : user.getGroups()) {
			ids.add(userGroup.getId());
		}
		if (ids.isEmpty()) {
			must(matchIfGiven(UserGroup.ID, -1L));
		} else {
			must(matchOneIfGiven(UserGroup.ID, ids));
		}
		return this;
	}
	
	@Override
	public IUserGroupSearchQuery name(String name) {
		must(matchIfGiven(UserGroup.NAME, name));
		return this;
	}

}
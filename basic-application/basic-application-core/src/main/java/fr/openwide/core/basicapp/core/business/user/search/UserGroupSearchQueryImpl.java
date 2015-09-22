package fr.openwide.core.basicapp.core.business.user.search;

import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserGroup;
import fr.openwide.core.basicapp.core.util.binding.Bindings;
import fr.openwide.core.jpa.more.business.search.query.AbstractHibernateSearchSearchQuery;

@Component
@Scope("prototype")
public class UserGroupSearchQueryImpl extends AbstractHibernateSearchSearchQuery<UserGroup, UserGroupSort> implements IUserGroupSearchQuery {

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
			must(matchIfGiven(Bindings.userGroup().id(), -1L));
		} else {
			must(matchOneIfGiven(Bindings.userGroup().id(), ids));
		}
		return this;
	}
	
	@Override
	public IUserGroupSearchQuery name(String name) {
		must(matchIfGiven(Bindings.userGroup().name(), name));
		return this;
	}
}
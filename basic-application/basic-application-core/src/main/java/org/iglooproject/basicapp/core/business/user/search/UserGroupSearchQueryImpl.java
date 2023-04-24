package org.iglooproject.basicapp.core.business.user.search;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UserGroupSearchQueryImpl implements IUserGroupSearchQuery {

	public UserGroupSearchQueryImpl() {
		//TODO: igloo-boot
//		super(UserGroup.class, UserGroupSort.NAME);
	}

	@Override
	public IUserGroupSearchQuery user(User user) {
//		Set<Long> ids = Sets.newHashSet();
//		for (UserGroup userGroup : user.getGroups()) {
//			ids.add(userGroup.getId());
//		}
//		if (ids.isEmpty()) {
//			must(matchIfGiven(Bindings.userGroup().id(), -1L));
//		} else {
//			must(matchOneIfGiven(Bindings.userGroup().id(), ids));
//		}
		return this;
	}
	
	@Override
	public IUserGroupSearchQuery name(String name) {
//		must(matchIfGiven(Bindings.userGroup().name(), name));
		return this;
	}

	@Override
	public ISearchQuery<UserGroup, UserGroupSort> sort(Map<UserGroupSort, SortOrder> sortMap) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public List<UserGroup> fullList() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public List<UserGroup> list(long offset, long limit) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

}
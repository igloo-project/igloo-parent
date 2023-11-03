package org.iglooproject.basicapp.core.business.user.search;

import org.bindgen.Bindable;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

@Bindable
public class UserGroupSearchQueryData implements ISearchQueryData<UserGroup> {

	private String name;

	private User user;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

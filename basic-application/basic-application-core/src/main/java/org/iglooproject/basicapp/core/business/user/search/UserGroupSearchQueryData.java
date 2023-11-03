package org.iglooproject.basicapp.core.business.user.search;

import org.bindgen.Bindable;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

@Bindable
public class UserGroupSearchQueryData implements ISearchQueryData<UserGroup> {

	private String name;

	private User user;

	private BasicUser basicUser;

	private TechnicalUser technicalUser;

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

	public BasicUser getBasicUser() {
		return basicUser;
	}

	public void setBasicUser(BasicUser basicUser) {
		this.basicUser = basicUser;
	}

	public TechnicalUser getTechnicalUser() {
		return technicalUser;
	}

	public void setTechnicalUser(TechnicalUser technicalUser) {
		this.technicalUser = technicalUser;
	}

}

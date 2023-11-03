package org.iglooproject.basicapp.core.business.user.search;

import org.bindgen.Bindable;
import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.search.query.ISearchQueryData;

@Bindable
public class BasicUserSearchQueryData implements ISearchQueryData<BasicUser> {

	private String term;

	private UserGroup group;

	private EnabledFilter enabledFilter = EnabledFilter.ENABLED_ONLY;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}

	public EnabledFilter getEnabledFilter() {
		return enabledFilter;
	}

	public void setEnabledFilter(EnabledFilter enabledFilter) {
		this.enabledFilter = enabledFilter;
	}

}

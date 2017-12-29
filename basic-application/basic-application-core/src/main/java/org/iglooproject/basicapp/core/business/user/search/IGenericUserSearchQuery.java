package org.iglooproject.basicapp.core.business.user.search;

import org.springframework.context.annotation.Scope;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;

@Scope("prototype")
public interface IGenericUserSearchQuery<U extends User> extends ISearchQuery<U, UserSort> {
	
	IGenericUserSearchQuery<U> name(String nameToMatch);

	IGenericUserSearchQuery<U> group(UserGroup groupToMatch);

	IGenericUserSearchQuery<U> includeInactive(Boolean includeInactives);

	IGenericUserSearchQuery<U> nameAutocomplete(String terms);
	
}

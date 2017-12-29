package org.iglooproject.showcase.core.business.user.search;

import org.springframework.context.annotation.Scope;

import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.iglooproject.showcase.core.business.user.model.User;

@Scope("prototype")
public interface IGenericUserSearchQuery<U extends User> extends ISearchQuery<U, UserSort> {
	
	IGenericUserSearchQuery<U> name(String nameToMatch);

	IGenericUserSearchQuery<U> includeInactive(Boolean includeInactives);

	IGenericUserSearchQuery<U> nameAutocomplete(String terms);
	
}

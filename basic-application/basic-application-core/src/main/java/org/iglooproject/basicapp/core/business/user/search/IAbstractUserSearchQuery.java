package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public interface IAbstractUserSearchQuery<U extends User> extends ISearchQuery<U, UserSort> {

	IAbstractUserSearchQuery<U> name(String nameToMatch);

	IAbstractUserSearchQuery<U> group(UserGroup groupToMatch);

	IAbstractUserSearchQuery<U> includeInactive(Boolean includeInactives);

	IAbstractUserSearchQuery<U> nameAutocomplete(String terms);

}

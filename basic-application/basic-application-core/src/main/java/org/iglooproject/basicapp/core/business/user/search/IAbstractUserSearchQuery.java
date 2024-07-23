package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.business.generic.model.search.EnabledFilter;
import org.iglooproject.jpa.more.business.search.query.ISearchQuery;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public interface IAbstractUserSearchQuery<U extends User> extends ISearchQuery<U, UserSort> {

  IAbstractUserSearchQuery<U> nameAutocomplete(String terms);

  IAbstractUserSearchQuery<U> name(String name);

  IAbstractUserSearchQuery<U> group(UserGroup group);

  IAbstractUserSearchQuery<U> enabled(EnabledFilter enabledFilter);
}

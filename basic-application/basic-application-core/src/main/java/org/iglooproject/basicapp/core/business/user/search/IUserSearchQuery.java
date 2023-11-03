package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface IUserSearchQuery extends IHibernateSearchSearchQuery<User, UserSort, UserSearchQueryData> {

}

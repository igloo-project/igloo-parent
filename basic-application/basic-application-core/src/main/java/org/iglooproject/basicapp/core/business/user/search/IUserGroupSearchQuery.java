package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.UserGroup;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface IUserGroupSearchQuery extends IHibernateSearchSearchQuery<UserGroup, UserGroupSort, UserGroupSearchQueryData> {

}

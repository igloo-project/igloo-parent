package basicapp.back.business.user.search;

import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

import basicapp.back.business.user.model.UserGroup;

public interface IUserGroupSearchQuery extends IHibernateSearchSearchQuery<UserGroup, UserGroupSort, UserGroupSearchQueryData> {

}

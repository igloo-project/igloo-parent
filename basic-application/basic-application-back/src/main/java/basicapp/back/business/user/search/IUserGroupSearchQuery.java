package basicapp.back.business.user.search;

import basicapp.back.business.user.model.UserGroup;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface IUserGroupSearchQuery
    extends IHibernateSearchSearchQuery<UserGroup, UserGroupSort, UserGroupSearchQueryData> {}

package basicapp.back.business.user.search;

import basicapp.back.business.user.model.User;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface IUserSearchQuery
    extends IHibernateSearchSearchQuery<User, UserSort, UserSearchQueryData> {}

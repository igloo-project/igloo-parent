package basicapp.back.business.user.search;

import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

import basicapp.back.business.user.model.User;

public interface IUserSearchQuery extends IHibernateSearchSearchQuery<User, UserSort, UserSearchQueryData> {

}

package basicapp.back.business.user.search;

import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

import basicapp.back.business.user.model.BasicUser;

public interface IBasicUserSearchQuery extends IHibernateSearchSearchQuery<BasicUser, BasicUserSort, BasicUserSearchQueryData> {

}

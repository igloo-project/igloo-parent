package basicapp.back.business.user.search;

import basicapp.back.business.user.model.BasicUser;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface IBasicUserSearchQuery
    extends IHibernateSearchSearchQuery<BasicUser, BasicUserSort, BasicUserSearchQueryData> {}

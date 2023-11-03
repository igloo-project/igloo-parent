package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.BasicUser;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface IBasicUserSearchQuery extends IHibernateSearchSearchQuery<BasicUser, BasicUserSort, BasicUserSearchQueryData> {

}

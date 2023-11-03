package org.iglooproject.basicapp.core.business.user.search;

import org.iglooproject.basicapp.core.business.user.model.TechnicalUser;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface ITechnicalUserSearchQuery extends IHibernateSearchSearchQuery<TechnicalUser, TechnicalUserSort, TechnicalUserSearchQueryData> {

}

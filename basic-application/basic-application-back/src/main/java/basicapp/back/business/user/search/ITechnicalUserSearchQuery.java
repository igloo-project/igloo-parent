package basicapp.back.business.user.search;

import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

import basicapp.back.business.user.model.TechnicalUser;

public interface ITechnicalUserSearchQuery extends IHibernateSearchSearchQuery<TechnicalUser, TechnicalUserSort, TechnicalUserSearchQueryData> {

}

package basicapp.back.business.user.search;

import basicapp.back.business.user.model.TechnicalUser;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface ITechnicalUserSearchQuery
    extends IHibernateSearchSearchQuery<
        TechnicalUser, TechnicalUserSort, TechnicalUserSearchQueryData> {}

package basicapp.back.business.user.search;

import basicapp.back.business.user.model.User;
import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IHibernateSearchSearchQuery;

public interface IUserSearchQuery
    extends IHibernateSearchSearchQuery<User, UserSort, UserSearchQueryData> {

  Collection<Long> listIds(UserSearchQueryData data, Map<UserSort, ISort.SortOrder> sorts);
}

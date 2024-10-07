package basicapp.back.business.role.search;

import basicapp.back.business.role.model.Role;
import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort;
import org.iglooproject.jpa.more.search.query.IJpaSearchQuery;

public interface IRoleSearchQuery extends IJpaSearchQuery<Role, RoleSort, RoleSearchQueryData> {

  Collection<Long> listIds(RoleSearchQueryData data, Map<RoleSort, ISort.SortOrder> sorts);
}

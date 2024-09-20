package basicapp.back.business.role.search;

import basicapp.back.business.role.model.QRole;
import basicapp.back.business.role.model.Role;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.iglooproject.jpa.more.business.sort.ISort.SortOrder;
import org.springframework.stereotype.Service;

@Service
public class RoleSearchQueryImpl implements IRoleSearchQuery {

  private static final QRole qRole = QRole.role;

  @PersistenceContext private EntityManager entityManager;

  @Override
  public List<Role> list(
      RoleSearchQueryData data, Map<RoleSort, SortOrder> sorts, Integer offset, Integer limit) {
    if (!checkLimit(limit)) {
      return List.of();
    }

    JPAQuery<Role> query = new JPAQuery<>(entityManager).select(qRole).from(qRole);

    predicateContributor(query, data);
    sortContributor(query, sorts);
    hitsContributor(query, offset, limit);

    return query.fetch();
  }

  @Override
  public long size(RoleSearchQueryData data) {
    JPAQuery<Role> query = new JPAQuery<>(entityManager).select(qRole).from(qRole);

    predicateContributor(query, data);

    @SuppressWarnings("deprecation")
    long size = query.fetchCount();

    return size;
  }

  @Override
  public Collection<Long> listIds(RoleSearchQueryData data, Map<RoleSort, SortOrder> sorts) {
    JPAQuery<Long> query = new JPAQuery<>(entityManager).select(qRole.id).from(qRole);

    predicateContributor(query, data);
    sortContributor(query, sorts);

    return query.fetch();
  }

  private void predicateContributor(JPAQuery<?> query, RoleSearchQueryData data) {
    if (data.getUser() != null) {
      query.where(qRole.users.any().eq(data.getUser()));
    }
  }
}

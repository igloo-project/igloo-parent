package basicapp.back.business.role.dao;

import basicapp.back.business.role.model.QRole;
import basicapp.back.business.role.model.Role;
import com.google.common.collect.Ordering;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Collection;
import java.util.List;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends GenericEntityDaoImpl<Long, Role> implements IRoleDao {

  private static final QRole qRole = QRole.role;

  @Override
  public List<Role> listByIds(Collection<Long> ids) {
    Collection<Role> roles =
        new JPAQuery<>(getEntityManager())
            .select(qRole)
            .from(qRole)
            .where(qRole.id.in(ids))
            .orderBy(qRole.id.asc())
            .fetch();

    return Ordering.explicit(List.copyOf(ids)).onResultOf(Role::getId).immutableSortedCopy(roles);
  }

  @Override
  public Role getByTitle(String title) {
    return new JPAQuery<>(getEntityManager())
        .select(qRole)
        .from(qRole)
        .where(qRole.title.eq(title))
        .fetchOne();
  }
}

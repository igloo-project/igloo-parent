package basicapp.back.business.role.dao;

import basicapp.back.business.role.model.QRole;
import basicapp.back.business.role.model.Role;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Optional;
import org.iglooproject.jpa.business.generic.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDaoImpl extends GenericEntityDaoImpl<Long, Role> implements IRoleDao {

  private static final QRole qRole = QRole.role;

  @Override
  public Optional<Role> getByTitle(String title) {
    return Optional.ofNullable(
        new JPAQuery<>(getEntityManager())
            .select(qRole)
            .from(qRole)
            .where(qRole.title.eq(title))
            .fetchOne());
  }
}

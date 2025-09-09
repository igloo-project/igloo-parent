package basicapp.back.business.role.repository;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.model.Role_;
import basicapp.back.business.user.model.User;
import basicapp.back.business.user.model.User_;
import org.iglooproject.jpa.jparepository.CriteriaJoinBuilder;
import org.springframework.data.jpa.domain.Specification;

public class RoleSpecifications {

  private RoleSpecifications() {}

  public static Specification<Role> user(User user) {
    return (role, cq, cb) ->
        cb.equal(CriteriaJoinBuilder.root(role).join(Role_.USERS).get(User_.ID), user.getId());
  }
}

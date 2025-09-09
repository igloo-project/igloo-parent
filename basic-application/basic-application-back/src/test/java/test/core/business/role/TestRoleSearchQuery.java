package test.core.business.role;

import basicapp.back.business.role.model.Role;
import basicapp.back.business.role.repository.IRoleRepository;
import basicapp.back.business.role.search.IRoleSearchQuery;
import basicapp.back.business.role.search.RoleSearchQueryData;
import basicapp.back.business.role.search.RoleSort;
import basicapp.back.business.user.model.User;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import test.core.AbstractBasicApplicationTestCase;
import test.core.config.spring.SpringBootTestBasicApplication;

@SpringBootTestBasicApplication
public class TestRoleSearchQuery extends AbstractBasicApplicationTestCase {

  @Autowired private IRoleSearchQuery roleSearchQuery;
  @Autowired private IRoleRepository roleRepository;

  @Nested
  class TestSearchRole {

    Role role1;
    Role role2;
    Role role3;
    Role role4;
    User user1;
    User user2;

    @Test
    void testSearchRole_findAllNoOrder() {
      initTests();
      Assertions.assertThat(
              roleSearchQuery.list(new RoleSearchQueryData(), Map.of(), 0, Integer.MAX_VALUE))
          .size()
          .isEqualTo(4)
          .returnToIterable()
          .containsExactly(role4, role1, role2, role3);
      Assertions.assertThat(roleSearchQuery.listIds(new RoleSearchQueryData(), Map.of()))
          .size()
          .isEqualTo(4)
          .returnToIterable()
          .containsExactly(role4.getId(), role1.getId(), role2.getId(), role3.getId());
    }

    @Test
    void testSearchRole_findAllOrderByTitle() {
      initTests();

      Assertions.assertThat(
              roleSearchQuery.list(
                  new RoleSearchQueryData(),
                  Map.of(RoleSort.TITLE, RoleSort.TITLE.getDefaultOrder()),
                  0,
                  Integer.MAX_VALUE))
          .size()
          .isEqualTo(4)
          .returnToIterable()
          .containsExactly(role2, role1, role3, role4);
      Assertions.assertThat(
              roleSearchQuery.listIds(
                  new RoleSearchQueryData(),
                  Map.of(RoleSort.TITLE, RoleSort.TITLE.getDefaultOrder())))
          .size()
          .isEqualTo(4)
          .returnToIterable()
          .containsExactly(role2.getId(), role1.getId(), role3.getId(), role4.getId());
    }

    @Test
    void testSearchRole_findByUserWithMultiplesRolesOrderByTitle() {
      initTests();

      RoleSearchQueryData roleSearchQueryData = new RoleSearchQueryData();
      roleSearchQueryData.setUser(user1);
      Assertions.assertThat(
              roleSearchQuery.list(
                  roleSearchQueryData,
                  Map.of(RoleSort.TITLE, RoleSort.TITLE.getDefaultOrder()),
                  0,
                  Integer.MAX_VALUE))
          .containsExactly(role2, role1);
      Assertions.assertThat(
              roleSearchQuery.listIds(
                  roleSearchQueryData, Map.of(RoleSort.TITLE, RoleSort.TITLE.getDefaultOrder())))
          .size()
          .isEqualTo(2)
          .returnToIterable()
          .containsExactly(role2.getId(), role1.getId());
    }

    @Test
    void testSearchRole_findByUserWithOneRoleOrderByTitle() {
      initTests();

      RoleSearchQueryData roleSearchQueryDataUser = new RoleSearchQueryData();
      roleSearchQueryDataUser.setUser(user2);
      Assertions.assertThat(
              roleSearchQuery.list(
                  roleSearchQueryDataUser,
                  Map.of(RoleSort.TITLE, RoleSort.TITLE.getDefaultOrder()),
                  0,
                  Integer.MAX_VALUE))
          .size()
          .isEqualTo(1)
          .returnToIterable()
          .containsExactly(role3);
      Assertions.assertThat(
              roleSearchQuery.listIds(
                  roleSearchQueryDataUser,
                  Map.of(RoleSort.TITLE, RoleSort.TITLE.getDefaultOrder())))
          .size()
          .isEqualTo(1)
          .returnToIterable()
          .containsExactly(role3.getId());
    }

    private void initTests() {
      role1 = entityDatabaseHelper.createRole(r -> r.setTitle("B"), true);
      role2 = entityDatabaseHelper.createRole(r -> r.setTitle("A"), true);
      role3 = entityDatabaseHelper.createRole(r -> r.setTitle("C"), true);
      role4 = roleRepository.findById(-1L).orElseThrow();
      user1 =
          entityDatabaseHelper.createUser(
              u -> {
                u.addRole(role1);
                u.addRole(role2);
              },
              true);
      user2 = entityDatabaseHelper.createUser(u -> u.addRole(role3), true);
      entityManagerReset();
    }
  }
}

package test.core.security;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_WRITE;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ANONYMOUS;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_AUTHENTICATED;

import basicapp.back.business.user.model.atomic.UserType;
import com.google.common.collect.ImmutableSortedSet;
import igloo.security.ICoreUserDetailsService;
import igloo.security.UserDetails;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.model.NamedPermission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import test.core.AbstractBasicApplicationTestCase;
import test.core.config.spring.SpringBootTestBasicApplication;

@SpringBootTestBasicApplication
public class TestUserDetailsService extends AbstractBasicApplicationTestCase {

  @Autowired private ICoreUserDetailsService userDetailsService;

  @Test
  void testGetAuthoritiesAndPermissions_TechnicalUser()
      throws SecurityServiceException, ServiceException {
    entityDatabaseHelper.createRole(
        r ->
            r.setPermissions(
                ImmutableSortedSet.of(GLOBAL_REFERENCE_DATA_READ, GLOBAL_REFERENCE_DATA_WRITE)),
        true);

    entityDatabaseHelper.createRole(
        r -> r.setPermissions(ImmutableSortedSet.of(GLOBAL_ROLE_WRITE, GLOBAL_ROLE_READ)), true);
    entityDatabaseHelper.createUser(u -> u.setUsername("username"), true);

    entityManagerReset();
    UserDetails userDetails = userDetailsService.loadUserByUsername("username");
    Assertions.assertThat(userDetails).isNotNull();
    Assertions.assertThat(userDetails.getUsername()).isEqualTo("username");

    Assertions.assertThat(userDetails.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactlyInAnyOrder(ROLE_ADMIN, ROLE_ANONYMOUS, ROLE_AUTHENTICATED);
    List<String> permissions =
        userDetails.getPermissions().stream()
            .filter(permission -> permission instanceof NamedPermission)
            .map(permission -> ((NamedPermission) permission).getName())
            .toList();

    Assertions.assertThat(permissions)
        .containsExactlyInAnyOrder(
            GLOBAL_REFERENCE_DATA_READ,
            GLOBAL_REFERENCE_DATA_WRITE,
            GLOBAL_ROLE_WRITE,
            GLOBAL_ROLE_READ);
  }

  @Test
  void testGetAuthoritiesAndPermissions_BasicUser()
      throws SecurityServiceException, ServiceException {
    entityDatabaseHelper.createRole(
        r ->
            r.setPermissions(
                ImmutableSortedSet.of(GLOBAL_REFERENCE_DATA_READ, GLOBAL_REFERENCE_DATA_WRITE)),
        true);

    addPermissions(
        entityDatabaseHelper.createUser(
            u -> {
              u.setUsername("username");
              u.setType(UserType.BASIC);
            },
            true),
        GLOBAL_ROLE_READ,
        GLOBAL_ROLE_WRITE);

    entityManagerReset();
    UserDetails userDetails = userDetailsService.loadUserByUsername("username");
    Assertions.assertThat(userDetails).isNotNull();
    Assertions.assertThat(userDetails.getUsername()).isEqualTo("username");

    Assertions.assertThat(userDetails.getAuthorities())
        .extracting(GrantedAuthority::getAuthority)
        .containsExactlyInAnyOrder(ROLE_ANONYMOUS, ROLE_AUTHENTICATED);
    List<String> permissions =
        userDetails.getPermissions().stream()
            .filter(permission -> permission instanceof NamedPermission)
            .map(permission -> ((NamedPermission) permission).getName())
            .toList();

    Assertions.assertThat(permissions)
        .containsExactlyInAnyOrder(GLOBAL_ROLE_WRITE, GLOBAL_ROLE_READ);
  }
}

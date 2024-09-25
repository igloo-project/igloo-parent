package org.iglooproject.test.jpa.security;

import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ADMIN;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_ANONYMOUS;
import static org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants.ROLE_AUTHENTICATED;

import igloo.security.ICoreUserDetailsService;
import igloo.security.UserDetails;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaSecurityTestCase;
import org.iglooproject.test.jpa.security.business.person.model.MockUser;
import org.iglooproject.test.jpa.security.config.spring.SpringBootTestJpaSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

@SpringBootTestJpaSecurity
class TestCoreJpaUserDetailsService extends AbstractJpaSecurityTestCase {

  @Autowired private ICoreUserDetailsService coreJpaUserDetailsService;

  @SuppressWarnings("unchecked")
  @Test
  // TODO a supprimer
  void testLoadUserByUsername() throws ServiceException, SecurityServiceException {

    MockUser userAdmin = createMockTechnicalUser();
    MockUser userBasic = createMockBasicUser();
    MockUser userBasic_NoPermissions = createMockUser("newUser", "newUser", "newUser");

    // Admin person
    UserDetails userDetailsAdmin =
        coreJpaUserDetailsService.loadUserByUsername(userAdmin.getUsername());
    Collection<GrantedAuthority> grantedAuthoritiesAdmin =
        (Collection<GrantedAuthority>) userDetailsAdmin.getAuthorities();

    Assertions.assertThat(grantedAuthoritiesAdmin).size().isEqualTo(6);
    Assertions.assertThat(grantedAuthoritiesAdmin)
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly(
            ROLE_ADMIN,
            ROLE_ANONYMOUS,
            ROLE_AUTHENTICATED,
            ROLE_GROUP_1,
            ROLE_GROUP_2,
            ROLE_GROUP_3);
    // person
    UserDetails userDetailsBasicWithRole =
        coreJpaUserDetailsService.loadUserByUsername(userBasic.getUsername());

    Collection<GrantedAuthority> grantedAuthoritiesWithRole =
        (Collection<GrantedAuthority>) userDetailsBasicWithRole.getAuthorities();
    Assertions.assertThat(grantedAuthoritiesWithRole).size().isEqualTo(2);
    Assertions.assertThat(grantedAuthoritiesWithRole)
        .extracting(GrantedAuthority::getAuthority)
        .containsExactly(ROLE_ANONYMOUS, ROLE_AUTHENTICATED);

    // Group2 person
    UserDetails userDetailsBasicWithoutRole =
        coreJpaUserDetailsService.loadUserByUsername(userBasic_NoPermissions.getUsername());
    Collection<GrantedAuthority> grantedAuthoritiesWithoutRole =
        (Collection<GrantedAuthority>) userDetailsBasicWithoutRole.getAuthorities();

    Assertions.assertThat(grantedAuthoritiesWithoutRole).isEmpty();

    // TODO a garder !!
    // Test reimplemented QueryDSL methods
    MockUser userInactive = createMockUser("inactive", "inactive", "inactive");
    userInactive.setEnabled(false);
    mockUserService.update(userInactive);

    Assertions.assertThat(mockUserService.count()).isEqualTo(4);
    Assertions.assertThat(mockUserService.countEnabled()).isEqualTo(3);
  }
}

package org.iglooproject.test.jpa.security.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.service.CoreJpaUserDetailsServiceImpl;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class TestJpaSecurityUserDetailsServiceImpl extends CoreJpaUserDetailsServiceImpl
    implements ITestJpaSecurityUserDetailsService {

  public static final String TECHNICAL_USERNAME = "TECHNICAL_USERNAME";

  @Override
  protected Pair<Set<GrantedAuthority>, Set<Permission>> getAuthoritiesAndPermissions(IUser user) {
    Set<GrantedAuthority> grantedAuthorities =
        getAuthorities(user).stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableSet());
    return new ImmutablePair<>(grantedAuthorities, Set.of());
  }

  private List<String> getAuthorities(IUser user) {
    if (Objects.equals(user.getUsername(), System.getProperty("user.name"))) {
      return List.of(CoreAuthorityConstants.ROLE_AUTHENTICATED);
    }
    if (Objects.equals(user.getUsername(), TECHNICAL_USERNAME)) {
      return List.of(CoreAuthorityConstants.ROLE_AUTHENTICATED, CoreAuthorityConstants.ROLE_ADMIN);
    }
    return List.of();
  }
}

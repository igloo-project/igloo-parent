package org.iglooproject.jpa.security.service;

import igloo.security.CoreUserDetails;
import igloo.security.ICoreUserDetailsService;
import igloo.security.UserDetails;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.business.user.service.ICoreUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.hierarchicalroles.NullRoleHierarchy;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

/**
 * 2024-09-25 - this class is now abstract to force implementation of {@link
 * CoreJpaUserDetailsServiceImpl#getAuthoritiesAndPermissions} to define how to get User Authority
 * Spring Security and User Permission Spring ACL for logged-in user
 *
 * <p>All custom hierarchies was deleted, Spring implements {@link NullRoleHierarchy} by default
 */
public abstract class CoreJpaUserDetailsServiceImpl implements ICoreUserDetailsService {

  public static final String EMPTY_PASSWORD_HASH = "*NO PASSWORD*";

  @Autowired private ICoreUserService<?> userService;

  private AuthenticationUsernameComparison authenticationUsernameComparison =
      AuthenticationUsernameComparison.CASE_SENSITIVE;

  public void setAuthenticationUsernameComparison(
      AuthenticationUsernameComparison authenticationUsernameComparison) {
    this.authenticationUsernameComparison = authenticationUsernameComparison;
  }

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException, DataAccessException {
    IUser user = getUserByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException(
          "CoreJpaUserDetailsServiceImpl: User not found: " + username);
    }
    Pair<Set<GrantedAuthority>, Set<Permission>> authoritiesAndPermissions =
        getAuthoritiesAndPermissions(user);

    // hierarchies are not used, add ROLE_ANONYMOUS for every authentification
    // authoritiesAndPermissions pair can be immutable, add in a new Collection
    Set<GrantedAuthority> grantedAuthorities =
        Stream.concat(
                authoritiesAndPermissions.getLeft().stream(),
                Set.of(new SimpleGrantedAuthority(CoreAuthorityConstants.ROLE_ANONYMOUS)).stream())
            .collect(Collectors.toSet());

    return new CoreUserDetails(
        user.getUsername(),
        // In any case, we can't pass an empty password hash to the CoreUserDetails
        StringUtils.hasText(user.getPasswordHash()) ? user.getPasswordHash() : EMPTY_PASSWORD_HASH,
        user.isEnabled(),
        true,
        true,
        true,
        grantedAuthorities,
        authoritiesAndPermissions.getRight());
  }

  protected IUser getUserByUsername(String username) {
    if (AuthenticationUsernameComparison.CASE_INSENSITIVE.equals(
        authenticationUsernameComparison)) {
      return userService.getByUsernameCaseInsensitive(username);
    } else {
      return userService.getByUsername(username);
    }
  }

  /**
   * Override this in order to define how to get User Authority Spring Security and User Permission
   * Spring ACL for logged-in user
   *
   * @param user connected user
   * @return pair with set of user authority and user permissions
   */
  protected abstract Pair<Set<GrantedAuthority>, Set<Permission>> getAuthoritiesAndPermissions(
      IUser user);
}

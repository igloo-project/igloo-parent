package org.iglooproject.jpa.security.service;

import igloo.security.CoreUserDetails;
import igloo.security.ICoreUserDetailsService;
import igloo.security.UserDetails;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.iglooproject.jpa.security.business.authority.model.Authority;
import org.iglooproject.jpa.security.business.authority.service.IAuthorityService;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.business.user.service.ISecurityUserService;
import org.iglooproject.jpa.security.hierarchy.IPermissionHierarchy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;

// TODO RFO ajouter une doc sur le fait de l'avoir passé abstrait
public abstract class CoreJpaUserDetailsServiceImpl implements ICoreUserDetailsService {

  public static final String EMPTY_PASSWORD_HASH = "*NO PASSWORD*";

  @Autowired private ISecurityUserService<?> userService;

  // TODO RFO supprimer + bean
  @Autowired private RoleHierarchy roleHierarchy;

  // TODO RFO supprimer + bean
  @Autowired private IPermissionHierarchy permissionHierarchy;

  @Autowired private PermissionFactory permissionFactory;

  @Autowired private IAuthorityService authorityService;

  private AuthenticationUsernameComparison authenticationUsernameComparison =
      AuthenticationUsernameComparison.CASE_SENSITIVE;

  public void setAuthenticationUsernameComparison(
      AuthenticationUsernameComparison authenticationUsernameComparison) {
    this.authenticationUsernameComparison = authenticationUsernameComparison;
  }

  // TODO RFO tester l'accès à anounymopus
  //
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

    // TODO RFO est-ce qu'on dégage les hierarchies ?
    // TODO ajouter le role anonymous au set de Authority
    // TODO ajouter les propertiees dans springSecuritypropertiesID -> igloo.system.roles
    // TODO ajouter dans la basicApp la properties
    // TODO supprimer le roleHierarchieImpl, verifier que spring pousse un NullRoleHierarchy
    Collection<? extends GrantedAuthority> expandedGrantedAuthorities =
        roleHierarchy.getReachableGrantedAuthorities(authoritiesAndPermissions.getLeft());
    addCustomPermissionsFromAuthorities(
        expandedGrantedAuthorities, authoritiesAndPermissions.getRight());
    addPermissionsFromAuthorities(expandedGrantedAuthorities, authoritiesAndPermissions.getRight());
    Collection<Permission> expandedReachablePermissions =
        permissionHierarchy.getReachablePermissions(authoritiesAndPermissions.getRight());

    return new CoreUserDetails(
        user.getUsername(),
        // In any case, we can't pass an empty password hash to the CoreUserDetails
        StringUtils.hasText(user.getPasswordHash()) ? user.getPasswordHash() : EMPTY_PASSWORD_HASH,
        user.isEnabled(),
        true,
        true,
        true,
        expandedGrantedAuthorities,
        expandedReachablePermissions);
  }

  protected IUser getUserByUsername(String username) {
    if (AuthenticationUsernameComparison.CASE_INSENSITIVE.equals(
        authenticationUsernameComparison)) {
      return userService.getByUsernameCaseInsensitive(username);
    } else {
      return userService.getByUsername(username);
    }
  }

  protected void addCustomPermissionsFromAuthorities(
      Collection<? extends GrantedAuthority> expandedGrantedAuthorities,
      Set<Permission> permissions) {
    for (GrantedAuthority grantedAuthority : expandedGrantedAuthorities) {
      Authority authority = authorityService.getByName(grantedAuthority.getAuthority());
      if (authority == null) {
        continue;
      }
      addPermissions(permissions, authority.getCustomPermissionNames());
    }
  }

  /**
   * Override this in order to define some permissions based on the (expanded) granted authorities.
   */
  protected void addPermissionsFromAuthorities(
      Collection<? extends GrantedAuthority> expandedGrantedAuthorities,
      Set<Permission> permissions) {
    // Does nothing by default
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

  protected void addPermissions(Set<Permission> permissions, Collection<String> permissionNames) {
    for (String permissionName : permissionNames) {
      permissions.add(permissionFactory.buildFromName(permissionName));
    }
  }
}

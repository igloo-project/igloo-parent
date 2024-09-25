package org.iglooproject.jpa.security.service;

import static org.iglooproject.spring.property.SpringSecurityPropertyIds.ROLES_SYSTEM;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import igloo.security.CoreUserDetails;
import igloo.security.ICoreUserDetailsService;
import igloo.security.UserDetails;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.util.UserConstants;
import org.iglooproject.spring.property.service.IPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class CoreSecurityServiceImpl implements ISecurityService {

  public static final String SYSTEM_USER_NAME = "system";

  @Autowired protected ICoreUserDetailsService userDetailsService;

  @Autowired protected ICorePermissionEvaluator permissionEvaluator;

  @Autowired private IAuthenticationService authenticationService;

  @Autowired private IPropertyService propertyService;

  @Override
  public boolean hasRole(Authentication authentication, String role) {
    if (authentication != null && role != null) {
      return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }
    return false;
  }

  @Override
  public boolean hasRole(IUser user, String role) {
    if (user == null) {
      return false;
    }

    return hasRole(getAuthentication(user), role);
  }

  @Override
  public boolean hasSystemRole(Authentication authentication) {
    return hasRole(authentication, CoreAuthorityConstants.ROLE_SYSTEM);
  }

  @Override
  public boolean hasSystemRole(IUser user) {
    return hasRole(user, CoreAuthorityConstants.ROLE_SYSTEM);
  }

  @Override
  public boolean hasAdminRole(Authentication authentication) {
    return hasRole(authentication, CoreAuthorityConstants.ROLE_ADMIN);
  }

  @Override
  public boolean hasAdminRole(IUser user) {
    return hasRole(user, CoreAuthorityConstants.ROLE_ADMIN);
  }

  @Override
  public boolean hasAuthenticatedRole(Authentication authentication) {
    return hasRole(authentication, CoreAuthorityConstants.ROLE_AUTHENTICATED);
  }

  @Override
  public boolean hasAuthenticatedRole(IUser user) {
    return hasRole(user, CoreAuthorityConstants.ROLE_AUTHENTICATED);
  }

  @Override
  public boolean isAnonymousAuthority(String grantedAuthoritySid) {
    return CoreAuthorityConstants.ROLE_ANONYMOUS.equals(grantedAuthoritySid);
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities(Authentication authentication) {
    return AuthenticationUtil.getAuthorities(authentication);
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities(IUser user) {
    return getAuthorities(getAuthentication(user));
  }

  protected void authenticateAs(String username, String... additionalAuthorities) {
    clearAuthentication();
    UsernamePasswordAuthenticationToken authentication;

    authentication = createAuthenticationFromUser(username, additionalAuthorities);

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  /**
   * 2024-09-25 igloo system hierarchies was deleted. Projects must defined all roles in
   * ROLES_SYSTEM property to authenticate system with all roles
   */
  protected void authenticateAsSystem() {
    List<? extends GrantedAuthority> rolesSystem =
        propertyService.get(ROLES_SYSTEM).stream().map(SimpleGrantedAuthority::new).toList();

    UserDetails userDetails =
        new CoreUserDetails(
            UserConstants.SYSTEM_USER_NAME,
            UserConstants.NO_CREDENTIALS,
            rolesSystem,
            Collections.emptyList());
    UsernamePasswordAuthenticationToken authentication =
        UsernamePasswordAuthenticationToken.authenticated(
            userDetails, UserConstants.NO_CREDENTIALS, rolesSystem);
    authentication.setDetails(userDetails);
    AuthenticationUtil.setAuthentication(authentication);
  }

  @Override
  public void clearAuthentication() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  /**
   * Exécute une {@link Callable} en tant qu'utilisateur système. Le contexte de sécurité est
   * modifié au début de la tâche et rétabli à la fin de la tâche.
   *
   * @param task un objet de type {@link Callable}
   * @return l'objet retourné par la méthode {@link Callable#call()}
   */
  @Override
  public <T> T runAsSystem(Callable<T> task) {
    Authentication originalAuthentication = AuthenticationUtil.getAuthentication();
    authenticateAsSystem();
    try {
      return task.call();
    } catch (Exception e) {
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      throw new RuntimeException(e);
    } finally {
      AuthenticationUtil.setAuthentication(originalAuthentication);
    }
  }

  @Override
  public <T> T runAs(Callable<T> task, String username, String... additionalAuthorities) {
    Authentication originalAuthentication = AuthenticationUtil.getAuthentication();
    authenticateAs(username, additionalAuthorities);
    try {
      return task.call();
    } catch (Exception e) {
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      throw new RuntimeException(e);
    } finally {
      AuthenticationUtil.setAuthentication(originalAuthentication);
    }
  }

  protected Authentication getAuthentication(IUser user) {
    return getAuthentication(user.getUsername());
  }

  protected Authentication getAuthentication(String username) {
    // Return current authentication if username is matching
    Authentication authentication = authenticationService.getAuthentication();
    if (authentication != null
        && (authentication.getPrincipal() instanceof UserDetails details)
        && Objects.equal(details.getUsername(), username)) {
      return authentication;
    }

    // build a new authentication
    try {
      return createAuthenticationFromUser(username);
    } catch (DisabledException e) {
      return null;
    }
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Object securedObject, Permission requirePermission) {
    return permissionEvaluator.hasPermission(authentication, securedObject, requirePermission);
  }

  @Override
  public boolean hasPermission(IUser user, Object securedObject, Permission requirePermission) {
    return hasPermission(getAuthentication(user), securedObject, requirePermission);
  }

  @Override
  public boolean hasPermission(Authentication authentication, Permission permission) {
    return permissionEvaluator.hasPermission(authentication, permission);
  }

  @Override
  public boolean hasPermission(IUser user, Permission permission) {
    return hasPermission(getAuthentication(user), permission);
  }

  @Override
  public Collection<Permission> getPermissions(Authentication authentication) {
    return AuthenticationUtil.getPermissions(authentication);
  }

  @Override
  public boolean isSuperUser(Authentication authentication) {
    return permissionEvaluator.isSuperUser(authentication);
  }

  private UsernamePasswordAuthenticationToken createAuthenticationFromUser(
      String username, String... additionalAuthorities) {
    UsernamePasswordAuthenticationToken authentication;
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    Set<GrantedAuthority> authorities = Sets.newHashSet(userDetails.getAuthorities());
    if (additionalAuthorities != null) {
      for (String additionalAuthority : additionalAuthorities) {
        authorities.add(new SimpleGrantedAuthority(additionalAuthority));
      }
    }

    authentication =
        UsernamePasswordAuthenticationToken.authenticated(
            additionalAuthorities, UserConstants.NO_CREDENTIALS, authorities);
    authentication.setDetails(userDetails);
    return authentication;
  }
}

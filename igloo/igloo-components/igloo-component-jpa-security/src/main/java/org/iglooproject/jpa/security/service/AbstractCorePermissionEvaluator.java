package org.iglooproject.jpa.security.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.iglooproject.jpa.security.business.user.model.GenericUser;
import org.iglooproject.jpa.security.business.user.service.IGenericUserService;
import org.iglooproject.jpa.security.hierarchy.IPermissionHierarchy;
import org.iglooproject.jpa.security.model.CoreUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public abstract class AbstractCorePermissionEvaluator<T extends GenericUser<T, ?>>
    implements ICorePermissionEvaluator {

  @Autowired private PermissionFactory permissionFactory;

  @Autowired private IPermissionHierarchy permissionHierarchy;

  @Autowired private IGenericUserService<T> userService;

  @Autowired private ISecurityService securityService;

  /**
   * Vérifie qu'un utilisateur possède la permission souhaitée
   *
   * @param user peut être <code>null</code> dans le cas d'une authentification anonyme
   */
  protected abstract boolean hasPermission(
      T user, Object targetDomainObject, Permission permission);

  protected T getUser(Authentication authentication) {
    if (authentication == null) {
      return null;
    }

    if (authentication.getPrincipal() instanceof UserDetails) {
      return userService.getByUsername(((UserDetails) authentication.getPrincipal()).getUsername());
    }

    return null;
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Object targetDomainObject, Object permission) {
    /*
     * On applique les vérifications même pour un superUser, contrairement à hasPermission(Authentication, Object)
     * En effet, il se peut que certaines permissions sur les objets soient attribuées en fonction de règles métier
     * qui peuvent interdire même à l'administrateur d'exécuter une action (par exemple clôturer un dossier déjà clôturé)
     */
    if (authentication == null) {
      return false;
    }

    /*
     * On donne une dérogation à l'utilisateur system car on risque sinon d'avoir des soucis dans les permission evaluator
     * car on n'a alors pas de IUser associé et on ne peut pas déterminer le périmètre autorisé.
     * Dans l'idéal, il faudrait utiliser l'objet Authentication jusque dans les *PermissionEvaluator.
     */
    if (securityService.hasSystemRole(authentication)) {
      return true;
    }

    T user = getUser(authentication);

    List<Permission> permissions = resolvePermission(permission);

    if (targetDomainObject instanceof Collection<?>) {
      return checkObjectsPermissions(user, (Collection<?>) targetDomainObject, permissions);
    } else {
      return checkObjectsPermissions(
          user, Collections.singletonList(targetDomainObject), permissions);
    }
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Serializable targetId, String targetType, Object permission) {
    throw new UnsupportedOperationException();
  }

  protected Collection<? extends Permission> getAnonymousPermissions() {
    return Collections.emptyList();
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object requiredPermission) {
    if (isSuperUser(authentication)) {
      return true;
    }

    Collection<? extends Permission> userPermissions = getPermissions(authentication);

    List<Permission> requiredPermissions = resolvePermission(requiredPermission);

    for (Permission permission : requiredPermissions) {
      // Il faut posséder au moins une des permissions acceptées
      if (userPermissions.contains(permission)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Collection<? extends Permission> getPermissions(Authentication authentication) {
    Collection<? extends Permission> userPermissions = getAnonymousPermissions();
    if (authentication != null) {
      Object userDetailsCandidate = authentication.getPrincipal();
      if (userDetailsCandidate instanceof CoreUserDetails) {
        CoreUserDetails userDetails = (CoreUserDetails) userDetailsCandidate;
        userPermissions = userDetails.getPermissions();
      }
    }

    return userPermissions;
  }

  @Override
  public boolean isSuperUser(Authentication authentication) {
    if (authentication != null) {
      return securityService.hasSystemRole(authentication)
          || securityService.hasAdminRole(authentication);
    }
    return false;
  }

  protected boolean checkObjectsPermissions(
      T user, Collection<?> targetDomainObject, List<Permission> permissions) {
    for (Object object : targetDomainObject) {
      // il faut que tous les objets possèdent les permissions requises
      boolean allowed = checkAcceptablePermissions(user, object, permissions);
      if (!allowed) {
        return false;
      }
    }
    return true;
  }

  protected boolean checkAcceptablePermissions(
      T user, Object targetDomainObject, List<Permission> permissions) {
    // les doublons de permissions sont retirés dans getAcceptablePermissions
    List<Permission> acceptablePermissions =
        permissionHierarchy.getAcceptablePermissions(permissions);
    for (Permission permission : acceptablePermissions) {
      // Il faut posséder au moins une des permissions acceptées
      boolean allowed = hasPermission(user, targetDomainObject, permission);
      if (allowed) {
        return true;
      }
    }
    return false;
  }

  /** {@see org.springframework.security.acls.AclPermissionEvaluator#resolvePermission(Object)} */
  private List<Permission> resolvePermission(Object permission) {
    if (permission instanceof Permission) {
      return Arrays.asList((Permission) permission);
    } else if (permission instanceof Permission[]) {
      return Arrays.asList((Permission[]) permission);
    } else if (permission instanceof String) {
      String permString = (String) permission;
      String[] split = permString.split("\\|");
      List<Permission> result = new ArrayList<>();
      for (String perm : split) {
        Permission resolvedPermission = resolvePermissionByName(perm);
        if (!result.contains(resolvedPermission)) {
          result.add(resolvedPermission);
        }
      }
      return result;
    }
    throw new IllegalStateException("Unsupported permission: " + permission);
  }

  private Permission resolvePermissionByName(String permission) {
    Permission p;
    try {
      p = permissionFactory.buildFromName(permission);
    } catch (IllegalArgumentException notfound) {
      p = permissionFactory.buildFromName(permission.toUpperCase());
    }
    if (p == null) {
      throw new IllegalStateException("Unsupported permission: " + permission);
    }
    return p;
  }
}

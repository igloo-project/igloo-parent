package org.iglooproject.jpa.security.service;

import static java.util.Optional.ofNullable;

import igloo.security.UserDetails;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.security.business.user.model.IUser;
import org.iglooproject.jpa.security.business.user.service.ICoreUserSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;

public abstract class AbstractCorePermissionEvaluator<U extends GenericEntity<Long, U> & IUser>
    implements ICorePermissionEvaluator {

  public static final Logger LOGGER =
      LoggerFactory.getLogger(AbstractCorePermissionEvaluator.class);

  @Autowired private PermissionFactory permissionFactory;

  @Autowired private ICoreUserSecurityService<U> userService;

  @Autowired private ISecurityService securityService;

  /**
   * Vérifie qu'un utilisateur possède la permission souhaitée
   *
   * @param user peut être <code>null</code> dans le cas d'une authentification anonyme
   */
  protected abstract boolean hasPermission(
      U user, Object targetDomainObject, Permission permission);

  protected U getUser(Authentication authentication) {
    return ofNullable(AuthenticationUtil.getUsername())
        .map(userService::getByUsername)
        .orElse(null);
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

    U user = getUser(authentication);

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

  protected Collection<Permission> getPermissions(Authentication authentication) {
    if (authentication != null) {
      if (authentication.getPrincipal() instanceof UserDetails userDetails) {
        return userDetails.getPermissions();
      }
    }
    LOGGER.warn("No authentication principal found");
    return List.of();
  }

  @Override
  public boolean isSuperUser(Authentication authentication) {
    if (authentication != null) {
      return securityService.hasSystemRole(authentication);
    }
    return false;
  }

  protected boolean checkObjectsPermissions(
      U user, Collection<?> targetDomainObject, List<Permission> permissions) {
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
      U user, Object targetDomainObject, List<Permission> permissions) {
    return permissions.stream()
        .anyMatch(permission -> hasPermission(user, targetDomainObject, permission));
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

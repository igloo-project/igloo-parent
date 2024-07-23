package org.iglooproject.jpa.security.service;

import java.util.Collection;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;

public interface ICorePermissionEvaluator extends PermissionEvaluator {

  /**
   * Permet d'indiquer qu'un utilisateur doit avoir toutes les permissions et bypasser tous les
   * checks de permissions.
   */
  boolean isSuperUser(Authentication authentication);

  boolean hasPermission(Authentication authentication, Object requirePermission);

  Collection<? extends Permission> getPermissions(Authentication authentication);
}

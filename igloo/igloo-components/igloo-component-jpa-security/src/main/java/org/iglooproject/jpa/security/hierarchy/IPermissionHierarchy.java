package org.iglooproject.jpa.security.hierarchy;

import java.util.Collection;
import java.util.List;
import org.springframework.security.acls.model.Permission;

public interface IPermissionHierarchy {

  List<Permission> getAcceptablePermissions(Permission permission);

  List<Permission> getAcceptablePermissions(Collection<Permission> permissions);

  List<Permission> getReachablePermissions(Permission permission);

  List<Permission> getReachablePermissions(Collection<Permission> permissions);
}

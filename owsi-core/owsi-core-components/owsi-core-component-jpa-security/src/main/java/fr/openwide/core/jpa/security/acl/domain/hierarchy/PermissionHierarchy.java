package fr.openwide.core.jpa.security.acl.domain.hierarchy;

import java.util.Collection;
import java.util.List;

import org.springframework.security.acls.model.Permission;

public interface PermissionHierarchy {
	
	List<Permission> getAcceptablePermissions(Permission permission);

	List<Permission> getAcceptablePermissions(Collection<Permission> permissions);

}

package fr.openwide.core.jpa.security.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.business.person.model.AbstractPerson;
import fr.openwide.core.jpa.security.business.person.service.IPersonService;
import fr.openwide.core.jpa.security.hierarchy.IPermissionHierarchy;
import fr.openwide.core.jpa.security.model.NamedPermission;

public abstract class AbstractCorePermissionEvaluator<T extends AbstractPerson<T>> implements PermissionEvaluator {

	@Autowired
	private PermissionFactory permissionFactory;
	
	@Autowired
	private IPermissionHierarchy permissionHierarchy;
	
	@Autowired
	private IPersonService<T> personService;
	
	/**
	 * Vérifie qu'un utilisateur possède la permission souhaitée
	 * @param user peut être <code>null</code> dans le cas d'une authentification anonyme
	 */
	protected abstract boolean hasPermission(T user, Object targetDomainObject, Permission permission);
	
	protected T getUser(Authentication authentication) {
		if (authentication == null) {
			return null;
		}

		if (authentication.getPrincipal() instanceof UserDetails) {
			return personService.getByUserName(((UserDetails) authentication.getPrincipal()).getUsername());
		}

		return null;
	}
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if (isSuperUser(authentication)) {
			return true;
		}
		
		T user = getUser(authentication);
		
		List<Permission> permissions = resolvePermission(permission);
		for (Permission perm : permissions) {
			if (NamedPermission.ALLOWED.equals(perm)) {
				return true;
			} else if (NamedPermission.DENIED.equals(perm)) {
				return false;
			}
		}
		
		if (targetDomainObject instanceof Collection<?>) {
			return checkObjectsPermissions(user, (Collection<?>) targetDomainObject, permissions);
		} else {
			return checkObjectsPermissions(user, Collections.singletonList(targetDomainObject), permissions);
		}
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Permet d'indiquer qu'un utilisateur doit avoir toutes les permissions et bypasser tous les checks de permissions.
	 */
	public boolean isSuperUser(Authentication authentication) {
		if (authentication != null) {
			return authentication.getAuthorities().contains(CoreAuthorityConstants.AUTHORITY_SYSTEM)
					|| authentication.getAuthorities().contains(CoreAuthorityConstants.AUTHORITY_ADMIN);
		}
		return false;
	}

	protected boolean checkObjectsPermissions(T user, Collection<?> targetDomainObject, List<Permission> permissions) {
		for (Object object : targetDomainObject) {
			// il faut que tous les objets possèdent les permissions requises
			boolean allowed = checkAcceptablePermissions(user, object, permissions);
			if (! allowed) {
				return false;
			}
		}
		return true;
	}

	protected boolean checkAcceptablePermissions(T user, Object targetDomainObject, List<Permission> permissions) {
		// les doublons de permissions sont retirés dans getAcceptablePermissions
		List<Permission> acceptablePermissions = permissionHierarchy.getAcceptablePermissions(permissions);
		for (Permission permission : acceptablePermissions) {
			// Il faut posséder au moins une des permissions acceptées
			boolean allowed = hasPermission(user, targetDomainObject, permission);
			if (allowed) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@see org.springframework.security.acls.AclPermissionEvaluator#resolvePermission(Object)}
	 */
	private List<Permission> resolvePermission(Object permission) {
		if (permission instanceof Permission) {
			return Arrays.asList((Permission) permission);
		} else if (permission instanceof Permission[]) {
			return Arrays.asList((Permission[]) permission);
		} else if (permission instanceof String) {
			String permString = (String) permission;
			String[] split = permString.split("\\|");
			List<Permission> result = new ArrayList<Permission>();
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

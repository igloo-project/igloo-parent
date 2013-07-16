package fr.openwide.core.jpa.security.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.business.person.model.IPerson;
import fr.openwide.core.jpa.security.runas.RunAsSystemToken;
import fr.openwide.core.jpa.security.util.UserConstants;

public class CoreSecurityServiceImpl implements ISecurityService {

	public static final String SYSTEM_USER_NAME = "system";
	
	@Autowired
	protected UserDetailsService userDetailsService;

	@Autowired
	protected RunAsImplAuthenticationProvider runAsAuthenticationProvider;

	@Autowired
	protected ICorePermissionEvaluator permissionEvaluator;
	
	@Autowired
	protected RoleHierarchy roleHierarchy;

	@Override
	public boolean hasRole(Authentication authentication, String role) {
		if (authentication != null && role != null) {
			return authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
		}
		return false;
	}

	@Override
	public boolean hasRole(IPerson person, String role) {
		if (person == null) {
			return false;
		}

		return hasRole(getAuthentication(person), role);
	}

	@Override
	public boolean hasSystemRole(Authentication authentication) {
		return hasRole(authentication, CoreAuthorityConstants.ROLE_SYSTEM);
	}

	@Override
	public boolean hasSystemRole(IPerson person) {
		return hasRole(person, CoreAuthorityConstants.ROLE_SYSTEM);
	}

	@Override
	public boolean hasAdminRole(Authentication authentication) {
		return hasRole(authentication, CoreAuthorityConstants.ROLE_ADMIN);
	}

	@Override
	public boolean hasAdminRole(IPerson person) {
		return hasRole(person, CoreAuthorityConstants.ROLE_ADMIN);
	}

	@Override
	public boolean hasAuthenticatedRole(Authentication authentication) {
		return hasRole(authentication, CoreAuthorityConstants.ROLE_AUTHENTICATED);
	}

	@Override
	public boolean hasAuthenticatedRole(IPerson person) {
		return hasRole(person, CoreAuthorityConstants.ROLE_AUTHENTICATED);
	}

	@Override
	public boolean isAnonymousAuthority(String grantedAuthoritySid) {
		return CoreAuthorityConstants.ROLE_ANONYMOUS.equals(grantedAuthoritySid);
	}

	@Override
	public List<GrantedAuthority> getAuthorities(Authentication authentication) {
		if (authentication != null) {
			return new ArrayList<GrantedAuthority>(authentication.getAuthorities());
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<GrantedAuthority> getAuthorities(IPerson person) {
		return getAuthorities(getAuthentication(person));
	}

	@Override
	public SecurityContext buildSecureContext(String userName) {
		SecurityContext secureContext = new SecurityContextImpl();
		secureContext.setAuthentication(getAuthentication(userName));

		return secureContext;
	}
	
	@SuppressWarnings("unused")
	private void authenticateAs(IPerson user) {
		authenticateAs(user.getUserName());
	}

	private void authenticateAs(String userName, String... additionalAuthorities) {
		clearAuthentication();

		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

		Set<GrantedAuthority> authorities = Sets.newHashSet(userDetails.getAuthorities());
		if (additionalAuthorities != null) {
			for (String additionalAuthority : additionalAuthorities) {
				authorities.add(new SimpleGrantedAuthority(additionalAuthority));
			}
		}

		Authentication authentication = new RunAsUserToken(runAsAuthenticationProvider.getKey(), userDetails,
				UserConstants.NO_CREDENTIALS, authorities, UsernamePasswordAuthenticationToken.class);

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void authenticateAsSystem() {
		RunAsSystemToken runAsSystem = new RunAsSystemToken(runAsAuthenticationProvider.getKey(),
				UserConstants.SYSTEM_USER_NAME,
				roleHierarchy.getReachableGrantedAuthorities(Lists.newArrayList(new SimpleGrantedAuthority(CoreAuthorityConstants.ROLE_SYSTEM))));
		AuthenticationUtil.setAuthentication(runAsAuthenticationProvider.authenticate(runAsSystem));
	}

	@Override
	public void clearAuthentication() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	/**
	 * Exécute une {@link Callable} en tant qu'utilisateur système. Le
	 * contexte de sécurité est modifié au début de la tâche et rétabli à la fin
	 * de la tâche.
	 * 
	 * @param task
	 *            un objet de type {@link Callable}
	 * 
	 * @return l'objet retourné par la méthode {@link Callable#call()}
	 */
	@Override
	public <T> T runAsSystem(Callable<T> task) {
		Authentication originalAuthentication = AuthenticationUtil.getAuthentication();
		authenticateAsSystem();
		try {
			return task.call();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			AuthenticationUtil.setAuthentication(originalAuthentication);
		}
	}
	

	protected Authentication getAuthentication(IPerson person) {
		return getAuthentication(person.getUserName());
	}

	protected Authentication getAuthentication(String userName) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

		Authentication authentication = new RunAsUserToken(runAsAuthenticationProvider.getKey(), userDetails,
				"no-credentials", userDetails.getAuthorities(), UsernamePasswordAuthenticationToken.class);
		
		return authentication;
	}

	@Override
	public boolean hasPermission(Authentication authentication, GenericEntity<?, ?> securedObject,
			Permission requirePermission) {
		return permissionEvaluator.hasPermission(authentication, securedObject, requirePermission);
	}

	@Override
	public boolean hasPermission(IPerson person, GenericEntity<?, ?> securedObject, Permission requirePermission) {
		return hasPermission(getAuthentication(person), securedObject, requirePermission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Permission permission) {
		if (permissionEvaluator.isSuperUser(authentication)) {
			return true;
		}
		
		List<Permission> permissions = getPermissions(authentication);
		if (permissions.contains(permission)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean hasPermission(IPerson person, Permission permission) {
		return hasPermission(getAuthentication(person), permission);
	}
	
	@Override
	public List<Permission> getPermissions(Authentication authentication) {
		return Lists.newArrayListWithCapacity(0);
	}
	
	@Override
	public List<Permission> getPermissions(IPerson person) {
		return getPermissions(getAuthentication(person));
	}
	
}
package fr.openwide.core.jpa.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.google.common.collect.Lists;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.security.acl.domain.UserConstants;
import fr.openwide.core.jpa.security.acl.service.ISidRetrievalService;
import fr.openwide.core.jpa.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.jpa.security.business.person.model.IPerson;
import fr.openwide.core.jpa.security.runas.RunAsSystemToken;

public class CoreSecurityServiceImpl implements ISecurityService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreSecurityServiceImpl.class);

	@Autowired
	protected AclService aclService;
	
	@Autowired
	protected ISidRetrievalService sidRetrievalService;
	
	@Autowired
	protected UserDetailsService userDetailsService;
	
	@Autowired
	protected RunAsImplAuthenticationProvider runAsAuthenticationProvider;
	
	@Override
	public boolean hasPermission(Authentication authentication, Permission permission) {
		if (hasAllPermissions(authentication)) {
			return true;
		}
		
		List<Permission> permissions = getPermissions(authentication);
		if (permissions.contains(permission)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Permet d'indiquer qu'un utilisateur doit avoir toutes les permissions
	 */
	protected boolean hasAllPermissions(Authentication authentication) {
		return hasSystemRole(authentication) || hasAdminRole(authentication);
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
	
	@Override
	public boolean hasPermission(Authentication authentication, GenericEntity<?, ?> entity,
			Permission permission) {
		if (hasAllPermissions(authentication, entity)) {
			return true;
		}
		
		// Obtain the OID applicable to the domain object
		ObjectIdentity objectIdentity = new ObjectIdentityImpl(entity);

		// Obtain the SIDs applicable to the principal
		List<Sid> sids = sidRetrievalService.getSids(authentication);
		
		List<Permission> permissions = new ArrayList<Permission>();
		permissions.add(permission);

		try {
			// Lookup only ACLs for SIDs we're interested in
			Acl acl = aclService.readAclById(objectIdentity, sids);

			return acl.isGranted(permissions, sids, false);
		} catch (NotFoundException e) {
			LOGGER.debug("ACL not found for object identity: " + objectIdentity, e);
			return false;
		}
	}
	
	/**
	 * Permet d'indiquer qu'un utilisateur doit avoir toutes les permissions sur un objet
	 */
	protected boolean hasAllPermissions(Authentication authentication, GenericEntity<?, ?> entity) {
		return hasAllPermissions(authentication);
	}

	@Override
	public boolean hasPermission(IPerson person, GenericEntity<?, ?> securedObject,
			Permission permission) {
		return hasPermission(getAuthentication(person), securedObject, permission);
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
	public boolean hasRole(Authentication authentication, String role) {
		if(authentication != null && role != null) {
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			// Attempt to find a matching granted authority
			for (GrantedAuthority authority : authorities) {
				if (role.equals(authority.getAuthority())) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean hasRole(IPerson person, String role) {
		return hasRole(getAuthentication(person), role);
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
	
	/**
	 * Procède à une authentification en tant qu'utilisateur système.
	 * L'utilisateur système possède tous les droits.
	 * Cette méthode remplace le contexte de sécurité courant.
	 */
	private void authenticateAsSystem() {
		RunAsSystemToken runAsSystem = new RunAsSystemToken(runAsAuthenticationProvider.getKey(),
				UserConstants.SYSTEM_USER_NAME, UserConstants.SYSTEM_USER_NAME);
		AuthenticationUtil.setAuthentication(runAsAuthenticationProvider.authenticate(runAsSystem));
	}
	
	/**
	 * Exécute une {@link IRunAsTask} en tant qu'utilisateur système. Le contexte
	 * de sécurité est modifié au début de la tâche et rétabli à la fin de la
	 * tâche.
	 *
	 * @param task un objet de type {@link IRunAsTask}
	 * 
	 * @return l'objet retourné par la méthode {@link IRunAsTask#execute()}
	 * @throws ServiceException 
	 */
	@Override
	public <T> T runAsSystem(Callable<T> task) throws ServiceException {
		Authentication originalAuthentication = AuthenticationUtil.getAuthentication();
		authenticateAsSystem();
		try {
			return task.call();
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			AuthenticationUtil.setAuthentication(originalAuthentication);
		}
	}

	protected Authentication getAuthentication(IPerson person) {
		return getAuthentication(person.getUserName());
	}
	
	protected Authentication getAuthentication(String userName) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		
		Authentication authentication = new RunAsUserToken(
				runAsAuthenticationProvider.getKey(), userDetails,
				"no-credentials", userDetails.getAuthorities(),
				UsernamePasswordAuthenticationToken.class);

		return authentication;
	}

}
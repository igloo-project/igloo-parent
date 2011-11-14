package fr.openwide.core.hibernate.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

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

import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.security.acl.domain.UserConstants;
import fr.openwide.core.hibernate.security.acl.service.SidRetrievalService;
import fr.openwide.core.hibernate.security.business.authority.util.CoreAuthorityConstants;
import fr.openwide.core.hibernate.security.business.person.model.Person;
import fr.openwide.core.hibernate.security.runas.RunAsSystemToken;
import fr.openwide.core.hibernate.security.runas.RunAsTask;

public class CoreSecurityServiceImpl implements SecurityService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoreSecurityServiceImpl.class);

	@Autowired
	private AclService aclService;
	
	@Autowired
	private SidRetrievalService sidRetrievalService;
	
	@Resource(name = "hibernateUserDetailsService")
	private UserDetailsService userDetailsService;
	
	@Autowired
	private RunAsImplAuthenticationProvider runAsAuthenticationProvider;
	
	@Override
	public boolean hasPermission(Authentication authentication, GenericEntity<?, ?> entity,
			Permission permission) {
		if (hasSystemRole(authentication) || hasAdminRole(authentication)) {
			return true;
		}
		
		// Obtain the OID applicable to the domain object
		ObjectIdentity objectIdentity = new ObjectIdentityImpl(entity);

		// Obtain the SIDs applicable to the principal
		List<Sid> sids = sidRetrievalService.getSids(authentication);
		
		List<Permission> permissions = new ArrayList<Permission>();
		permissions.add(permission);

		Acl acl = null;

		try {
			// Lookup only ACLs for SIDs we're interested in
			acl = aclService.readAclById(objectIdentity, sids);

			return acl.isGranted(permissions, sids, false);
		} catch (NotFoundException e) {
			LOGGER.debug("ACL not found for object identity: " + objectIdentity, e);
			return false;
		}
	}

	@Override
	public boolean hasPermission(Person person, GenericEntity<?, ?> securedObject,
			Permission permission) {
		return hasPermission(getAuthentication(person), securedObject, permission);
	}
	
	@Override
	public boolean hasSystemRole(Authentication authentication) {
		return hasRole(authentication, CoreAuthorityConstants.ROLE_SYSTEM);
	}
	
	@Override
	public boolean hasSystemRole(Person person) {
		return hasRole(person, CoreAuthorityConstants.ROLE_SYSTEM);
	}
	
	@Override
	public boolean hasAdminRole(Authentication authentication) {
		return hasRole(authentication, CoreAuthorityConstants.ROLE_ADMIN);
	}
	
	@Override
	public boolean hasAdminRole(Person person) {
		return hasRole(person, CoreAuthorityConstants.ROLE_ADMIN);
	}
	
	@Override
	public boolean hasAuthenticatedRole(Authentication authentication) {
		return hasRole(authentication, CoreAuthorityConstants.ROLE_AUTHENTICATED);
	}
	
	@Override
	public boolean hasAuthenticatedRole(Person person) {
		return hasRole(person, CoreAuthorityConstants.ROLE_AUTHENTICATED);
	}
	
	@Override
	public boolean hasRole(Authentication authentication, String role) {
		if(authentication != null && role != null) {
			Collection<GrantedAuthority> authorities = authentication.getAuthorities();
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
	public boolean hasRole(Person person, String role) {
		return hasRole(getAuthentication(person), role);
	}
	
	@Override
	public boolean isAnonymousAuthority(String grantedAuthoritySid) {
		return CoreAuthorityConstants.ROLE_ANONYMOUS.equals(grantedAuthoritySid);
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
	 * Exécute une {@link RunAsTask} en tant qu'utilisateur système. Le contexte
	 * de sécurité est modifié au début de la tâche et rétabli à la fin de la
	 * tâche.
	 *
	 * @param task un objet de type {@link RunAsTask}
	 * 
	 * @return l'objet retourné par la méthode {@link RunAsTask#execute()}
	 */
	@Override
	public <T> T runAsSystem(RunAsTask<T> task) {
		Authentication originalAuthentication = AuthenticationUtil.getAuthentication();
		authenticateAsSystem();
		try {
			return task.execute();
		} finally {
			AuthenticationUtil.setAuthentication(originalAuthentication);
		}
	}

	protected Authentication getAuthentication(Person person) {
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
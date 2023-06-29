package igloo.security;

import java.util.Collection;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.AuthenticatedPrincipal;

/**
 * This interface is added so that we can implement similar contract both for OidcUser (spring-oauth2) and UserDetails
 * (spring-security-core, UsernamePassword token). It also adds {@link #getPermissions()} method that is not part of
 * core spring-security features.
 */
public interface UserDetails extends org.springframework.security.core.userdetails.UserDetails, AuthenticatedPrincipal {

	public Collection<Permission> getPermissions();

}

package fr.openwide.core.jpa.security.service;

import java.util.Collection;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import fr.openwide.core.jpa.security.model.CoreUserDetails;

public final class AuthenticationUtil {

	static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	static void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	static boolean isLoggedIn() {
		return (getUserName() != null);
	}

	static String getUserName() {
		String userName = null;

		Authentication authentication = getAuthentication();
		if (authentication != null && (authentication.getPrincipal() instanceof UserDetails)) {
			UserDetails details = (UserDetails) authentication.getPrincipal();
			userName = details.getUsername();
		}

		return userName;
	}

	static Collection<? extends GrantedAuthority> getAuthorities() {
		return getAuthentication().getAuthorities();
	}
	
	public static Collection<? extends Permission> getPermissions() {
		Authentication authentication = getAuthentication();
		
		Object userDetailsCandidate = authentication.getPrincipal();
		if (!(userDetailsCandidate instanceof CoreUserDetails)) {
			throw new IllegalStateException("UserDetails should be a CoreUserDetails");
		}
		CoreUserDetails userDetails = (CoreUserDetails) userDetailsCandidate;
		
		return userDetails.getPermissions();
	}

	private AuthenticationUtil() {
	}

}
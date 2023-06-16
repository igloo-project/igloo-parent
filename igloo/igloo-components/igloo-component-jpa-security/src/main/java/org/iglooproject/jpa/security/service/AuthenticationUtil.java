package org.iglooproject.jpa.security.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import igloo.security.UserDetails;

final class AuthenticationUtil {

	static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	static void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	static boolean isLoggedIn() {
		return (getUsername() != null);
	}

	static String getUsername() {
		return Optional.ofNullable(getAuthentication()).map(AuthenticationUtil::getUserDetails).map(UserDetails::getUsername).orElse(null);
	}

	static Collection<Permission> getPermissions() {
		return getPermissions(getAuthentication());
	}

	static Collection<Permission> getPermissions(Authentication authentication) {
		return Optional.ofNullable(authentication)
				.map(AuthenticationUtil::getUserDetails)
				.map(UserDetails::getPermissions)
				.orElseGet(Collections::emptySet);
	}

	public static UserDetails getUserDetails() {
		return getUserDetails(getAuthentication());
	}

	public static UserDetails getUserDetails(Authentication authentication) {
		return Arrays.stream(new Object[] {
				authentication.getPrincipal(),
				authentication.getDetails()
		})
			.filter(Objects::nonNull)
			.filter(igloo.security.UserDetails.class::isInstance)
			.map(UserDetails.class::cast)
			.findFirst().orElse(null);
	}
	
	static Collection<? extends GrantedAuthority> getAuthorities() {
		return getAuthorities(getAuthentication());
	}

	static Collection<GrantedAuthority> getAuthorities(Authentication authentication) {
		return Optional.ofNullable(authentication)
				.map(Authentication::getAuthorities)
				.map(List::<GrantedAuthority>copyOf)
				.orElseGet(Collections::emptyList);
	}

	private AuthenticationUtil() {
	}

}
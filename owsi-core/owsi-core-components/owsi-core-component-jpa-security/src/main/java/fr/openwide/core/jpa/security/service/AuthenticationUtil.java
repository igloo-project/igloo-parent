package fr.openwide.core.jpa.security.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Lists;

final class AuthenticationUtil {

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

	static List<? extends GrantedAuthority> getAuthorities() {
		return Lists.newArrayList(getAuthentication().getAuthorities());
	}

	private AuthenticationUtil() {
	}

}
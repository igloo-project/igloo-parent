package org.iglooproject.jpa.security.service;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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
    Authentication authentication = getAuthentication();
    if (authentication != null) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof String) {
        return (String) principal;
      } else if (principal instanceof UserDetails) {
        return ((UserDetails) principal).getUsername();
      }
    }
    return null;
  }

  static Collection<? extends GrantedAuthority> getAuthorities() {
    Authentication authentication = getAuthentication();
    return authentication == null
        ? ImmutableSet.<GrantedAuthority>of()
        : authentication.getAuthorities();
  }

  private AuthenticationUtil() {}
}

package org.iglooproject.jpa.security.runas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.access.intercept.RunAsUserToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CoreRunAsManagerImpl extends RunAsManagerImpl {

  public static final String RUN_AS_PREFIX = "RUN_AS_";

  @Autowired private RoleHierarchy roleHierarchy;

  @Override
  public Authentication buildRunAs(
      Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
    List<GrantedAuthority> newAuthorities = new ArrayList<>();

    for (ConfigAttribute attribute : attributes) {
      if (this.supports(attribute)) {
        List<GrantedAuthority> extraAuthorities = new ArrayList<>();
        extraAuthorities.add(
            new SimpleGrantedAuthority(
                getRolePrefix() + attribute.getAttribute().replaceFirst(RUN_AS_PREFIX, "")));
        newAuthorities.addAll(roleHierarchy.getReachableGrantedAuthorities(extraAuthorities));
      }
    }

    if (newAuthorities.size() == 0) {
      return null;
    } else {
      newAuthorities.addAll(authentication.getAuthorities());

      return new RunAsUserToken(
          this.getKey(),
          authentication.getPrincipal(),
          authentication.getCredentials(),
          newAuthorities,
          authentication.getClass());
    }
  }
}

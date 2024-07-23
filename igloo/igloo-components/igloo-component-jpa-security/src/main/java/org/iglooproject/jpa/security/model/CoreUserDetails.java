package org.iglooproject.jpa.security.model;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.Set;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CoreUserDetails extends User {

  private static final long serialVersionUID = -3194785312438750915L;

  private Set<Permission> permissions;

  public CoreUserDetails(
      String username,
      String password,
      boolean enabled,
      boolean accountNonExpired,
      boolean credentialsNonExpired,
      boolean accountNonLocked,
      Collection<? extends GrantedAuthority> authorities,
      Collection<? extends Permission> permissions) {
    super(
        username,
        password,
        enabled,
        accountNonExpired,
        credentialsNonExpired,
        accountNonLocked,
        authorities);
    this.permissions = ImmutableSet.copyOf(permissions);
  }

  public CoreUserDetails(
      String username,
      String password,
      Collection<? extends GrantedAuthority> authorities,
      Collection<? extends Permission> permissions) {
    super(username, password, authorities);
    this.permissions = ImmutableSet.copyOf(permissions);
  }

  public Collection<Permission> getPermissions() {
    return permissions;
  }
}

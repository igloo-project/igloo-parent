package igloo.openidc;

import igloo.security.UserDetails;
import java.util.Collection;
import java.util.Set;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

/**
 * This is an alternative {@link org.springframework.security.oauth2.core.oidc.user.OidcUser}
 * implementation that provides {@link UserDetails} implementation.
 *
 * <p>It allows to use {@link UserDetails} as a common interface for both openidc and standard
 * login.
 */
public class OidcUserDetails extends DefaultOidcUser
    implements org.springframework.security.oauth2.core.oidc.user.OidcUser, UserDetails {

  private static final long serialVersionUID = -2455798932693104211L;

  /**
   * This attribute is needed when lookup is not exact (case insensitive) as we need to keep local
   * username reference.
   */
  private final String localUsername;

  private final Set<Permission> permissions;

  public OidcUserDetails(
      String localUsername,
      Collection<? extends GrantedAuthority> authorities,
      Set<Permission> permissions,
      OidcIdToken idToken,
      OidcUserInfo userInfo,
      String nameAttributeKey) {
    super(authorities, idToken, userInfo, nameAttributeKey);
    this.localUsername = localUsername;
    this.permissions = Set.copyOf(permissions);
  }

  public OidcUserDetails(
      String localUsername,
      Collection<? extends GrantedAuthority> authorities,
      Set<Permission> permissions,
      OidcIdToken idToken,
      OidcUserInfo userInfo) {
    super(authorities, idToken, userInfo);
    this.localUsername = localUsername;
    this.permissions = Set.copyOf(permissions);
  }

  public OidcUserDetails(
      String localUsername,
      Collection<? extends GrantedAuthority> authorities,
      Set<Permission> permissions,
      OidcIdToken idToken,
      String nameAttributeKey) {
    super(authorities, idToken, nameAttributeKey);
    this.localUsername = localUsername;
    this.permissions = Set.copyOf(permissions);
  }

  public OidcUserDetails(
      String localUsername,
      Collection<? extends GrantedAuthority> authorities,
      Set<Permission> permissions,
      OidcIdToken idToken) {
    super(authorities, idToken);
    this.localUsername = localUsername;
    this.permissions = Set.copyOf(permissions);
  }

  @Override
  public String getPassword() {
    return null;
  }

  /** This username is the local application username. It may differ from the remote username. */
  @Override
  public String getUsername() {
    return this.localUsername;
  }

  /** This username is the remote system username. It may differ from the local username. */
  @Override
  public String getRemoteUsername() {
    return getPreferredUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<Permission> getPermissions() {
    return permissions;
  }
}

package igloo.openidc;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import igloo.security.UserDetails;

/**
 * This is an alternative {@link org.springframework.security.oauth2.core.oidc.user.OidcUser} implementation that
 * provides {@link UserDetails} implementation.
 * 
 * It allows to use {@link UserDetails} as a common interface for both openidc and standard login.
 */
public class OidcUserDetails extends DefaultOidcUser implements org.springframework.security.oauth2.core.oidc.user.OidcUser, UserDetails {

	private static final long serialVersionUID = -2455798932693104211L;

	private final Set<Permission> permissions;

	public OidcUserDetails(Collection<? extends GrantedAuthority> authorities, Set<Permission> permissions,
			OidcIdToken idToken, OidcUserInfo userInfo, String nameAttributeKey) {
		super(authorities, idToken, userInfo, nameAttributeKey);
		this.permissions = Set.copyOf(permissions);
	}

	public OidcUserDetails(Collection<? extends GrantedAuthority> authorities, Set<Permission> permissions,
			OidcIdToken idToken, OidcUserInfo userInfo) {
		super(authorities, idToken, userInfo);
		this.permissions = Set.copyOf(permissions);
	}

	public OidcUserDetails(Collection<? extends GrantedAuthority> authorities, Set<Permission> permissions,
			OidcIdToken idToken, String nameAttributeKey) {
		super(authorities, idToken, nameAttributeKey);
		this.permissions = Set.copyOf(permissions);
	}

	public OidcUserDetails(Collection<? extends GrantedAuthority> authorities, Set<Permission> permissions,
			OidcIdToken idToken) {
		super(authorities, idToken);
		this.permissions = Set.copyOf(permissions);
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
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

package igloo.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CoreUserDetails extends User implements UserDetails {

	private static final long serialVersionUID = -3194785312438750915L;
	
	private Set<Permission> permissions;

	public CoreUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			Collection<? extends Permission> permissions) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.permissions = Set.copyOf(permissions);
	}

	public CoreUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
			Collection<? extends Permission> permissions) {
		super(username, password, authorities);
		this.permissions = Set.copyOf(permissions);
	}

	@Override
	public Collection<Permission> getPermissions() {
		return permissions;
	}

	@Override
	public String getName() {
		return getUsername();
	}

}

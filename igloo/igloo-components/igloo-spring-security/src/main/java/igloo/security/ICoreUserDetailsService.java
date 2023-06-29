package igloo.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Specialization of spring {@link UserDetailsService} that supplies Igloo {@link UserDetails}.
 */
public interface ICoreUserDetailsService extends UserDetailsService {

	@Override
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}

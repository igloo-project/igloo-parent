package org.iglooproject.jpa.security.business.authority.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CoreAuthorityConstants {

	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	public static final String ROLE_AUTHENTICATED = "ROLE_AUTHENTICATED";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";

	/**
	 * used for internal calls
	 */
	public static final String ROLE_SYSTEM = "ROLE_SYSTEM";
	public static final String RUN_AS_SYSTEM = "RUN_AS_SYSTEM";

	public static final GrantedAuthority AUTHORITY_SYSTEM = new SimpleGrantedAuthority(ROLE_SYSTEM);
	public static final GrantedAuthority AUTHORITY_ADMIN = new SimpleGrantedAuthority(ROLE_ADMIN);

	protected CoreAuthorityConstants() {
	}

}

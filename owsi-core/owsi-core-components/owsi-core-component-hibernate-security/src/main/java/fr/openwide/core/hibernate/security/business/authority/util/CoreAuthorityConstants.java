package fr.openwide.core.hibernate.security.business.authority.util;

public final class CoreAuthorityConstants {

	public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
	public static final String ROLE_AUTHENTICATED = "ROLE_AUTHENTICATED";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	
	/**
	 * used for internal calls
	 */
	public static final String ROLE_SYSTEM = "ROLE_SYSTEM";
	public static final String RUN_AS_SYSTEM = "RUN_AS_SYSTEM";
	
	/**
	 * prefix used to make the difference between real authorities and groups
	 */
	public static final String PERSON_GROUP_PREFIX = "group:";

	private CoreAuthorityConstants() {
	}
	
}
package fr.openwide.core.spring.property;

import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class SpringSecurityPropertyIds {

	/*
	 * Mutable Properties
	 */
	
	
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<String> PASSWORD_SALT = new ImmutablePropertyId<>("security.passwordSalt");
	public static final ImmutablePropertyId<Integer> PASSWORD_EXPIRATION_DAYS = new ImmutablePropertyId<>("security.password.expiration.days");
	public static final ImmutablePropertyId<Integer> PASSWORD_HISTORY_COUNT = new ImmutablePropertyId<>("security.password.history.count");
	public static final ImmutablePropertyId<Integer> PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT = new ImmutablePropertyId<>("security.password.recovery.request.token.random.count");
	public static final ImmutablePropertyId<Integer> PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES = new ImmutablePropertyId<>("security.password.recovery.request.expiration.minutes");

}

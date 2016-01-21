package fr.openwide.core.spring.property;

import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class SpringSecurityPropertyIds extends AbstractPropertyIds {
	
	private SpringSecurityPropertyIds() {
		
	}

	/*
	 * Mutable Properties
	 */
	
	// None
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<String> PASSWORD_SALT = immutable("security.passwordSalt");
	public static final ImmutablePropertyId<Integer> PASSWORD_EXPIRATION_DAYS = immutable("security.password.expiration.days");
	public static final ImmutablePropertyId<Integer> PASSWORD_HISTORY_COUNT = immutable("security.password.history.count");
	public static final ImmutablePropertyId<Integer> PASSWORD_RECOVERY_REQUEST_TOKEN_RANDOM_COUNT = immutable("security.password.recovery.request.token.random.count");
	public static final ImmutablePropertyId<Integer> PASSWORD_RECOVERY_REQUEST_EXPIRATION_MINUTES = immutable("security.password.recovery.request.expiration.minutes");

}

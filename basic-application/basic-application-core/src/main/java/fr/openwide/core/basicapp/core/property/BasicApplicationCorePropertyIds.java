package fr.openwide.core.basicapp.core.property;

import java.util.List;

import fr.openwide.core.basicapp.core.config.util.Environment;
import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class BasicApplicationCorePropertyIds extends AbstractPropertyIds {
	
	/*
	 * Mutable Properties
	 */
	
	// None
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<Environment> ENVIRONMENT = immutable("environment");
	
	public static final ImmutablePropertyId<Boolean> SECURITY_PASSWORD_VALIDATOR_ENABLED = immutable("security.password.validator.enabled");
	public static final ImmutablePropertyId<List<String>> SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS = immutable("security.password.user.forbiddenPasswords");

}

package fr.openwide.core.basicapp.core.property;

import java.util.List;

import fr.openwide.core.basicapp.core.config.util.Environment;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;

public final class BasicApplicationCorePropertyIds {
	
	/*
	 * Mutable Properties
	 */
	
	
	
	/*
	 * Immutable Properties
	 */
	
	public static final ImmutablePropertyId<Environment> ENVIRONMENT = new ImmutablePropertyId<Environment>("environment");
	
	public static final ImmutablePropertyId<Boolean> SECURITY_PASSWORD_VALIDATOR_ENABLED = new ImmutablePropertyId<>("security.password.validator.enabled");
	public static final ImmutablePropertyId<List<String>> SECURITY_PASSWORD_USER_FORBIDDEN_PASSWORDS = new ImmutablePropertyId<>("security.password.user.forbiddenPasswords");

}

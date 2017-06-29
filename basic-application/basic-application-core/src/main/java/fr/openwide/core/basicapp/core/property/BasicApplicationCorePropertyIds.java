package fr.openwide.core.basicapp.core.property;

import static fr.openwide.core.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_PREFIX;
import static fr.openwide.core.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_SUFFIX;

import java.util.List;

import fr.openwide.core.basicapp.core.config.util.Environment;
import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;
import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyIdTemplate;

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

	/*
	 * DateUpgrade autoperform
	 */
	public static final MutablePropertyIdTemplate<Boolean> DATA_UPGRADE_AUTOPERFORM_TEMPLATE =
			mutableTemplate(DATA_UPGRADE_AUTOPERFOM_PREFIX + ".%1s." + DATA_UPGRADE_AUTOPERFOM_SUFFIX);
	public static final MutablePropertyId<Boolean> dataUpgradeAutoperfom(IDataUpgrade dataUpgrade) {
		return DATA_UPGRADE_AUTOPERFORM_TEMPLATE.create(dataUpgrade.getName());
	}
}

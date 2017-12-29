package org.iglooproject.basicapp.core.property;

import static org.iglooproject.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_PREFIX;
import static org.iglooproject.basicapp.core.business.upgrade.util.DataUpgradeConstants.DATA_UPGRADE_AUTOPERFOM_SUFFIX;

import java.util.List;

import org.iglooproject.basicapp.core.config.util.Environment;
import org.iglooproject.jpa.more.business.upgrade.model.IDataUpgrade;
import org.iglooproject.spring.property.model.AbstractPropertyIds;
import org.iglooproject.spring.property.model.ImmutablePropertyId;
import org.iglooproject.spring.property.model.MutablePropertyId;
import org.iglooproject.spring.property.model.MutablePropertyIdTemplate;

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

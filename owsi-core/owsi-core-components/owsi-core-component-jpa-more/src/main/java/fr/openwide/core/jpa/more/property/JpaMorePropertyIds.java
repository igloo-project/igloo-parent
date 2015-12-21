package fr.openwide.core.jpa.more.property;

import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;
import fr.openwide.core.spring.property.model.MutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyIdTemplate;

public class JpaMorePropertyIds {

	public static final MutablePropertyId<Boolean> DATABASE_INITIALIZED = new MutablePropertyId<>("databaseInitialized");
	public static final MutablePropertyIdTemplate<Boolean> DATA_UPGRADE_DONE_TEMPLATE = new MutablePropertyIdTemplate<>("dataUpgrade.%1s");
	public static final MutablePropertyId<Boolean> dataUpgrade(IDataUpgrade dataUpgrade) {
		return DATA_UPGRADE_DONE_TEMPLATE.create(dataUpgrade.getName());
	}
	public static final MutablePropertyId<Boolean> MAINTENANCE = new MutablePropertyId<>("maintenance");

}

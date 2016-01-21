package fr.openwide.core.jpa.more.property;

import java.io.File;

import fr.openwide.core.jpa.more.business.upgrade.model.IDataUpgrade;
import fr.openwide.core.spring.property.model.AbstractPropertyIds;
import fr.openwide.core.spring.property.model.ImmutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyId;
import fr.openwide.core.spring.property.model.MutablePropertyIdTemplate;

public class JpaMorePropertyIds extends AbstractPropertyIds {
	
	private JpaMorePropertyIds() {
	}

	public static final MutablePropertyId<Boolean> DATABASE_INITIALIZED = mutable("databaseInitialized");
	public static final MutablePropertyIdTemplate<Boolean> DATA_UPGRADE_DONE_TEMPLATE = mutableTemplate("dataUpgrade.%1s");
	public static final MutablePropertyId<Boolean> dataUpgrade(IDataUpgrade dataUpgrade) {
		return DATA_UPGRADE_DONE_TEMPLATE.create(dataUpgrade.getName());
	}
	public static final MutablePropertyId<Boolean> MAINTENANCE = mutable("maintenance");
	
	public static final ImmutablePropertyId<Boolean> MIGRATION_LOGGING_MEMORY = immutable("migration.logging.memory");
	
	public static final ImmutablePropertyId<File> IMAGE_MAGICK_CONVERT_BINARY_PATH = immutable("imageMagick.convertBinary.path");

}

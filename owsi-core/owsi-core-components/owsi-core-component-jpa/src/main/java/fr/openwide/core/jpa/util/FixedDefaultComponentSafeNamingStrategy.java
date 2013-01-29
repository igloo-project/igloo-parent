package fr.openwide.core.jpa.util;

import org.hibernate.cfg.DefaultComponentSafeNamingStrategy;
import org.hibernate.cfg.NamingStrategy;

/**
 * Fixes bug <a href="https://hibernate.onjira.com/browse/HHH-6005">HHH-6005</a> in {@link DefaultComponentSafeNamingStrategy}.
 */
public class FixedDefaultComponentSafeNamingStrategy extends DefaultComponentSafeNamingStrategy {
	public static final NamingStrategy INSTANCE = new FixedDefaultComponentSafeNamingStrategy();

	private static final long serialVersionUID = -2382165043375385406L;
	
	protected static String fixPropertyName(String propertyName) {
		return propertyName == null ? null : propertyName.replace(".collection&&element.", ".");
	}

	@Override
	public String propertyToColumnName(String propertyName) {
		return super.propertyToColumnName(fixPropertyName(propertyName));
	}
	
	@Override
	public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity,
			String associatedEntityTable, String propertyName) {
		return super.collectionTableName(ownerEntity, ownerEntityTable, associatedEntity, associatedEntityTable,
				fixPropertyName(propertyName));
	}
	
	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName,
			String referencedColumnName) {
		return super.foreignKeyColumnName(fixPropertyName(propertyName),
				propertyEntityName, propertyTableName, referencedColumnName);
	}

}

package org.iglooproject.jpa.business.generic.model.migration;

import java.io.Serializable;

import org.iglooproject.jpa.business.generic.model.IPredefinedIdEntity;

public interface IMigratedFromOldApplicationEntity<K extends Serializable & Comparable<K>> extends IPredefinedIdEntity<K> {

	boolean isMigrated();
	
	void setMigrated(boolean migrated);

}
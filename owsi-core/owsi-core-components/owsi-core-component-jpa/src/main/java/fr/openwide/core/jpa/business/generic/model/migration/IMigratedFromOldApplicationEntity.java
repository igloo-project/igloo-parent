package fr.openwide.core.jpa.business.generic.model.migration;

import java.io.Serializable;

public interface IMigratedFromOldApplicationEntity<K extends Serializable & Comparable<K>> {

	public abstract K getOldApplicationId();

}
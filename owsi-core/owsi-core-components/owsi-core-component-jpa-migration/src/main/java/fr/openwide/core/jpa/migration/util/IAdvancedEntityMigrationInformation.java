package fr.openwide.core.jpa.migration.util;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.migration.rowmapper.AbstractResultRowMapper;

public interface IAdvancedEntityMigrationInformation<T extends GenericEntity<Long, T>> extends IMigrationInformation,
		IPreloadAwareMigrationInformation {
	
	Class<? extends AbstractResultRowMapper<?>> getRowMapperClass();

	Class<T> getEntityClass();

	String getTableName();

	String getSqlAllIds();

}

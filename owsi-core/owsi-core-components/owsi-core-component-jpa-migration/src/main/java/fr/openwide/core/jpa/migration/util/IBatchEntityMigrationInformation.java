package fr.openwide.core.jpa.migration.util;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.migration.rowmapper.AbstractResultRowMapper;

public interface IBatchEntityMigrationInformation<T extends GenericEntity<Long, T>> extends IBatchMigrationInformation {

	Class<? extends AbstractResultRowMapper<?>> getRowMapperClass();

	Class<T> getEntityClass();

	String getSqlAllIds();

	/*
	 * Chaîne utilisée dans le IN() de la requête SQL
	 */
	String getParameterIds();

}

package fr.openwide.core.jpa.migration.util;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.migration.rowmapper.AbstractEntityRowMapper;

public interface IMigrationEntitySimpleInformation<T extends GenericEntity<Long, T>> {

	Class<T> getEntityClass();

	Class<? extends AbstractEntityRowMapper<?>> getRowMapperClass();

	String getSqlAllIds();

	/*
	 * Chaîne utilisée dans le IN() éventuel de la requête SQL.
	 * Dans le cas d'un import par lots, il doit obligatoirement être non null.
	 */
	String getParameterIds();

	String getSqlRequest();

}

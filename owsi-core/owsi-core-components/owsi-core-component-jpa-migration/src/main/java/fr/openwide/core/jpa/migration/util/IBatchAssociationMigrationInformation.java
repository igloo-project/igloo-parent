package fr.openwide.core.jpa.migration.util;

import java.util.Map;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.migration.rowmapper.AbstractResultRowMapper;

public interface IBatchAssociationMigrationInformation<T1 extends GenericEntity<Long, T1>, T2>
		extends IBatchMigrationInformation {

	AbstractResultRowMapper<? extends Map<T1, T2>> newRowMapper(int expectedKeys, int expectedValuesPerKey);
	
	void addToAssociation(T1 owning, T2 owned);

	String getAssociationName();
	
	String getSqlAllIds();

	/*
	 * Chaîne utilisée dans le IN() de la requête SQL
	 */
	String getParameterIds();

}

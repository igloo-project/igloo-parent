package fr.openwide.core.jpa.migration.util;

import java.util.Map;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;
import fr.openwide.core.jpa.migration.rowmapper.AbstractResultRowMapper;

public interface IBatchAssociationMigrationInformation<Owning extends GenericEntity<Long, Owning>, Owned>
		extends IBatchMigrationInformation {

	AbstractResultRowMapper<? extends Map<Owning, Owned>> newRowMapper(int expectedKeys, int expectedValuesPerKey);
	
	void addToAssociation(Owning owning, Owned owned);

	String getAssociationName();
	
	String getSqlAllIds();

	/*
	 * Chaîne utilisée dans le IN() de la requête SQL
	 */
	String getParameterIds();

}

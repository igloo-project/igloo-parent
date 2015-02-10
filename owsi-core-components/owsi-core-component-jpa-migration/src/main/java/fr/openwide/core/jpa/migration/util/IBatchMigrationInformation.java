package fr.openwide.core.jpa.migration.util;

import java.util.Map;

import fr.openwide.core.jpa.business.generic.model.GenericEntity;

public interface IBatchMigrationInformation extends IMigrationInformation {

	String getSqlCountRows();

	/*
	 * Map utilisée pour le préchargement d'entités liées pour accélérer la migration.
	 * La clé est la classe des entités à précharger.
	 * La valeur est : 
	 * 		- soit la requête SQL associée
	 * 		- soit null dans le cas où les entités sont déjà préchargées, auquel cas on se contentera d'appeler
	 * 		la méthode listEntitiesByIds de la classe AbstractEntityMigrationService
	 */
	Map<Class<? extends GenericEntity<Long, ?>>, String> getPreloadRequests();

}

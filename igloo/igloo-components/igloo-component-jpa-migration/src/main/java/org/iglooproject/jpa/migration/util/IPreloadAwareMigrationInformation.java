package org.iglooproject.jpa.migration.util;

import java.util.Map;
import org.iglooproject.jpa.business.generic.model.GenericEntity;

public interface IPreloadAwareMigrationInformation {

  /*
   * String used in the IN() clause of the SQL query
   */
  String getParameterIds();

  /*
   * Map used to preload entities to speed up the migration.
   * The key is the class of the entities to preload.
   * The value is:
   * 		- either the associated SQL query
   * 		- or null if the entities are already preloaded. In this case, it's sufficient
   * 		to call the listEntitiesByIds method of AbstractEntityMigrationService
   */
  Map<Class<? extends GenericEntity<Long, ?>>, String> getPreloadRequests();
}

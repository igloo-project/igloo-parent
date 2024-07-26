package org.iglooproject.jpa.migration.util;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.migration.rowmapper.AbstractResultRowMapper;

public interface ISimpleEntityMigrationInformation<T extends GenericEntity<Long, ?>>
    extends IMigrationInformation {

  Class<? extends AbstractResultRowMapper<?>> getRowMapperClass();

  Class<T> getEntityClass();

  String getSqlAllIds();

  /*
   * Chaîne utilisée dans le IN() éventuel de la requête SQL.
   * Dans le cas d'un import par lots, il doit obligatoirement être non null.
   */
  String getParameterIds();
}

package org.iglooproject.jpa.migration.util;

import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.migration.rowmapper.AbstractResultRowMapper;

public interface IBatchEntityMigrationInformation<T extends GenericEntity<Long, T>>
    extends IBatchMigrationInformation {

  Class<? extends AbstractResultRowMapper<?>> getRowMapperClass();

  Class<T> getEntityClass();

  String getSqlAllIds();
}

package org.iglooproject.jpa.migration.util;

import java.util.Map;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.migration.rowmapper.AbstractResultRowMapper;

public interface IBatchAssociationMigrationInformation<
        Owning extends GenericEntity<Long, Owning>, Owned>
    extends IBatchMigrationInformation {

  AbstractResultRowMapper<? extends Map<Owning, Owned>> newRowMapper(
      int expectedKeys, int expectedValuesPerKey);

  void addToAssociation(Owning owning, Owned owned);

  String getAssociationName();

  String getSqlAllIds();
}

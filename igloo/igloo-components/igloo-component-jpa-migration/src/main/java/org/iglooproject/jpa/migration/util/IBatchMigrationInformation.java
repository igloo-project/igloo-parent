package org.iglooproject.jpa.migration.util;

public interface IBatchMigrationInformation
    extends IMigrationInformation, IPreloadAwareMigrationInformation {

  String getSqlCountRows();
}

package fr.openwide.core.jpa.migration.util;

public interface IBatchMigrationInformation extends IMigrationInformation, IPreloadAwareMigrationInformation {

	String getSqlCountRows();

}

package fr.openwide.core.jpa.util;

import org.hibernate.cfg.NamingStrategy;

/**
 * A wrapper which truncates all database names (table names, column names,
 * ...) to a fixed length. <br>
 * It is useful with PostgreSQL in particular, which silently truncates all
 * names to 64 characters by default. If we don't truncate names on the
 * Hibernate side, further comparisons of the Hibernate table model with the
 * PostgreSQL table model can result in missing matches : for instance,
 * 'a_column_with_a_long_name' can be truncated to 'a_column' on the PostgreSQL
 * side, so that Hibernate will flag 'a_column_with_a_long_name' as missing, and
 * will try adding the column although it already exists with a truncated name.
 */
public class TruncatingNamingStrategyWrapper implements NamingStrategy {
	
	private final NamingStrategy delegate;
	
	private final int maxNameLength;
	
	public TruncatingNamingStrategyWrapper(NamingStrategy delegate, int maxNameLength) {
		this.delegate = delegate;
		this.maxNameLength = maxNameLength;
	}
	
	protected String truncateName(String propertyName) {
		if (propertyName == null || propertyName.length() <= maxNameLength) {
			return propertyName;
		} else {
			return propertyName.substring(0, maxNameLength);
		}
	}

	@Override
	public String classToTableName(String className) {
		return truncateName(delegate.classToTableName(className));
	}

	@Override
	public String propertyToColumnName(String propertyName) {
		return truncateName(delegate.propertyToColumnName(propertyName));
	}

	@Override
	public String tableName(String tableName) {
		return truncateName(delegate.tableName(tableName));
	}

	@Override
	public String columnName(String columnName) {
		return truncateName(delegate.columnName(columnName));
	}

	@Override
	public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity,
			String associatedEntityTable, String propertyName) {
		return truncateName(delegate.collectionTableName(ownerEntity, ownerEntityTable, associatedEntity, associatedEntityTable, propertyName));
	}

	@Override
	public String joinKeyColumnName(String joinedColumn, String joinedTable) {
		return truncateName(delegate.joinKeyColumnName(joinedColumn, joinedTable));
	}

	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName,
			String referencedColumnName) {
		return truncateName(delegate.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName));
	}

	@Override
	public String logicalColumnName(String columnName, String propertyName) {
		return truncateName(delegate.logicalColumnName(columnName, propertyName));
	}

	@Override
	public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable,
			String propertyName) {
		return truncateName(delegate.logicalCollectionTableName(tableName, ownerEntityTable, associatedEntityTable, propertyName));
	}

	@Override
	public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
		return truncateName(delegate.logicalCollectionColumnName(columnName, propertyName, referencedColumn));
	}

}

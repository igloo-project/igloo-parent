package org.iglooproject.jpa.util;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public final class DbTypeConstants {
	/**
	 * <p>postgresql mainstream driver (https://jdbc.postgresql.org/)</p>
	 * <p>This is the preferred postgresql driver</p>
	 */
	public static final String DB_TYPE_POSTGRESQL = "postgresql";
	/**
	 * <p>pgjdbc-ng driver (http://impossibl.github.io/pgjdbc-ng/)</p>
	 */
	public static final String DB_TYPE_PGSQL = "pgsql";
	public static final String DB_TYPE_MYSQL5 = "mysql5";
	public static final String DB_TYPE_MYSQL = "mysql";
	public static final String DB_TYPE_ORACLE10G = "oracle10g";
	public static final String DB_TYPE_H2 = "h2";
	public static final String DB_TYPE_SQLSERVER = "sqlserver";
	
	public static final Set<String> FAMILY_POSTGRESQL = ImmutableSet.<String>builder()
		.add(DB_TYPE_POSTGRESQL)
		.add(DB_TYPE_PGSQL)
		.build();

	private DbTypeConstants() {
	}

}

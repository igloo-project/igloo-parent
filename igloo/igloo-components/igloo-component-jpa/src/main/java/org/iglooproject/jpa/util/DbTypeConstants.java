package org.iglooproject.jpa.util;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

public final class DbTypeConstants {
	public static final String DB_TYPE_POSTGRESQL = "postgresql";
	@Deprecated
	public static final String DB_TYPE_PGSQL = "pgsql";
	public static final String DB_TYPE_MYSQL5 = "mysql5";
	public static final String DB_TYPE_MYSQL = "mysql";
	public static final String DB_TYPE_ORACLE10G = "oracle10g";
	public static final String DB_TYPE_H2 = "h2";
	public static final String DB_TYPE_SQLSERVER = "sqlserver";
	
	public static final Set<String> FAMILY_POSTGRESQL = ImmutableSet.<String>builder()
			.add(DB_TYPE_POSTGRESQL)
			.add(DB_TYPE_PGSQL).build();

}

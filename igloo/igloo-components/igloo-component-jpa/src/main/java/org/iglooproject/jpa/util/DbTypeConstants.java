package org.iglooproject.jpa.util;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

public final class DbTypeConstants {
  /**
   * postgresql mainstream driver (https://jdbc.postgresql.org/)
   *
   * <p>This is the preferred postgresql driver
   */
  public static final String DB_TYPE_POSTGRESQL = "postgresql";

  /** pgjdbc-ng driver (http://impossibl.github.io/pgjdbc-ng/) */
  public static final String DB_TYPE_PGSQL = "pgsql";

  public static final String DB_TYPE_MYSQL5 = "mysql5";
  public static final String DB_TYPE_MYSQL = "mysql";
  public static final String DB_TYPE_ORACLE10G = "oracle10g";
  public static final String DB_TYPE_H2 = "h2";
  public static final String DB_TYPE_SQLSERVER = "sqlserver";

  public static final Set<String> FAMILY_POSTGRESQL =
      ImmutableSet.<String>builder().add(DB_TYPE_POSTGRESQL).add(DB_TYPE_PGSQL).build();

  private DbTypeConstants() {}
}

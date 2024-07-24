package org.iglooproject.test.jpa.util.jdbc.model;

import java.sql.DatabaseMetaData;

/**
 * Constants to use with {@link DatabaseMetaData}.
 *
 * @author Laurent Almeras
 */
public final class JdbcDatabaseMetaDataConstants {

  public static final String REL_TYPE_TABLE = "TABLE";
  public static final String REL_TYPE_SEQUENCE = "SEQUENCE";

  private JdbcDatabaseMetaDataConstants() {}
}

package org.igloo.hibernate.hbm;

public class SqlOutput {

  private final SqlOutputType type;
  private final String target;

  private SqlOutput(SqlOutputType type, String target) {
    this.type = type;
    this.target = target;
  }

  public String getTarget() {
    return target;
  }

  public boolean isFile() {
    return SqlOutputType.FILE.equals(type);
  }

  public static SqlOutput file(String target) {
    return new SqlOutput(SqlOutputType.FILE, target);
  }

  public static SqlOutput stdout() {
    return new SqlOutput(SqlOutputType.STDOUT, null);
  }

  private enum SqlOutputType {
    FILE,
    STDOUT;
  };
}

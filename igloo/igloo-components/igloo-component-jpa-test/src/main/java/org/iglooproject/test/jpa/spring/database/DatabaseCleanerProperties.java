package org.iglooproject.test.jpa.spring.database;

public class DatabaseCleanerProperties {
  private String[] excludes = new String[0];
  private String[] tables = new String[0];

  public String[] getExcludes() {
    if (excludes == null) {
      return new String[0];
    }
    return excludes;
  }

  public void setExcludes(String[] excludes) {
    this.excludes = excludes;
  }

  public String[] getTables() {
    if (tables == null) {
      return new String[0];
    }
    return tables;
  }

  public void setTables(String[] tables) {
    this.tables = tables;
  }
}

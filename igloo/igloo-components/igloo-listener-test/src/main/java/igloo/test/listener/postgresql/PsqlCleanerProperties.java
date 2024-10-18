package igloo.test.listener.postgresql;

import java.util.stream.Stream;

public class PsqlCleanerProperties {
  private String[] excludes = new String[0];
  private boolean excludeFlywayTable = true;

  public String[] getExcludes() {
    if (excludes == null) {
      return new String[0];
    }
    return excludes;
  }

  public void setExcludes(String[] excludes) {
    if (excludes == null) {
      excludes = new String[0];
    }
    this.excludes = Stream.of(excludes).map(String::toLowerCase).toArray(i -> new String[i]);
  }

  public boolean isExcludeFlywayTable() {
    return excludeFlywayTable;
  }

  public void setExcludeFlywayTable(boolean excludeFlywayTable) {
    this.excludeFlywayTable = excludeFlywayTable;
  }
}

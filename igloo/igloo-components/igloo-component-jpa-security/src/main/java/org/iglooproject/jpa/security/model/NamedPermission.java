package org.iglooproject.jpa.security.model;

import java.util.Objects;
import org.springframework.security.acls.model.Permission;

public class NamedPermission implements Permission {

  private static final long serialVersionUID = 4702776652617722298L;

  public static final NamedPermission READ = new NamedPermission(CorePermissionConstants.READ);
  public static final NamedPermission WRITE = new NamedPermission(CorePermissionConstants.WRITE);
  public static final NamedPermission CREATE = new NamedPermission(CorePermissionConstants.CREATE);
  public static final NamedPermission DELETE = new NamedPermission(CorePermissionConstants.DELETE);
  public static final NamedPermission ADMINISTRATION =
      new NamedPermission(CorePermissionConstants.ADMINISTRATION);

  protected String name;

  protected NamedPermission(String name) {
    this.name = name;
  }

  @Override
  public int getMask() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getPattern() {
    throw new UnsupportedOperationException();
  }

  public String getName() {
    return name;
  }

  @Override
  public final String toString() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof NamedPermission other)) {
      return false;
    }
    return Objects.equals(name, other.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}

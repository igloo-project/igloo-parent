package org.iglooproject.jpa.security.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.acls.model.Permission;

public class NamedPermission implements Permission {

  private static final long serialVersionUID = 4702776652617722298L;

  public static final NamedPermission READ = new NamedPermission(CorePermissionConstants.READ);
  public static final NamedPermission WRITE = new NamedPermission(CorePermissionConstants.WRITE);
  public static final NamedPermission CREATE = new NamedPermission(CorePermissionConstants.CREATE);
  public static final NamedPermission DELETE = new NamedPermission(CorePermissionConstants.DELETE);
  public static final NamedPermission ADMINISTRATION =
      new NamedPermission(CorePermissionConstants.ADMINISTRATION);
  public static final NamedPermission ADMIN_SIGN_IN_AS =
      new NamedPermission(CorePermissionConstants.ADMIN_SIGN_IN_AS);

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
  public boolean equals(Object object) {
    if (object == null) {
      return false;
    }
    if (object == this) {
      return true;
    }
    if (object instanceof NamedPermission) {
      NamedPermission permission = (NamedPermission) object;
      return new EqualsBuilder().append(name, permission.getName()).build();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(name).build();
  }
}

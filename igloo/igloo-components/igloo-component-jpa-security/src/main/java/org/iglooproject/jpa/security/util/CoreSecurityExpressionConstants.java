package org.iglooproject.jpa.security.util;

import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.model.CorePermissionConstants;

public class CoreSecurityExpressionConstants {

  public static final String COLLECTION_READ =
      "hasPermission(filterObject, '" + CorePermissionConstants.READ + "')";
  public static final String RETURN_READ =
      "hasPermission(returnObject, '" + CorePermissionConstants.READ + "')";

  public static final String ROLE_SYSTEM = "hasRole('" + CoreAuthorityConstants.ROLE_SYSTEM + "')";
  public static final String ROLE_ADMIN = "hasRole('" + CoreAuthorityConstants.ROLE_ADMIN + "')";
  public static final String ROLE_AUTHENTICATED =
      "hasRole('" + CoreAuthorityConstants.ROLE_AUTHENTICATED + "')";
  public static final String ROLE_ANONYMOUS =
      "hasRole('" + CoreAuthorityConstants.ROLE_ANONYMOUS + "')";

  protected CoreSecurityExpressionConstants() {}
}

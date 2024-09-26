package basicapp.back.security.model;

import static org.iglooproject.commons.util.security.PermissionObject.DEFAULT_PERMISSION_OBJECT_NAME;

public final class BasicApplicationSecurityExpressionConstants {

  public static final String USER_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_WRITE
          + "')";

  public static final String USER_BASIC_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_BASIC_WRITE
          + "')";

  public static final String USER_TECHNICAL_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_TECHNICAL_WRITE
          + "')";

  public static final String USER_ENABLE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_ENABLE
          + "')";

  public static final String USER_DISABLE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_DISABLE
          + "')";

  public static final String USER_EDIT_PASSWORD =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_EDIT_PASSWORD
          + "')";

  public static final String USER_RECOVERY_PASSWORD =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_RECOVERY_PASSWORD
          + "')";

  public static final String ADMIN_EDIT_PASSWORD =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.ADMIN_EDIT_PASSWORD
          + "')";

  public static final String ADMIN_RECOVERY_PASSWORD =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.ADMIN_RECOVERY_PASSWORD
          + "')";

  public static final String USER_OPEN_ANNONCEMENT =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_OPEN_ANNONCEMENT
          + "')";

  public static final String USER_CLOSE_ANNONCEMENT =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_CLOSE_ANNONCEMENT
          + "')";

  public static final String ROLE_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.ROLE_WRITE
          + "')";

  public static final String ANNOUNCEMENT_WRITE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.ANNOUNCEMENT_WRITE
          + "')";
  public static final String ANNOUNCEMENT_REMOVE =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.ANNOUNCEMENT_REMOVE
          + "')";

  private BasicApplicationSecurityExpressionConstants() {}
}

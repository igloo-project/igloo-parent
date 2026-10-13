package basicapp.back.security.model;

import static org.iglooproject.commons.util.security.PermissionObject.DEFAULT_PERMISSION_OBJECT_NAME;

public final class BasicApplicationSecurityExpressionConstants {

  public static final String USER_WRITE_ADMIN =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_WRITE_ADMIN
          + "')";

  public static final String USER_BASIC_WRITE_ADMIN =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_BASIC_WRITE_ADMIN
          + "')";

  public static final String USER_TECHNICAL_WRITE_ADMIN =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_TECHNICAL_WRITE_ADMIN
          + "')";

  public static final String USER_ENABLE_ADMIN =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_ENABLE_ADMIN
          + "')";

  public static final String USER_DISABLE_ADMIN =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_DISABLE_ADMIN
          + "')";

  public static final String USER_PASSWORD_RESET_ADMIN =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_PASSWORD_RESET_ADMIN
          + "')";

  public static final String USER_PASSWORD_UPDATE_USER =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_PASSWORD_UPDATE_USER
          + "')";

  public static final String USER_ANNONCEMENT_OPEN_USER =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_ANNONCEMENT_OPEN_USER
          + "')";

  public static final String USER_ANNONCEMENT_CLOSE_USER =
      "hasPermission(#"
          + DEFAULT_PERMISSION_OBJECT_NAME
          + ", '"
          + BasicApplicationPermissionConstants.USER_ANNONCEMENT_CLOSE_USER
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

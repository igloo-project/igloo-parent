package basicapp.back.security.model;

import org.iglooproject.jpa.security.model.CorePermissionConstants;

public class BasicApplicationPermissionConstants extends CorePermissionConstants {

  // -------------------------------------------------------------------------------
  // "Object" permissions (relate to an object in particular)
  // See also CorePermisionConstants (parent class)

  // Add contants of the form public static final String MY_PERMISSION_NAME = "MY_PERMISSION_NAME";
  // here

  public static final String REFERENCE_DATA_READ = "REFERENCE_DATA_READ";
  public static final String REFERENCE_DATA_WRITE = "REFERENCE_DATA_WRITE_CREATE";

  public static final String ROLE_READ = "ROLE_READ";
  public static final String ROLE_WRITE = "ROLE_WRITE";

  public static final String USER_READ = "USER_READ";
  public static final String USER_BASIC_WRITE = "USER_BASIC_WRITE";
  public static final String USER_TECHNICAL_WRITE = "USER_TECHNICAL_WRITE";
  public static final String USER_OPEN_ANNONCEMENT = "USER_OPEN_ANNONCEMENT";
  public static final String USER_CLOSE_ANNONCEMENT = "USER_CLOSE_ANNONCEMENT";
  public static final String USER_DISABLE = "USER_DISABLE";
  public static final String USER_ENABLE = "USER_ENABLE";

  public static final String ADMIN_EDIT_PASSWORD = "ADMIN_EDIT_PASSWORD";
  public static final String ADMIN_RECOVERY_PASSWORD = "ADMIN_RECOVERY_PASSWORD";
  public static final String USER_EDIT_PASSWORD = "USER_EDIT_PASSWORD";
  public static final String USER_RECOVERY_PASSWORD = "USER_RECOVERY_PASSWORD";

  public static final String ANNOUNCEMENT_WRITE = "ANNOUNCEMENT_WRITE";
  public static final String ANNOUNCEMENT_REMOVE = "ANNOUNCEMENT_REMOVE";

  // -------------------------------------------------------------------------------
  // "Global" permissions (relate to no  object in particular, but to a class of objects)

  // Add contants of the form public static final String MY_PERMISSION_NAME = "MY_PERMISSION_NAME";
  // here

  public static final String GLOBAL_ROLE_READ = "GLOBAL_ROLE_READ";
  public static final String GLOBAL_ROLE_WRITE = "GLOBAL_ROLE_WRITE";
  public static final String GLOBAL_USER_READ = "GLOBAL_USER_READ";
  public static final String GLOBAL_USER_WRITE = "GLOBAL_USER_WRITE";
  public static final String GLOBAL_ANNOUNCEMENT_READ = "GLOBAL_ANNOUNCEMENT_READ";
  public static final String GLOBAL_ANNOUNCEMENT_WRITE = "GLOBAL_ANNOUNCEMENT_WRITE";
  public static final String GLOBAL_REFERENCE_DATA_READ = "GLOBAL_REFERENCE_DATA_READ";
  public static final String GLOBAL_REFERENCE_DATA_WRITE = "GLOBAL_REFERENCE_DATA_WRITE";
}

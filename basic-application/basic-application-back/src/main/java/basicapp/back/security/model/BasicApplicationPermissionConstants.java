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

  // User - Admin
  public static final String USER_READ_ADMIN = "USER_READ_ADMIN";
  public static final String USER_WRITE_ADMIN = "USER_WRITE_ADMIN";
  public static final String USER_BASIC_WRITE_ADMIN = "USER_BASIC_WRITE_ADMIN";
  public static final String USER_TECHNICAL_WRITE_ADMIN = "USER_TECHNICAL_WRITE_ADMIN";
  public static final String USER_ENABLE_ADMIN = "USER_ENABLE_ADMIN";
  public static final String USER_DISABLE_ADMIN = "USER_DISABLE_ADMIN";
  public static final String USER_PASSWORD_RESET_ADMIN = "USER_PASSWORD_RESET_ADMIN";
  // User - User
  public static final String USER_PASSWORD_UPDATE_USER = "USER_PASSWORD_UPDATE_USER";
  public static final String USER_ANNONCEMENT_OPEN_USER = "USER_ANNONCEMENT_OPEN_USER";
  public static final String USER_ANNONCEMENT_CLOSE_USER = "USER_ANNONCEMENT_CLOSE_USER";

  public static final String ROLE_READ = "ROLE_READ";
  public static final String ROLE_WRITE = "ROLE_WRITE";

  public static final String ANNOUNCEMENT_WRITE = "ANNOUNCEMENT_WRITE";
  public static final String ANNOUNCEMENT_REMOVE = "ANNOUNCEMENT_REMOVE";

  // -------------------------------------------------------------------------------
  // "Global" permissions (relate to no  object in particular, but to a class of objects)

  // Add contants of the form public static final String MY_PERMISSION_NAME = "MY_PERMISSION_NAME";
  // here

  public static final String GLOBAL_REFERENCE_DATA_READ = "GLOBAL_REFERENCE_DATA_READ";
  public static final String GLOBAL_REFERENCE_DATA_WRITE = "GLOBAL_REFERENCE_DATA_WRITE";

  public static final String GLOBAL_USER_READ = "GLOBAL_USER_READ";
  public static final String GLOBAL_USER_WRITE = "GLOBAL_USER_WRITE";

  public static final String GLOBAL_ROLE_READ = "GLOBAL_ROLE_READ";
  public static final String GLOBAL_ROLE_WRITE = "GLOBAL_ROLE_WRITE";

  public static final String GLOBAL_ANNOUNCEMENT_READ = "GLOBAL_ANNOUNCEMENT_READ";
  public static final String GLOBAL_ANNOUNCEMENT_WRITE = "GLOBAL_ANNOUNCEMENT_WRITE";
}

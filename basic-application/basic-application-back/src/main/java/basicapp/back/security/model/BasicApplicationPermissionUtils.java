package basicapp.back.security.model;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ANNOUNCEMENT_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ANNOUNCEMENT_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_REFERENCE_DATA_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ROLE_WRITE;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_READ;
import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_USER_WRITE;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Ordering;
import java.util.List;
import java.util.Map;

public class BasicApplicationPermissionUtils {

  public static final Map<BasicApplicationPermissionCategoryEnum, List<String>> PERMISSIONS =
      new ImmutableSortedMap.Builder<BasicApplicationPermissionCategoryEnum, List<String>>(
              Ordering.natural())
          .put(
              BasicApplicationPermissionCategoryEnum.REFERENCE_DATA,
              List.of(GLOBAL_REFERENCE_DATA_READ, GLOBAL_REFERENCE_DATA_WRITE))
          .put(
              BasicApplicationPermissionCategoryEnum.ADMINISTRATION,
              List.of(
                  GLOBAL_ROLE_READ,
                  GLOBAL_ROLE_WRITE,
                  GLOBAL_ANNOUNCEMENT_READ,
                  GLOBAL_ANNOUNCEMENT_WRITE,
                  GLOBAL_USER_READ,
                  GLOBAL_USER_WRITE))
          .build();

  private BasicApplicationPermissionUtils() {}
}

package org.iglooproject.jpa.security.spring;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Map;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.jpa.security.model.CorePermissionConstants;

// TODO RFO a suprimer
public class SecurityUtils {

  public static String defaultPermissionHierarchyAsString() {
    return hierarchyAsStringFromMap(
        ImmutableMultimap.<String, String>builder()
            .put(CorePermissionConstants.ADMINISTRATION, CorePermissionConstants.WRITE)
            .put(CorePermissionConstants.WRITE, CorePermissionConstants.READ)
            .build());
  }

  public static String defaultRoleHierarchyAsString() {
    return hierarchyAsStringFromMap(
        ImmutableMultimap.<String, String>builder()
            .put(CoreAuthorityConstants.ROLE_SYSTEM, CoreAuthorityConstants.ROLE_ADMIN)
            .put(CoreAuthorityConstants.ROLE_ADMIN, CoreAuthorityConstants.ROLE_AUTHENTICATED)
            .put(CoreAuthorityConstants.ROLE_AUTHENTICATED, CoreAuthorityConstants.ROLE_ANONYMOUS)
            .build());
  }

  public static String hierarchyAsStringFromMap(Multimap<String, String> multimap) {
    return hierarchyAsStringFromMap(multimap.asMap());
  }

  public static String hierarchyAsStringFromMap(Map<String, ? extends Collection<String>> map) {
    StringBuilder builder = new StringBuilder();
    for (Map.Entry<String, ? extends Collection<String>> entry : map.entrySet()) {
      String parent = entry.getKey();
      for (String child : entry.getValue()) {
        builder.append(parent).append(" > ").append(child).append("\n");
      }
    }
    return builder.toString();
  }

  private SecurityUtils() {}
}

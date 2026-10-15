package basicapp.back.security.model;

import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Field;
import java.util.Collection;
import org.iglooproject.jpa.security.model.NamedPermission;

public final class BasicApplicationPermission extends NamedPermission {

  private static final long serialVersionUID = 8541973919257428300L;

  public static final Collection<BasicApplicationPermission> ALL;

  static {
    ImmutableSet.Builder<BasicApplicationPermission> builder = ImmutableSet.builder();
    Field[] fields = BasicApplicationPermissionConstants.class.getFields();
    for (Field field : fields) {
      try {
        Object fieldValue = field.get(null);
        if (fieldValue instanceof String name) {
          builder.add(new BasicApplicationPermission(name));
        }
      } catch (IllegalArgumentException | IllegalAccessException _) {
      }
    }
    ALL = builder.build();
  }

  private BasicApplicationPermission(String name) {
    super(name);
  }
}

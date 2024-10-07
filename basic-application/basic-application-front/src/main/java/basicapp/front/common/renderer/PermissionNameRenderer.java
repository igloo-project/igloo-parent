package basicapp.front.common.renderer;

import igloo.wicket.renderer.Renderer;
import java.util.Locale;
import org.springframework.util.StringUtils;

public class PermissionNameRenderer extends Renderer<String> {

  private static final long serialVersionUID = 1L;

  private static final PermissionNameRenderer INSTANCE = new PermissionNameRenderer();

  public static PermissionNameRenderer get() {
    return INSTANCE;
  }

  @Override
  public String render(String value, Locale locale) {
    if (!StringUtils.hasText(value)) {
      return null;
    }
    return getString("permission." + value, locale);
  }
}

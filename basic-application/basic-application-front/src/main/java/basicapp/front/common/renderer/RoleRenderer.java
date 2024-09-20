package basicapp.front.common.renderer;

import basicapp.back.business.role.model.Role;
import igloo.wicket.renderer.Renderer;
import java.util.Locale;

public abstract class RoleRenderer extends Renderer<Role> {

  private static final long serialVersionUID = 1L;

  private static final Renderer<Role> INSTANCE =
      new RoleRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        public String render(Role value, Locale locale) {
          return value.getTitle();
        }
      }.nullsAsNull();

  public static Renderer<Role> get() {
    return INSTANCE;
  }

  private RoleRenderer() {}
}

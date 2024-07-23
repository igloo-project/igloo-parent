package org.iglooproject.basicapp.web.application.common.renderer;

import igloo.bootstrap.common.BootstrapColor;
import java.util.Locale;
import org.iglooproject.basicapp.core.business.user.model.User;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;

public abstract class UserEnabledRenderer extends BootstrapRenderer<User> {

  private static final long serialVersionUID = 8417578372352258838L;

  private static final UserEnabledRenderer INSTANCE =
      new UserEnabledRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        protected BootstrapRendererInformation doRender(User value, Locale locale) {
          if (value == null) {
            return null;
          }
          if (value.isEnabled()) {
            return BootstrapRendererInformation.builder()
                .label(getString("business.user.enabled.true", locale))
                .icon("fa fa-fw fa-check")
                .color(BootstrapColor.SUCCESS)
                .build();
          } else {
            return BootstrapRendererInformation.builder()
                .label(getString("business.user.enabled.false", locale))
                .icon("fa fa-fw fa-times")
                .color(BootstrapColor.SECONDARY)
                .build();
          }
        }
      };

  public static final UserEnabledRenderer get() {
    return INSTANCE;
  }

  private UserEnabledRenderer() {}
}

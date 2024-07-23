package org.iglooproject.basicapp.web.application.common.renderer;

import igloo.bootstrap.common.BootstrapColor;
import java.util.Locale;
import org.iglooproject.basicapp.core.business.announcement.model.Announcement;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRenderer;
import org.iglooproject.wicket.more.markup.html.bootstrap.common.renderer.BootstrapRendererInformation;

public abstract class AnnouncementEnabledRenderer extends BootstrapRenderer<Announcement> {

  private static final long serialVersionUID = 8417578372352258838L;

  private static final AnnouncementEnabledRenderer INSTANCE =
      new AnnouncementEnabledRenderer() {
        private static final long serialVersionUID = 1L;

        @Override
        protected BootstrapRendererInformation doRender(Announcement value, Locale locale) {
          if (value == null) {
            return null;
          }
          if (value.isEnabled()) {
            return BootstrapRendererInformation.builder()
                .label(getString("business.announcement.enabled.true", locale))
                .icon("fa fa-fw fa-check")
                .color(BootstrapColor.SUCCESS)
                .build();
          } else {
            return BootstrapRendererInformation.builder()
                .label(getString("business.announcement.enabled.false", locale))
                .icon("fa fa-fw fa-ban")
                .color(BootstrapColor.SECONDARY)
                .build();
          }
        }
      };

  public static final AnnouncementEnabledRenderer get() {
    return INSTANCE;
  }

  private AnnouncementEnabledRenderer() {}
}

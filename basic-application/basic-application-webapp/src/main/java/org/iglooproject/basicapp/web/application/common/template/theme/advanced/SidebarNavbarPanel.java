package org.iglooproject.basicapp.web.application.common.template.theme.advanced;

import org.apache.wicket.markup.html.panel.Panel;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;

public class SidebarNavbarPanel extends Panel {

  private static final long serialVersionUID = -3741272240940846720L;

  public SidebarNavbarPanel(String id) {
    super(id);

    add(BasicApplicationApplication.get().getHomePageLinkDescriptor().link("home"));
  }
}

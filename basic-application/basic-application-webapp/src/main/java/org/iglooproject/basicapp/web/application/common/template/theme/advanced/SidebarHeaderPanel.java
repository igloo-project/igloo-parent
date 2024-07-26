package org.iglooproject.basicapp.web.application.common.template.theme.advanced;

import igloo.wicket.condition.Condition;
import org.apache.wicket.markup.html.panel.Panel;
import org.iglooproject.basicapp.web.application.BasicApplicationApplication;

public class SidebarHeaderPanel extends Panel {

  private static final long serialVersionUID = -926306336711136387L;

  public SidebarHeaderPanel(String id) {
    super(id);

    add(Condition.anyChildVisible(this).thenShow());

    add(BasicApplicationApplication.get().getHomePageLinkDescriptor().link("home"));
  }
}

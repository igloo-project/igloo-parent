package org.iglooproject.wicket.more.markup.repeater.table;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

public class CoreToolbarsContainer extends WebMarkupContainer {

  private static final long serialVersionUID = 2842680043238970230L;

  private final RepeatingView toolbars;

  public CoreToolbarsContainer(final String id) {
    super(id);
    toolbars = new RepeatingView("toolbars");
    add(toolbars);
  }

  public RepeatingView getRepeatingView() {
    return toolbars;
  }

  @Override
  public void onConfigure() {
    super.onConfigure();

    toolbars.configure();

    Boolean visible =
        toolbars.visitChildren(
            new IVisitor<Component, Boolean>() {
              @Override
              public void component(Component object, IVisit<Boolean> visit) {
                object.configure();
                if (object.isVisible()) {
                  visit.stop(Boolean.TRUE);
                } else {
                  visit.dontGoDeeper();
                }
              }
            });
    if (visible == null) {
      visible = false;
    }
    setVisible(visible);
  }
}

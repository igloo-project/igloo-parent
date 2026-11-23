package org.iglooproject.wicket.more.markup.repeater.table;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;

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
            (object, visit) -> {
              object.configure();
              if (object.isVisible()) {
                visit.stop(Boolean.TRUE);
              } else {
                visit.dontGoDeeper();
              }
            });
    if (visible == null) {
      visible = false;
    }
    setVisible(visible);
  }
}

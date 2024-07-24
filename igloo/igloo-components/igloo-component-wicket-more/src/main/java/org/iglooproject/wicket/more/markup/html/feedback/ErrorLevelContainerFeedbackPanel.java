package org.iglooproject.wicket.more.markup.html.feedback;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

class ErrorLevelContainerFeedbackPanel extends FeedbackPanel {

  private static final long serialVersionUID = -731818705093875451L;
  protected int level;

  public ErrorLevelContainerFeedbackPanel(String id, int level, MarkupContainer component) {
    super(id, new ErrorLevelContainerFeedbackMessageFilter(component, level));
    this.level = level;
  }

  @Override
  public boolean isVisible() {
    return anyMessage(level);
  }
}

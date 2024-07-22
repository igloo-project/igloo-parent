package org.iglooproject.wicket.more.markup.html.feedback;

import org.apache.wicket.feedback.ExactLevelFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class ErrorLevelFeedbackPanel extends FeedbackPanel {

  private static final long serialVersionUID = -731818705093875451L;
  protected int level;

  public ErrorLevelFeedbackPanel(String id, int level) {
    super(id, new ExactLevelFeedbackMessageFilter(level));
    this.level = level;
  }

  @Override
  public boolean isVisible() {
    return anyMessage(level);
  }
}

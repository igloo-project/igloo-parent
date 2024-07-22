package org.iglooproject.wicket.more.markup.html.feedback;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class GlobalFeedbackPanel extends AbstractFeedbackPanel {

  private static final long serialVersionUID = -6830234934956457182L;

  public GlobalFeedbackPanel(String id) {
    super(id, null);
  }

  @Override
  public FeedbackPanel getFeedbackPanel(String id, int level, MarkupContainer component) {
    return new ErrorLevelFeedbackPanel(id, level);
  }
}

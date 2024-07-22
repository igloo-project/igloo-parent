package org.iglooproject.wicket.more.markup.html.feedback;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

/**
 * Filtre de feedback qui permet de limiter les messages à un élément et ses enfants et à un seul
 * niveau d'alerte (permet de faire des affichages différenciés par niveaux).
 */
class ErrorLevelContainerFeedbackMessageFilter implements IFeedbackMessageFilter {

  private static final long serialVersionUID = -8307834623085786613L;

  private final MarkupContainer container;

  private final int level;

  public ErrorLevelContainerFeedbackMessageFilter(MarkupContainer component, int level) {
    super();

    this.level = level;
    this.container = component;
  }

  @Override
  public boolean accept(FeedbackMessage message) {
    if (message.getReporter() == null) {
      return false;
    } else {
      return (container.contains(message.getReporter(), true) || container == message.getReporter())
          && message.getLevel() == level;
    }
  }
}

package org.iglooproject.wicket.more.markup.html.feedback;

import org.apache.wicket.Component;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;

public class ContainerClassFeedbackMessageFilter<T> implements IFeedbackMessageFilter {

  private static final long serialVersionUID = -3527902186905319759L;

  private final Class<T> containerClass;

  public ContainerClassFeedbackMessageFilter(Class<T> containerClass) {
    super();
    this.containerClass = containerClass;
  }

  @Override
  public final boolean accept(FeedbackMessage message) {
    Component component = message.getReporter();

    T container = null;
    if (component != null) {
      if (containerClass.isInstance(component)) {
        container = containerClass.cast(component);
      } else {
        container = component.findParent(containerClass);
      }
    }

    return accept(message, container);
  }

  protected boolean accept(FeedbackMessage message, T container) {
    return container != null;
  }
}

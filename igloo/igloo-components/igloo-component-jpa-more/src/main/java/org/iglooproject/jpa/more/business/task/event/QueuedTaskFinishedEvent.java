package org.iglooproject.jpa.more.business.task.event;

import org.springframework.context.ApplicationEvent;

public class QueuedTaskFinishedEvent extends ApplicationEvent {

  private static final long serialVersionUID = 8649861744549925604L;

  public QueuedTaskFinishedEvent(Long queuedTaskId) {
    super(queuedTaskId);
  }
}

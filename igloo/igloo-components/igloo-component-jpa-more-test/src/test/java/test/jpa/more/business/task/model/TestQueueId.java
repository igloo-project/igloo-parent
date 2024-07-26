package test.jpa.more.business.task.model;

import org.iglooproject.jpa.more.business.task.model.IQueueId;

public enum TestQueueId implements IQueueId {
  QUEUE_1,
  QUEUE_2;

  @Override
  public String getUniqueStringId() {
    return name();
  }
}

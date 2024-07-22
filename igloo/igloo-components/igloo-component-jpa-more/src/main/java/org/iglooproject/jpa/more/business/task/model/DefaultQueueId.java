package org.iglooproject.jpa.more.business.task.model;

public enum DefaultQueueId implements IQueueId {
  DEFAULT("&__DEFAULT_QUEUE__&");

  private final String uniqueStringId;

  private DefaultQueueId(String uniqueStringId) {
    this.uniqueStringId = uniqueStringId;
  }

  @Override
  public String getUniqueStringId() {
    return uniqueStringId;
  }
}

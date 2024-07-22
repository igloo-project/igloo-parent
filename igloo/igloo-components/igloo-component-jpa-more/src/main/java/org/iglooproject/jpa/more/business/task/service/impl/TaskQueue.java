package org.iglooproject.jpa.more.business.task.service.impl;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ForwardingBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

public final class TaskQueue extends ForwardingBlockingQueue<Long> {

  private final String id;

  private final BlockingQueue<Long> delegate = Queues.newLinkedBlockingQueue();

  public TaskQueue(String id) {
    Assert.notNull(id, "[Assertion failed] - this argument is required; it must not be null");
    this.id = id;
  }

  public final String getId() {
    return id;
  }

  @Override
  protected BlockingQueue<Long> delegate() {
    return delegate;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", id)
        .append("delegate", delegate)
        .build();
  }
}

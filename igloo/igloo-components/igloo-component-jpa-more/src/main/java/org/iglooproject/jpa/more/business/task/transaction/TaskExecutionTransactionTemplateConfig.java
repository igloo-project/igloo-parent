package org.iglooproject.jpa.more.business.task.transaction;

import java.io.Serializable;

public class TaskExecutionTransactionTemplateConfig implements Serializable {

  private static final long serialVersionUID = 9011212624970034026L;

  private final boolean transactional;

  private final boolean readOnly;

  public TaskExecutionTransactionTemplateConfig() {
    this(true, false);
  }

  public TaskExecutionTransactionTemplateConfig(boolean transactional, boolean readOnly) {
    this.transactional = transactional;
    this.readOnly = readOnly;
  }

  public boolean isTransactional() {
    return transactional;
  }

  public boolean isReadOnly() {
    return readOnly;
  }
}

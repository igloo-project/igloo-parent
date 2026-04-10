package org.iglooproject.spring.notification.model;

import java.util.Objects;
import org.iglooproject.commons.util.context.IExecutionContext;
import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;

public abstract class AbstractExecutionContextNotificationContentDescriptorWrapper
    implements INotificationContentDescriptor {

  private final INotificationContentDescriptor wrapped;

  private final IExecutionContext excecutionContext;

  public AbstractExecutionContextNotificationContentDescriptorWrapper(
      INotificationContentDescriptor wrapped, IExecutionContext excecutionContext) {
    super();
    this.wrapped = wrapped;
    this.excecutionContext = excecutionContext;
  }

  protected IExecutionContext getExecutionContext() {
    return excecutionContext;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof AbstractExecutionContextNotificationContentDescriptorWrapper other)) {
      return false;
    }
    return Objects.equals(wrapped, other.wrapped)
        && Objects.equals(excecutionContext, other.excecutionContext);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wrapped, excecutionContext);
  }

  @Override
  public String renderSubject() throws NotificationContentRenderingException {
    try (ITearDownHandle handle = excecutionContext.open()) {
      return wrapped.renderSubject();
    }
  }

  @Override
  public INotificationContentBody renderBody() throws NotificationContentRenderingException {
    try (ITearDownHandle handle = excecutionContext.open()) {
      return wrapped.renderBody();
    }
  }

  @Override
  public abstract INotificationContentDescriptor withContext(INotificationRecipient recipient);
}

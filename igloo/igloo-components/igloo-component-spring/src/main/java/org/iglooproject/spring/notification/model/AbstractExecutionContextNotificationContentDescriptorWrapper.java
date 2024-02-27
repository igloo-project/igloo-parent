package org.iglooproject.spring.notification.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.iglooproject.commons.util.context.IExecutionContext;
import org.iglooproject.commons.util.context.IExecutionContext.ITearDownHandle;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;

public abstract class AbstractExecutionContextNotificationContentDescriptorWrapper implements INotificationContentDescriptor {

	private final INotificationContentDescriptor wrapped;

	private final IExecutionContext excecutionContext;

	public AbstractExecutionContextNotificationContentDescriptorWrapper(INotificationContentDescriptor wrapped, IExecutionContext excecutionContext) {
		super();
		this.wrapped = wrapped;
		this.excecutionContext = excecutionContext;
	}

	protected IExecutionContext getExecutionContext() {
		return excecutionContext;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractExecutionContextNotificationContentDescriptorWrapper) {
			if (obj == this) {
				return true;
			}
			AbstractExecutionContextNotificationContentDescriptorWrapper other = (AbstractExecutionContextNotificationContentDescriptorWrapper) obj;
			return new EqualsBuilder()
				.append(wrapped, other.wrapped)
				.append(excecutionContext, other.excecutionContext)
				.build();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(wrapped)
			.append(excecutionContext)
			.build();
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

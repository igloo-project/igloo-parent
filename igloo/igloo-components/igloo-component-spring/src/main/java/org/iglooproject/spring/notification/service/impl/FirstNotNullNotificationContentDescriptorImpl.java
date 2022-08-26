package org.iglooproject.spring.notification.service.impl;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;

import com.google.common.collect.ImmutableList;

public class FirstNotNullNotificationContentDescriptorImpl implements INotificationContentDescriptor {
	
	private final Iterable<? extends INotificationContentDescriptor> chainedDescriptors;

	public FirstNotNullNotificationContentDescriptorImpl(
			Iterable<? extends INotificationContentDescriptor> chainedDescriptors) {
		super();
		this.chainedDescriptors = ImmutableList.copyOf(chainedDescriptors);
	}

	@Override
	public String renderSubject() throws NotificationContentRenderingException {
		for (INotificationContentDescriptor descriptor : chainedDescriptors) {
			String subject = descriptor.renderSubject();
			if (subject != null) {
				return subject;
			}
		}
		return null;
	}

	@Override
	public String renderHtmlBody() throws NotificationContentRenderingException {
		for (INotificationContentDescriptor descriptor : chainedDescriptors) {
			String body = descriptor.renderHtmlBody();
			if (body != null) {
				return body;
			}
		}
		return null;
	}

	@Override
	public String renderTextBody() throws NotificationContentRenderingException {
		for (INotificationContentDescriptor descriptor : chainedDescriptors) {
			String body = descriptor.renderTextBody();
			if (body != null) {
				return body;
			}
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FirstNotNullNotificationContentDescriptorImpl) {
			if (obj == this) {
				return true;
			}
			FirstNotNullNotificationContentDescriptorImpl other = (FirstNotNullNotificationContentDescriptorImpl) obj;
			return new EqualsBuilder()
					.append(chainedDescriptors, other.chainedDescriptors)
					.build();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(chainedDescriptors)
				.build();
	}

	@Override
	public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
		ImmutableList.Builder<INotificationContentDescriptor> wrapped = ImmutableList.builder();
		
		for (INotificationContentDescriptor current : chainedDescriptors) {
			wrapped.add(current.withContext(recipient));
		}
		
		return new FirstNotNullNotificationContentDescriptorImpl(wrapped.build());
	}
}

package fr.openwide.core.spring.notification.service.impl;

import java.util.Locale;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.spring.notification.exception.NotificationContentRenderingException;
import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;

public class FirstNotNullNotificationContentDescriptorImpl implements INotificationContentDescriptor {
	
	private final Iterable<? extends INotificationContentDescriptor> chainedDescriptors;

	public FirstNotNullNotificationContentDescriptorImpl(
			Iterable<? extends INotificationContentDescriptor> chainedDescriptors) {
		super();
		this.chainedDescriptors = ImmutableList.copyOf(chainedDescriptors);
	}

	@Override
	public String renderSubject(Locale locale) throws NotificationContentRenderingException {
		for (INotificationContentDescriptor descriptor : chainedDescriptors) {
			String subject = descriptor.renderSubject(locale);
			if (subject != null) {
				return subject;
			}
		}
		return null;
	}

	@Override
	public String renderHtmlBody(Locale locale) throws NotificationContentRenderingException {
		for (INotificationContentDescriptor descriptor : chainedDescriptors) {
			String body = descriptor.renderHtmlBody(locale);
			if (body != null) {
				return body;
			}
		}
		return null;
	}

	@Override
	public String renderTextBody(Locale locale) throws NotificationContentRenderingException {
		for (INotificationContentDescriptor descriptor : chainedDescriptors) {
			String body = descriptor.renderTextBody(locale);
			if (body != null) {
				return body;
			}
		}
		return null;
	}

}

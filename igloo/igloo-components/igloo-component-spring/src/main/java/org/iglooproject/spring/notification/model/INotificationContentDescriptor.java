package org.iglooproject.spring.notification.model;

import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;

public interface INotificationContentDescriptor {
	
	String renderSubject() throws NotificationContentRenderingException;
	
	String renderHtmlBody() throws NotificationContentRenderingException;
	
	String renderTextBody() throws NotificationContentRenderingException;
	
	/**
	 * @param recipient The notification recipient.
	 * @param contextDescriptor The notification content descriptor
	 * @return A notification content descriptor that does the same as <code>this</code> but takes into account
	 * some context relative to the recipient (such as its locale, for instance).
	 * This content descriptor implements {@link #equals(Object)} and {@link #hashCode()} so that the rendering of
	 * single notification for multiple recipients whose context is the same may be executed only once.   
	 */
	INotificationContentDescriptor withContext(INotificationRecipient recipient);

}

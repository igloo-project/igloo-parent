package org.iglooproject.spring.notification.util;

import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.INotificationContentBody;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;
import org.iglooproject.spring.notification.model.NotificationContentBody;

public final class NotificationContentDescriptors {

	private NotificationContentDescriptors() {
	}

	public static INotificationContentDescriptor explicit(String subject, String textBody, String htmlBody) {
		return new ExplicitNotificationContentDecscriptor(subject, textBody, htmlBody);
	}

	private static class ExplicitNotificationContentDecscriptor implements INotificationContentDescriptor {
		
		private final String subject;
		private final String bodyPlainText;
		private final String bodyHtmlText;
		
		public ExplicitNotificationContentDecscriptor(String subject, String bodyPlainText, String bodyHtmlText) {
			super();
			this.subject = subject;
			this.bodyPlainText = bodyPlainText;
			this.bodyHtmlText = bodyHtmlText;
		}
		
		@Override
		public String renderSubject() throws NotificationContentRenderingException {
			return subject;
		}
		
		@Override
		public INotificationContentBody renderBody() throws NotificationContentRenderingException {
			return NotificationContentBody.start()
				.with(o -> {
					o.setPlainText(bodyPlainText);
					o.setHtmlText(bodyHtmlText);
				})
				.build();
		}
		
		@Override
		public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
			return this;
		}
		
	}

}

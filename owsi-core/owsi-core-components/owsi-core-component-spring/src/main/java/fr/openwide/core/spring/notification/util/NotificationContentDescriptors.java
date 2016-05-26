package fr.openwide.core.spring.notification.util;

import fr.openwide.core.spring.notification.exception.NotificationContentRenderingException;
import fr.openwide.core.spring.notification.model.INotificationContentDescriptor;
import fr.openwide.core.spring.notification.model.INotificationRecipient;

public final class NotificationContentDescriptors {

	private NotificationContentDescriptors() {
	}

	public static INotificationContentDescriptor explicit(String subject, String textBody, String htmlBody) {
		return new ExplicitNotificationContentDecscriptor(subject, textBody, htmlBody);
	}
	
	private static class ExplicitNotificationContentDecscriptor implements INotificationContentDescriptor {
		
		private final String subject;
		private final String textBody;
		private final String htmlBody;

		public ExplicitNotificationContentDecscriptor(String subject, String textBody, String htmlBody) {
			super();
			this.subject = subject;
			this.textBody = textBody;
			this.htmlBody = htmlBody;
		}

		@Override
		public String renderSubject() throws NotificationContentRenderingException {
			return subject;
		}

		@Override
		public String renderHtmlBody() throws NotificationContentRenderingException {
			return htmlBody;
		}

		@Override
		public String renderTextBody() throws NotificationContentRenderingException {
			return textBody;
		}

		@Override
		public INotificationContentDescriptor withContext(INotificationRecipient recipient) {
			return this;
		}

	}
}

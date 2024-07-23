package org.iglooproject.spring.notification.util;

import org.iglooproject.mail.api.INotificationRecipient;
import org.iglooproject.spring.notification.exception.NotificationContentRenderingException;
import org.iglooproject.spring.notification.model.INotificationContentDescriptor;

public final class NotificationContentDescriptors {

  private NotificationContentDescriptors() {}

  public static INotificationContentDescriptor explicit(
      String subject, String textBody, String htmlBody) {
    return new ExplicitNotificationContentDecscriptor(subject, textBody, htmlBody);
  }

  private static class ExplicitNotificationContentDecscriptor
      implements INotificationContentDescriptor {

    private final String subject;
    private final String textBody;
    private final String htmlBody;

    public ExplicitNotificationContentDecscriptor(
        String subject, String textBody, String htmlBody) {
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

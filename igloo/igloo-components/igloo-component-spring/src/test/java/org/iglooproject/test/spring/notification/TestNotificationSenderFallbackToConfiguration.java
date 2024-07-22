package org.iglooproject.test.spring.notification;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(
    properties = {
      "notification.mail.sender.behavior=FALLBACK_TO_CONFIGURATION",
      "notification.mail.sender=" + TestNotificationSenderFallbackToConfiguration.CONFIG_SENDER
    })
public class TestNotificationSenderFallbackToConfiguration extends AbstractTestNotification {

  protected static final String CONFIG_SENDER = "Sender Example <sender-config@example.com>";

  @Test
  public void testNotificationSenderUnset() throws ServiceException, MessagingException {
    createNotificationBuilder()
        .from("from@example.com")
        .toAddress("to@example.com")
        .subject("subject")
        .textBody("text")
        .send();
    MimeMessage message = mockitoSend(Mockito.times(1)).getValue();
    Assertions.assertThat(message.getFrom())
        .containsExactly(new InternetAddress("from@example.com"));
    Assertions.assertThat(message.getSender()).isEqualTo(new InternetAddress(CONFIG_SENDER));
  }

  @Test
  public void testNotificationSenderSet() throws ServiceException, MessagingException {
    createNotificationBuilder()
        .sender("sender@example.com")
        .from("from@example.com")
        .toAddress("to@example.com")
        .subject("subject")
        .textBody("text")
        .send();
    MimeMessage message = mockitoSend(Mockito.times(1)).getValue();
    Assertions.assertThat(message.getFrom())
        .containsExactly(new InternetAddress("from@example.com"));
    Assertions.assertThat(message.getSender()).isEqualTo(new InternetAddress("sender@example.com"));
  }

  @Test
  public void testNotificationDefaultFromSenderSet() throws ServiceException, MessagingException {
    createNotificationBuilder()
        .sender("sender@example.com")
        .toAddress("to@example.com")
        .subject("subject")
        .textBody("text")
        .send();
    MimeMessage message = mockitoSend(Mockito.times(1)).getValue();
    Assertions.assertThat(message.getFrom()).containsExactly(new InternetAddress(CONFIG_FROM));
    Assertions.assertThat(message.getSender()).isEqualTo(new InternetAddress("sender@example.com"));
  }

  @Test
  public void testNotificationDefaultFromSenderUnset() throws ServiceException, MessagingException {
    createNotificationBuilder()
        .toAddress("to@example.com")
        .subject("subject")
        .textBody("text")
        .send();
    MimeMessage message = mockitoSend(Mockito.times(1)).getValue();
    Assertions.assertThat(message.getFrom()).containsExactly(new InternetAddress(CONFIG_FROM));
    Assertions.assertThat(message.getSender()).isEqualTo(new InternetAddress(CONFIG_SENDER));
  }
}

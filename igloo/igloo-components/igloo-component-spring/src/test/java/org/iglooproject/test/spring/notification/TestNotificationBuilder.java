package org.iglooproject.test.spring.notification;

import jakarta.mail.Address;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import org.assertj.core.api.Assertions;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.notification.exception.InvalidNotificationTargetException;
import org.iglooproject.spring.notification.service.INotificationBuilderBaseState;
import org.iglooproject.spring.notification.service.NotificationBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class TestNotificationBuilder extends AbstractTestNotification {

  @Test
  void testToCcBccExcept() throws ServiceException {
    INotificationBuilderBaseState builder = createNotificationBuilder();

    builder
        .toAddress(
            "test-to-1@example.com",
            "test-to-2@example.com",
            "mail@îdn.fr",
            "test-to-ignore@example.com")
        .ccAddress(
            "test-to-2@example.com",
            "test-cc-1@example.com",
            "test-cc-2@example.com",
            "test-cc-ignore@example.com")
        .bccAddress(
            "test-to-2@example.com",
            "test-cc-1@example.com",
            "test-bcc-1@example.com",
            "test-bcc-2@example.com",
            "test-bcc-ignore@example.com")
        .exceptAddress(
            "test-to-ignore@example.com",
            "test-cc-ignore@example.com",
            "test-bcc-ignore@example.com")
        .subject("Test notification builder")
        .textBody("Test notification builder")
        .send();
  }

  @Test
  void testInvalidEmail() throws ServiceException {
    INotificationBuilderBaseState builder = createNotificationBuilder();
    Assertions.assertThatThrownBy(
            () ->
                builder
                    .toAddress("pas un mail valide")
                    .subject("Test notification builder")
                    .textBody("Test notification builder")
                    .send())
        .isInstanceOf(InvalidNotificationTargetException.class);
  }

  @Test
  void testSendGroup() throws ServiceException, MessagingException {
    INotificationBuilderBaseState builder = createNotificationBuilder();
    String address1 = "test-to-1@example.com";
    String address2 = "test-to-2@example.com";
    builder.toAddress(address1, address2).subject("subject").textBody("body").send();
    ArgumentCaptor<MimeMessage> argument = mockitoSend(Mockito.times(1));
    MimeMessage mimeMessage = argument.getValue();
    Assertions.assertThat(mimeMessage.getRecipients(RecipientType.TO))
        .hasSize(2)
        .anyMatch(a -> ((InternetAddress) a).getAddress().equals(address1))
        .anyMatch(a -> ((InternetAddress) a).getAddress().equals(address2));
  }

  /**
   * Test added on 09-2018 specifically for {@link NotificationBuilder#sender(String)} behavior in
   * regard to address format.
   *
   * @see InternetAddress
   */
  @Test
  void testSenderRFC822() throws ServiceException, MessagingException {
    createNotificationBuilder()
        .sender("Sender <sender@example.com>")
        .toAddress("to@example.com")
        .subject("subject")
        .textBody("text")
        .send();
    MimeMessage message = mockitoSend(Mockito.times(1)).getValue();
    Address sender = message.getSender();
    Assertions.assertThat(sender).isInstanceOf(InternetAddress.class);
    Assertions.assertThat(((InternetAddress) sender).getAddress()).isEqualTo("sender@example.com");
    Assertions.assertThat(((InternetAddress) sender).getPersonal()).isEqualTo("Sender");
    Assertions.assertThat(((InternetAddress) sender).getGroup(false)).isNull();
  }
}

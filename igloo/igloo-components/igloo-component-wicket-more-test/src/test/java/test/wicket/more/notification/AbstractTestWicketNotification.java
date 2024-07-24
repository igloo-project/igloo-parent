package test.wicket.more.notification;

import jakarta.mail.internet.MimeMessage;
import org.iglooproject.spring.notification.service.INotificationBuilderBaseState;
import org.iglooproject.spring.notification.service.NotificationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import test.wicket.more.AbstractWicketMoreTestCase;

public abstract class AbstractTestWicketNotification extends AbstractWicketMoreTestCase {

  @Autowired private ApplicationContext applicationContext;

  @Autowired private JavaMailSender javaMailSender;

  @BeforeEach
  public void resetJavaMailSenderMock() {
    Mockito.reset(javaMailSender);
    JavaMailSender real = new JavaMailSenderImpl();
    Mockito.when(javaMailSender.createMimeMessage()).then(invocation -> real.createMimeMessage());
  }

  protected INotificationBuilderBaseState createNotificationBuilder() {
    INotificationBuilderBaseState builder = NotificationBuilder.create().init(applicationContext);
    return builder;
  }

  protected ArgumentCaptor<MimeMessage> mockitoSend(VerificationMode verificationMode) {
    ArgumentCaptor<MimeMessage> argument = ArgumentCaptor.forClass(MimeMessage.class);
    Mockito.verify(javaMailSender, verificationMode).send(argument.capture());
    return argument;
  }
}

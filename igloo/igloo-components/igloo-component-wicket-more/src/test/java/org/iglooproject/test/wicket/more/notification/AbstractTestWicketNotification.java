package org.iglooproject.test.wicket.more.notification;

import javax.mail.internet.MimeMessage;

import org.iglooproject.spring.notification.service.INotificationBuilderBaseState;
import org.iglooproject.spring.notification.service.NotificationBuilder;
import org.iglooproject.test.wicket.more.AbstractWicketMoreTestCase;
import org.iglooproject.test.wicket.more.notification.config.spring.WicketMoreTestNotificationWebappConfig;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(inheritLocations = false, classes = WicketMoreTestNotificationWebappConfig.class)
public abstract class AbstractTestWicketNotification extends AbstractWicketMoreTestCase {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private JavaMailSender javaMailSender;

	@Before
	public void resetJavaMailSenderMock() {
		Mockito.reset(javaMailSender);
		JavaMailSender real = new JavaMailSenderImpl();
		Mockito.when(javaMailSender.createMimeMessage()).then((invocation) -> real.createMimeMessage());
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

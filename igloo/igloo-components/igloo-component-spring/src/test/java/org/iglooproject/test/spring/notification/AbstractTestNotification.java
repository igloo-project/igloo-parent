package org.iglooproject.test.spring.notification;

import javax.mail.internet.MimeMessage;

import org.iglooproject.spring.notification.service.INotificationBuilderBaseState;
import org.iglooproject.spring.notification.service.NotificationBuilder;
import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public abstract class AbstractTestNotification {
	
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

	protected JavaMailSender getJavaMailSenderMock() {
		return javaMailSender;
	}

	protected ArgumentCaptor<MimeMessage> mockitoSend(VerificationMode verificationMode) {
		ArgumentCaptor<MimeMessage> argument = ArgumentCaptor.forClass(MimeMessage.class);
		Mockito.verify(javaMailSender, verificationMode).send(argument.capture());
		return argument;
	}

	protected INotificationBuilderBaseState createNotificationBuilder() {
		INotificationBuilderBaseState builder = NotificationBuilder.create().init(applicationContext);
		return builder;
	}

}

package org.iglooproject.test.spring.notification;

import jakarta.mail.internet.MimeMessage;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.spring.notification.service.INotificationBuilderBaseState;
import org.iglooproject.spring.notification.service.NotificationBuilder;
import org.iglooproject.test.spring.notification.spring.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(
		classes = { TestConfig.class },
		initializers = { ExtendedApplicationContextInitializer.class }
)
@TestPropertySource(properties = {
		"notification.mail.sender.behavior=EXPLICIT",
		"notification.mail.from=" + AbstractTestNotification.CONFIG_FROM
})
@ExtendWith(SpringExtension.class)
public abstract class AbstractTestNotification {

	protected static final String CONFIG_FROM = "Example From <from-config@example.com>";

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private JavaMailSender javaMailSender;

	@BeforeEach
	public void resetJavaMailSenderMock() {
		Mockito.reset(javaMailSender);
		JavaMailSender real = new JavaMailSenderImpl();
		Mockito.when(javaMailSender.createMimeMessage()).then(invocation -> real.createMimeMessage());
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

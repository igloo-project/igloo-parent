package org.iglooproject.test.spring.notification;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.notification.service.INotificationBuilderBaseState;
import org.iglooproject.spring.notification.service.NotificationBuilder;
import org.iglooproject.test.spring.notification.spring.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = { TestConfig.class })
public class TestNotificationBuilder {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Test
	@Ignore("Can only be tested manually. Comment the @Ignore annotation when you want to test this.")
	public void testToCcBccExcept() throws ServiceException {
		INotificationBuilderBaseState builder = NotificationBuilder.create().init(applicationContext);
		
		builder.toAddress("test-to-1@example.com", "test-to-2@example.com", "pas un mail valide", "mail@îdn.fr", "test-to-ignore@example.com")
				.ccAddress("test-to-2@example.com", "test-cc-1@example.com", "test-cc-2@example.com", "test-cc-ignore@example.com")
				.bccAddress("test-to-2@example.com", "test-cc-1@example.com", "test-bcc-1@example.com", "test-bcc-2@example.com", "test-bcc-ignore@example.com")
				.exceptAddress("test-to-ignore@example.com", "test-cc-ignore@example.com", "test-bcc-ignore@example.com")
				.subject("Test notification builder")
				.textBody("Test notification builder")
				.send();
	}
	
}

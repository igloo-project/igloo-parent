package fr.openwide.core.test.spring.notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.notification.service.INotificationBuilderBaseState;
import fr.openwide.core.spring.notification.service.NotificationBuilder;
import fr.openwide.core.spring.util.SpringBeanUtils;
import fr.openwide.core.test.spring.notification.spring.config.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(classes = { TestConfig.class })
public class TestNotificationBuilder {
	
	@Autowired
	private ApplicationContext applicationContext;
	
//	@Test
	public void testToCcBccExcept() throws ServiceException {
		INotificationBuilderBaseState builder = NotificationBuilder.create();
		SpringBeanUtils.autowireBean(applicationContext, builder);
		
		builder.toAddress("test-to-1@example.com", "test-to-2@example.com", "pas un mail valide", "mail@îdn.fr", "test-to-ignore@example.com")
				.ccAddress("test-to-2@example.com", "test-cc-1@example.com", "test-cc-2@example.com", "test-cc-ignore@example.com")
				.bccAddress("test-to-2@example.com", "test-cc-1@example.com", "test-bcc-1@example.com", "test-bcc-2@example.com", "test-bcc-ignore@example.com")
				.exceptAddress("test-to-ignore@example.com", "test-cc-ignore@example.com", "test-bcc-ignore@example.com")
				.subject("Test notification builder")
				.textBody("Test notification builder")
				.send();
	}
	
	@Test
	public void emptyDummyTest() {
	}

}

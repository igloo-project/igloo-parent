package org.iglooproject.test.spring.notification;

import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.assertj.core.api.Assertions;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.notification.service.INotificationBuilderBaseState;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.test.spring.notification.spring.config.TestConfig;
import org.iglooproject.test.spring.notification.spring.config.TestFilteredConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(
		classes = { TestConfig.class, TestFilteredConfig.class },
		initializers = { ExtendedApplicationContextInitializer.class }
)
public class TestNotificationFiltered extends AbstractTestNotification {

	@Autowired
	private IPropertyService propertyService;

	@Test
	public void testSendGroup() throws ServiceException, MessagingException {
		INotificationBuilderBaseState builder = createNotificationBuilder();
		String address1 = "test-to-1@example.com";
		String address2 = "test-to-2@example.com";
		builder.toAddress(address1, address2).subject("subject").textBody("body").send();

		ArgumentCaptor<MimeMessage> argument = mockitoSend(Mockito.times(1));
		MimeMessage mimeMessage = argument.getValue();
		List<String> filtered = propertyService.get(SpringPropertyIds.NOTIFICATION_TEST_EMAILS);
		List<InternetAddress> filteredAddress = filtered.stream().map((m) -> 
			{ try { return new InternetAddress(m); } catch (AddressException e) { throw new RuntimeException(e); } }
		).collect(Collectors.toList());
		Assertions.assertThat(mimeMessage.getRecipients(RecipientType.TO)).hasSize(filtered.size())
			.containsExactlyElementsOf(filteredAddress);
	}

}

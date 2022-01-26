package org.iglooproject.test.spring.notification;

import static org.junit.Assert.assertEquals;

import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.iglooproject.spring.property.SpringPropertyIds;
import org.iglooproject.spring.property.service.IPropertyService;
import org.iglooproject.test.spring.notification.spring.config.TestEncodingConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(
		classes = { TestEncodingConfig.class },
		initializers = { ExtendedApplicationContextInitializer.class }
)
public class TestEncoding extends AbstractTestNotification {

	@Autowired
	private IPropertyService propertyService;

	@Test
	public void testEncoding() {
		String subjectPrefix = "[Test Igloo encoding : à é]";
		String subjectPrefixProperty = propertyService.get(SpringPropertyIds.NOTIFICATION_MAIL_SUBJECT_PREFIX);
		assertEquals(subjectPrefix, subjectPrefixProperty);
	}

}

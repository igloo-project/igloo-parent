package org.iglooproject.test.spring.encoding;

import org.assertj.core.api.Assertions;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
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
public class TestEncoding {

	@Value("${encoding}")
	private String compositeUtf8Value;
	@Value("${alone}")
	private String utf8Value;

	@Test
	public void testCompositeEncoding() {
		String subjectPrefix = "[Test Igloo encoding : à é]";
		Assertions.assertThat(compositeUtf8Value).isEqualTo(subjectPrefix);
	}

	@Test
	public void testIndividualEncoding() {
		String subjectPrefix = "[Test Igloo encoding : à é]";
		Assertions.assertThat(utf8Value).isEqualTo(subjectPrefix);
	}

}

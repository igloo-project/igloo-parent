package org.iglooproject.test.spring.encoding;

import org.assertj.core.api.Assertions;
import org.iglooproject.config.bootstrap.spring.ExtendedApplicationContextInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@ContextConfiguration(
    classes = {TestEncodingConfig.class},
    initializers = {ExtendedApplicationContextInitializer.class})
@ExtendWith(SpringExtension.class)
class TestEncoding {

  @Value("${encoding}")
  private String compositeUtf8Value;

  @Value("${alone}")
  private String utf8Value;

  @Test
  void testCompositeEncoding() {
    String subjectPrefix = "[Test Igloo encoding : à é]";
    Assertions.assertThat(compositeUtf8Value).isEqualTo(subjectPrefix);
  }

  @Test
  void testIndividualEncoding() {
    String subjectPrefix = "[Test Igloo encoding : à é]";
    Assertions.assertThat(utf8Value).isEqualTo(subjectPrefix);
  }
}

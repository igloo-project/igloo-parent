package org.iglooproject.test.listener;

import org.iglooproject.test.jpa.spring.IglooTestListenerEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Helper to intercept {@link IglooTestListenerEvent} events. */
@Configuration
public class EventRecorderConfiguration {
  @Bean
  public EventRecorder eventRecorder() {
    return new EventRecorder();
  }
}

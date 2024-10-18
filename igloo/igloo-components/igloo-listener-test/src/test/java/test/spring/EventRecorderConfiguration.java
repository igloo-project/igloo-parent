package test.spring;

import igloo.test.listener.model.IglooTestListenerEvent;
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

package org.iglooproject.test.listener;

import org.assertj.core.api.Assertions;
import org.iglooproject.test.jpa.spring.IglooTestExecutionListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.ApplicationEventsTestExecutionListener;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@TestConfiguration
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({
  // enable dependency injection and events
  ApplicationEventsTestExecutionListener.class,
  DependencyInjectionTestExecutionListener.class,
  // inject our listener
  IglooTestExecutionListener.class
})
@RecordApplicationEvents
class TestIglooExecutionListener {

  @Autowired ApplicationEvents events;

  @Test
  void testEmpty() {
    // just check that listener is here
    Assertions.assertThat(events.stream(IglooTestExecutionListener.IglooTestListenerEvent.class))
        .anyMatch(e -> "loading".equals(e.getSource()));
  }
}

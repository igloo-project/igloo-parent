package test.spring;

import igloo.test.listener.model.IglooTestListenerEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.event.AfterTestExecutionEvent;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.context.event.annotation.AfterTestExecution;

/**
 * Event recorder for testing purpose. spring-test {@link RecordApplicationEvents} mechanism cannot
 * be used as it is registered too late to handle {@link TestExecutionListeners} interception.
 */
public class EventRecorder {
  private List<IglooTestListenerEvent> events = new ArrayList<>();

  @EventListener
  public void onEvent(IglooTestListenerEvent event) {
    events.add(event);
  }

  @AfterTestExecution
  public void onEvent(AfterTestExecutionEvent event) {
    events.clear();
  }

  public List<IglooTestListenerEvent> getEvents() {
    return Collections.unmodifiableList(events);
  }
}

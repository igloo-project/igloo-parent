package test;

import igloo.test.listener.IIglooTestListener;
import igloo.test.listener.IglooAnyTestExecutionListener;
import igloo.test.listener.IglooClassTestExecutionListener;
import igloo.test.listener.IglooExecutionTestExecutionListener;
import igloo.test.listener.IglooMethodTestExecutionListener;
import igloo.test.listener.IglooTestExecutionListener;
import igloo.test.listener.model.IglooTestListenerEvent;
import igloo.test.listener.model.IglooTestListenerType;
import java.lang.StackWalker.StackFrame;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.event.ApplicationEventsTestExecutionListener;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import test.spring.BaseTest;
import test.spring.EventRecorder;
import test.spring.EventRecorderConfiguration;

/**
 * Test general behavior of {@link IglooTestExecutionListener} and {@link IIglooTestListener}
 * (class, method, execution, auto, any listener, before, after, ...)
 */
class TestIglooTestExecutionListener {

  @BaseTest
  @ContextConfiguration(classes = EventRecorderConfiguration.class)
  static class Empty {
    @Autowired EventRecorder eventRecorder;

    @Test
    void testEmpty() {
      // just check that listener is here
      Assertions.assertThat(eventRecorder.getEvents())
          .anyMatch(e -> "loading".equals(e.getSource()));
    }

    @Test
    void testNoHibernateSearch() {
      Assertions.assertThat(eventRecorder.getEvents())
          .noneMatch(e -> "hibernate-search:clean".equals(e.getSource()));
    }
  }

  @BaseTest
  @ContextConfiguration(
      classes = {EventRecorderConfiguration.class, Ordered.CustomConfiguration.class})
  static class Ordered {
    @Autowired EventRecorder eventRecorder;

    /**
     * Check that {@link org.springframework.core.Ordered} / @{@link Order} contracts are applied
     * for {@link IIglooTestListener} ordering.
     *
     * <p>Expected order is that before-listeners are called from highest to lowest precedence, and
     * and after-listeners are called in reverse order.
     *
     * <p>Checks for after-listener are performed by the last registered listener (and not inside
     * test).
     */
    @Test
    void testOrder() {
      List<String> events = List.of("first", "second", "third");
      Assertions.assertThat(
              eventRecorder.getEvents().stream()
                  .map(IglooTestListenerEvent::getSource)
                  .filter(events::contains))
          .containsExactly("first", "second", "third");
    }

    /** Register {@link IIglooTestListener} with various orders. */
    @Configuration
    public static class CustomConfiguration {
      @Order(600)
      @Bean
      public IIglooTestListener third(ApplicationContext context) {
        return IIglooTestListener.beforeAfter((t, c) -> publishEvent(context, "third"));
      }

      @Order(400)
      @Bean
      public IIglooTestListener first(ApplicationContext context) {
        return IIglooTestListener.beforeAfter((t, c) -> publishEvent(context, "first"));
      }

      @Order(500)
      @Bean
      public IIglooTestListener second(ApplicationContext context) {
        return IIglooTestListener.beforeAfter((t, c) -> publishEvent(context, "second"));
      }

      @Order(100)
      @Bean
      public IIglooTestListener last(ApplicationContext context) {
        return IIglooTestListener.afterOnly((t, c) -> checkEvents(context));
      }

      static void publishEvent(ApplicationContext context, String eventName) {
        context.publishEvent(new IglooTestListenerEvent(eventName));
      }

      /**
       * Check in after-method mode if all listeners are called in the expected order. This method
       * is invoked from the last registered listener.
       */
      static void checkEvents(ApplicationContext context) {
        List<String> events = List.of("first", "second", "third");
        Assertions.assertThat(
                context.getBean(EventRecorder.class).getEvents().stream()
                    .map(IglooTestListenerEvent::getSource)
                    .filter(events::contains))
            .containsExactly("first", "second", "third", "third", "second", "first");
      }
    }
  }

  @TestInstance(Lifecycle.PER_CLASS)
  @BaseTest
  @ContextConfiguration(
      classes = {EventRecorderConfiguration.class, AutoClass.CustomConfiguration.class})
  static class AutoClass {
    @Autowired EventRecorder eventRecorder;

    /** Check that listener is called by beforeTestClass / afterTestClass. Done by listener. */
    @Test
    void testAutoClass() {} // NOSONAR

    /** Listener that checks in stack which listener is invoked. */
    @Configuration
    public static class CustomConfiguration {
      @Bean
      public IIglooTestListener checkMethod(ApplicationContext context) {
        return new CheckCalledListenerIglooTestListener("TestClass");
      }
    }
  }

  @TestInstance(Lifecycle.PER_METHOD)
  @BaseTest
  @ContextConfiguration(
      classes = {EventRecorderConfiguration.class, AutoMethod.CustomConfiguration.class})
  static class AutoMethod {
    @Autowired EventRecorder eventRecorder;

    /** Check that listener is called by beforeTestMethod / afterTestMethod. Done by listener. */
    @Test
    void testAutoMethod() {} // NOSONAR

    /** Listener that checks in stack which listener is invoked. */
    @Configuration
    public static class CustomConfiguration {
      @Bean
      public IIglooTestListener checkMethod(ApplicationContext context) {
        return new CheckCalledListenerIglooTestListener("TestMethod");
      }
    }
  }

  @TestInstance(Lifecycle.PER_CLASS)
  @ExtendWith(SpringExtension.class)
  @TestExecutionListeners({
    // enable dependency injection and events
    ApplicationEventsTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    // inject our listener
    IglooMethodTestExecutionListener.class
  })
  @ContextConfiguration(
      classes = {EventRecorderConfiguration.class, ManualMethod.CustomConfiguration.class})
  static class ManualMethod {
    @Autowired EventRecorder eventRecorder;

    /** Check that listener is called by beforeTestMethod / afterTestMethod. Done by listener. */
    @Test
    void testManualMethod() {} // NOSONAR

    /** Listener that checks in stack which listener is invoked. */
    @Configuration
    public static class CustomConfiguration {
      @Bean
      public IIglooTestListener checkMethod(ApplicationContext context) {
        return new CheckCalledListenerIglooTestListener("TestMethod");
      }
    }
  }

  @TestInstance(Lifecycle.PER_METHOD)
  @ExtendWith(SpringExtension.class)
  @TestExecutionListeners({
    // enable dependency injection and events
    ApplicationEventsTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    // inject our listener
    IglooClassTestExecutionListener.class
  })
  @ContextConfiguration(
      classes = {EventRecorderConfiguration.class, ManualClass.CustomConfiguration.class})
  static class ManualClass {
    @Autowired EventRecorder eventRecorder;

    /** Check that listener is called by beforeTestMethod / afterTestMethod. Done by listener. */
    @Test
    void testManualClass() {} // NOSONAR

    /** Listener that checks in stack which listener is invoked. */
    @Configuration
    public static class CustomConfiguration {
      @Bean
      public IIglooTestListener checkMethod(ApplicationContext context) {
        return new CheckCalledListenerIglooTestListener("TestClass");
      }
    }
  }

  @TestInstance(Lifecycle.PER_METHOD)
  @ExtendWith(SpringExtension.class)
  @TestExecutionListeners({
    // enable dependency injection and events
    ApplicationEventsTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    // inject our listener
    IglooExecutionTestExecutionListener.class
  })
  @ContextConfiguration(
      classes = {EventRecorderConfiguration.class, ManualExecution.CustomConfiguration.class})
  static class ManualExecution {
    @Autowired EventRecorder eventRecorder;

    /**
     * Check that listener is called by beforeTestExecution / afterTestExecution. Done by listener.
     */
    @Test
    void testManualExecution() {} // NOSONAR

    /** Listener that checks in stack which listener is invoked. */
    @Configuration
    public static class CustomConfiguration {
      @Bean
      public IIglooTestListener checkMethod(ApplicationContext context) {
        return new CheckCalledListenerIglooTestListener("TestExecution");
      }
    }
  }

  @ExtendWith(SpringExtension.class)
  @TestExecutionListeners({
    // enable dependency injection and events
    ApplicationEventsTestExecutionListener.class,
    DependencyInjectionTestExecutionListener.class,
    // inject our listener
    IglooAnyTestExecutionListener.class
  })
  @ContextConfiguration(
      classes = {EventRecorderConfiguration.class, ManualAny.CustomConfiguration.class})
  static class ManualAny {
    @Autowired EventRecorder eventRecorder;

    /** Check that all (class, method, execution) listeners are called. Done by listener. */
    @Test
    void testManualAny() {} // NOSONAR

    /**
     * Listener that produces events and listener that checks that all expected listeners are
     * called.
     */
    @Configuration
    public static class CustomConfiguration {
      @Autowired private EventRecorder eventRecorder;

      /** Publish event named from phase. */
      @Bean
      @Order(100)
      public IIglooTestListener publishEvents(ApplicationEventPublisher eventPublisher) {
        return IIglooTestListener.beforeAfter(
            (p, t) -> eventPublisher.publishEvent(new IglooTestListenerEvent(p.name())));
      }

      /** Check in after class that all expected events are published. */
      @Bean
      @Order(0)
      public IIglooTestListener checkMethod() {
        return IIglooTestListener.afterOnly(
            (p, t) -> {
              // only check on class
              if (IglooTestListenerType.CLASS.equals(p)) {
                Assertions.assertThat(
                        eventRecorder.getEvents().stream().map(IglooTestListenerEvent::getSource))
                    .containsExactly(
                        "loading", "CLASS", "METHOD", "EXECUTION", "EXECUTION", "METHOD", "CLASS");
              }
            });
      }
    }
  }

  /** Check if given stackFrame is the expected {@link IglooTestExecutionListener} method */
  private static boolean matchMethodName(StackFrame stackFrame, String methodName) {
    return stackFrame.getClassName().equals(IglooTestExecutionListener.class.getName())
        && stackFrame.getMethodName().equals(methodName);
  }

  /**
   * Utility {@link IIglooTestListener} that checks which listener is invoked. Used to check {@link
   * IglooTestListenerType#AUTO} behavior.
   */
  private static class CheckCalledListenerIglooTestListener implements IIglooTestListener {
    private final String methodName;

    public CheckCalledListenerIglooTestListener(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public void before(IglooTestListenerType type, TestContext context) {
      Assertions.assertThatList(StackWalker.getInstance().walk(f -> f.limit(10).toList()))
          .anyMatch(i -> TestIglooTestExecutionListener.matchMethodName(i, "before" + methodName));
    }

    @Override
    public void after(IglooTestListenerType type, TestContext context) {
      Assertions.assertThatList(StackWalker.getInstance().walk(f -> f.limit(10).toList()))
          .anyMatch(i -> TestIglooTestExecutionListener.matchMethodName(i, "after" + methodName));
    }
  }
}

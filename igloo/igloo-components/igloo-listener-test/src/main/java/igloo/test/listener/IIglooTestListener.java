package igloo.test.listener;

import igloo.test.listener.model.IglooTestListenerType;
import org.springframework.test.context.TestContext;

public interface IIglooTestListener {
  /**
   * Method called for before listeners.
   *
   * <p>{@code type} can be ignored if listener relies on {@link IglooTestExecutionListener}
   * filtering, or can be used to perform custom action for each step.
   *
   * <p>{@link TestContext} is passed as provided by Spring API.
   */
  default void before(IglooTestListenerType type, TestContext context) {}

  /**
   * Method called for after listeners.
   *
   * <p>{@code type} can be ignored if listener relies on {@link IglooTestExecutionListener}
   * filtering, or can be used to perform custom action for each step.
   *
   * <p>{@link TestContext} is passed as provided by Spring API.
   */
  default void after(IglooTestListenerType type, TestContext context) {}

  /**
   * This method is used so that {@link IglooTestExecutionListener} can filter expected beans among
   * all {@link IIglooTestListener}. By default, we match {@code default} name.
   *
   * <p>Using a custom name implies to create a custom {@link IglooTestExecutionListener} with the
   * appropriate name.
   *
   * @see IglooTestExecutionListener
   */
  default boolean match(String name) {
    return "default".equals(name);
  }

  @FunctionalInterface
  interface BeforeOnlyIglooTestListener extends IIglooTestListener {
    @Override
    void before(IglooTestListenerType type, TestContext context);
  }

  @FunctionalInterface
  interface AfterOnlyIglooTestListener extends IIglooTestListener {
    @Override
    void after(IglooTestListenerType type, TestContext context);
  }

  @FunctionalInterface
  interface BeforeAfterOnlyIglooTestListener extends IIglooTestListener {
    @Override
    default void after(IglooTestListenerType type, TestContext context) {
      handle(type, context);
    }

    @Override
    default void before(IglooTestListenerType type, TestContext context) {
      handle(type, context);
    }

    void handle(IglooTestListenerType type, TestContext context);
  }

  /** Build a {@link IIglooTestListener} from a functional interface. */
  public static IIglooTestListener beforeOnly(BeforeOnlyIglooTestListener handler) {
    return handler;
  }

  /** Build a {@link IIglooTestListener} from a functional interface. */
  public static IIglooTestListener afterOnly(AfterOnlyIglooTestListener handler) {
    return handler;
  }

  /** Build a {@link IIglooTestListener} from a functional interface. */
  public static IIglooTestListener beforeAfter(BeforeAfterOnlyIglooTestListener handler) {
    return handler;
  }
}

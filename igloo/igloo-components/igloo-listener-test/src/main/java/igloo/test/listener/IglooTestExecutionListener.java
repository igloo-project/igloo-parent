package igloo.test.listener;

import static igloo.test.listener.model.IglooTestListenerType.CLASS;
import static igloo.test.listener.model.IglooTestListenerType.EXECUTION;
import static igloo.test.listener.model.IglooTestListenerType.METHOD;

import com.google.common.collect.Lists;
import igloo.test.listener.model.IglooTestListenerEvent;
import igloo.test.listener.model.IglooTestListenerType;
import java.util.List;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;

/**
 * Call {@link IIglooTestListener} beans matching provided {@code name}, ordered by {@link Ordered}
 * contract. before-test items are called by precedence (highest to lowest) and after-test are
 * called in reverse order.
 *
 * <p>To customize listener behavior, you can:
 *
 * <ul>
 *   <li>Keep this default provider, and use your own {@link IIglooTestListener}. You need to match
 *       {@code default} in {@link IIglooTestListener#match} method. Igloo default {@link
 *       IIglooTestListener} may be present (behavior based on context configuration).
 *   <li>Create a new implementation inheriting this class but using a custom name (see {@link
 *       IglooTestExecutionListener} constructor), and the matching {@link IIglooTestListener}.
 * </ul>
 *
 * <p>Default setup installs a listener before {@link SqlScriptsTestExecutionListener} (to handle
 * database cleaning) and after {@link SqlScriptsTestExecutionListener} (to handle reindexation).
 */
public class IglooTestExecutionListener implements TestExecutionListener, Ordered {

  public static final Integer ORDER_DATABASE = 1000;
  public static final Integer ORDER_CACHE = 1100;
  public static final Integer ORDER_HSEARCH = 1300;

  /**
   * Name used to select {@link IIglooTestListener} beans.
   *
   * @see IIglooTestListener#match(String)
   */
  private final String name;

  /** Order among TestExecutionListener. */
  private final Integer order;

  /**
   * Type of before/after invocation.
   *
   * @see IglooTestListenerType#CLASS
   * @see IglooTestListenerType#METHOD
   * @see IglooTestListenerType#EXECUTION
   * @see IglooTestListenerType#ANY
   */
  private final IglooTestListenerType type;

  /**
   * Default implementation that selects {@code default}-matching {@link IIglooTestListener}.
   *
   * @see IglooTestExecutionListener#name
   */
  public IglooTestExecutionListener() {
    // default order : after SqlScriptsTestExecutionListener
    this(5000 - 100, "default", IglooTestListenerType.AUTO);
  }

  /**
   * This constructor allows to build custom {@link TestExecutionListener} that matches a customized
   * {@code name}. Use this if you need to use a custom set of {@link IIglooTestListener}.
   */
  protected IglooTestExecutionListener(Integer order, String name, IglooTestListenerType type) {
    this.order = order;
    this.name = name;
    this.type = type;
  }

  @Override
  public void beforeTestClass(TestContext testContext) throws Exception {
    if (getType(testContext).match(CLASS)) {
      getIglooTestListener(testContext).forEach(l -> l.before(CLASS, testContext));
    }
  }

  @Override
  public void beforeTestExecution(TestContext testContext) throws Exception {
    if (getType(testContext).match(EXECUTION)) {
      getIglooTestListener(testContext).forEach(l -> l.before(EXECUTION, testContext));
    }
  }

  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {
    if (getType(testContext).match(METHOD)) {
      getIglooTestListener(testContext).forEach(l -> l.before(METHOD, testContext));
    }
  }

  @Override
  public void afterTestClass(TestContext testContext) throws Exception {
    if (getType(testContext).match(CLASS)) {
      Lists.reverse(getIglooTestListener(testContext)).forEach(l -> l.after(CLASS, testContext));
    }
  }

  @Override
  public void afterTestExecution(TestContext testContext) throws Exception {
    if (getType(testContext).match(EXECUTION)) {
      Lists.reverse(getIglooTestListener(testContext))
          .forEach(l -> l.after(EXECUTION, testContext));
    }
  }

  @Override
  public void afterTestMethod(TestContext testContext) throws Exception {
    if (getType(testContext).match(METHOD)) {
      Lists.reverse(getIglooTestListener(testContext)).forEach(l -> l.after(METHOD, testContext));
    }
  }

  /**
   * Load {@link IglooTestExecutionListener} from context, filtering object by qualifier and
   * ordering by {@link Ordered} contract
   */
  private List<IIglooTestListener> getIglooTestListener(TestContext testContext) {
    @SuppressWarnings("unchecked")
    List<IIglooTestListener> listeners = (List<IIglooTestListener>) testContext.getAttribute(key());
    if (listeners == null) {
      // for testing purpose
      testContext.getApplicationContext().publishEvent(new IglooTestListenerEvent("loading"));
      // result is an ordered map; we keep beans ordered
      listeners =
          testContext
              .getApplicationContext()
              .getAutowireCapableBeanFactory()
              .createBean(Holder.class)
              .listeners
              .stream()
              .filter(b -> b.match(name))
              .toList();
      testContext.setAttribute(key(), listeners);
    }
    return listeners;
  }

  public IglooTestListenerType getType(TestContext context) {
    if (IglooTestListenerType.AUTO.equals(type)) {
      return TestInstance.Lifecycle.PER_METHOD.equals(loadTestInstanceLifecycle(context))
          ? METHOD
          : CLASS;
    } else {
      return type;
    }
  }

  /** Retrieve {@link TestInstance} configuration, fallback to system property setting */
  private TestInstance.Lifecycle loadTestInstanceLifecycle(TestContext testContext) {
    return AnnotationUtils.findAnnotation(testContext.getTestClass(), TestInstance.class, true)
        .map(TestInstance::value)
        .orElseGet(this::loadTestInstanceLifecycleSystemProperty);
  }

  /** Retrieve {@link TestInstance.Lifecycle} */
  private TestInstance.Lifecycle loadTestInstanceLifecycleSystemProperty() {
    String setting = System.getProperty(TestInstance.Lifecycle.DEFAULT_LIFECYCLE_PROPERTY_NAME);
    if (setting == null || setting.trim().length() == 0) {
      return TestInstance.Lifecycle.PER_METHOD;
    }
    try {
      return TestInstance.Lifecycle.valueOf(setting);
    } catch (IllegalArgumentException e) {
      return TestInstance.Lifecycle.PER_METHOD;
    }
  }

  /** {@link TestContext} attribute key */
  private String key() {
    return "__IGLOO__" + name;
  }

  /** Order among listeners. */
  @Override
  public int getOrder() {
    return order;
  }

  /**
   * We need an object to fetch ordered listeners collection from spring (there is no method on
   * {@link ApplicationContext} to perform an ordered lookup).
   */
  public static class Holder {
    private final List<IIglooTestListener> listeners;

    public Holder(List<IIglooTestListener> listeners) {
      this.listeners = listeners;
    }
  }
}

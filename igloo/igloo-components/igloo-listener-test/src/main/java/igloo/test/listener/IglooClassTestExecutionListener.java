package igloo.test.listener;

import igloo.test.listener.model.IglooTestListenerType;
import org.springframework.test.context.TestExecutionListener;

/**
 * {@link TestExecutionListener} invoking {@link IIglooTestListener} only for before/after class.
 * Default implementation uses {@code default}-matching {@link IIglooTestListener}. Custom
 * constructor allows to create a subclass using a custom matching name.
 */
public class IglooClassTestExecutionListener extends IglooTestExecutionListener {
  public IglooClassTestExecutionListener() {
    this(5000 - 100, "default");
  }

  protected IglooClassTestExecutionListener(Integer order, String name) {
    super(order, name, IglooTestListenerType.CLASS);
  }
}

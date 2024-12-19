package igloo.test.listener;

import igloo.test.listener.model.IglooTestListenerType;
import org.springframework.test.context.TestExecutionListener;

/**
 * {@link TestExecutionListener} invoking {@link IIglooTestListener} for all listeners. Default
 * implementation uses {@code default}-matching {@link IIglooTestListener}. Custom constructor
 * allows to create a subclass using a custom matching name.
 */
public class IglooAnyTestExecutionListener extends IglooTestExecutionListener {
  public IglooAnyTestExecutionListener() {
    this(5000 - 100, "default");
  }

  protected IglooAnyTestExecutionListener(Integer order, String name) {
    super(order, name, IglooTestListenerType.ANY);
  }
}
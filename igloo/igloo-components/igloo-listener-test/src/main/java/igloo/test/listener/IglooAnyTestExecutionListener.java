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
    this("default");
  }

  protected IglooAnyTestExecutionListener(String name) {
    super(name, IglooTestListenerType.ANY);
  }
}

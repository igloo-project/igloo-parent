package igloo.test.listener;

import igloo.test.listener.model.IglooTestListenerType;
import org.springframework.test.context.TestExecutionListener;

/**
 * {@link TestExecutionListener} invoking {@link IIglooTestListener} only for before/after method.
 * Default implementation uses {@code default}-matching {@link IIglooTestListener}. Custom
 * constructor allows to create a subclass using a custom matching name.
 */
public class IglooMethodTestExecutionListener extends IglooTestExecutionListener {
  public IglooMethodTestExecutionListener() {
    this(5000 - 100, "default");
  }

  protected IglooMethodTestExecutionListener(Integer order, String name) {
    super(order, name, IglooTestListenerType.METHOD);
  }
}

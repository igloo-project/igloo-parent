package org.iglooproject.test.jpa.spring;

import org.springframework.test.context.TestExecutionListener;

/**
 * {@link TestExecutionListener} invoking {@link IIglooTestListener} only for before/after
 * execution. Default implementation uses {@code default}-matching {@link IIglooTestListener}.
 * Custom constructor allows to create a subclass using a custom matching name.
 */
public class IglooExecutionTestExecutionListener extends IglooTestExecutionListener {
  public IglooExecutionTestExecutionListener() {
    this("default");
  }

  protected IglooExecutionTestExecutionListener(String name) {
    super(name, IglooTestListenerType.EXECUTION);
  }
}

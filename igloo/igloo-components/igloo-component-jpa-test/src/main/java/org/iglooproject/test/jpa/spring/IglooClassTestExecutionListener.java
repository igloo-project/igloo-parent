package org.iglooproject.test.jpa.spring;

import org.springframework.test.context.TestExecutionListener;

/**
 * {@link TestExecutionListener} invoking {@link IIglooTestListener} only for before/after class.
 * Default implementation uses {@code default}-matching {@link IIglooTestListener}. Custom
 * constructor allows to create a subclass using a custom matching name.
 */
public class IglooClassTestExecutionListener extends IglooTestExecutionListener {
  public IglooClassTestExecutionListener() {
    this("default");
  }

  protected IglooClassTestExecutionListener(String name) {
    super(name, IglooTestListenerType.CLASS);
  }
}

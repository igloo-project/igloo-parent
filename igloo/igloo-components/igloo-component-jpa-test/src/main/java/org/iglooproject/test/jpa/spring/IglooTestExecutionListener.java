package org.iglooproject.test.jpa.spring;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class IglooTestExecutionListener implements TestExecutionListener {

  private final String name;

  public IglooTestExecutionListener() {
    this("default");
  }

  public IglooTestExecutionListener(String name) {
    this.name = name;
  }

  @Override
  public void beforeTestClass(TestContext testContext) throws Exception {
    getIglooTestListener(testContext)
        .forEach(l -> l.perform(IglooTestExecutionPhase.BEFORE_CLASS, testContext));
  }

  @Override
  public void beforeTestExecution(TestContext testContext) throws Exception {
    getIglooTestListener(testContext)
        .forEach(l -> l.perform(IglooTestExecutionPhase.BEFORE_EXECUTION, testContext));
  }

  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {
    getIglooTestListener(testContext)
        .forEach(l -> l.perform(IglooTestExecutionPhase.BEFORE_METHOD, testContext));
  }

  @Override
  public void afterTestClass(TestContext testContext) throws Exception {
    getIglooTestListener(testContext)
        .forEach(l -> l.perform(IglooTestExecutionPhase.AFTER_CLASS, testContext));
  }

  @Override
  public void afterTestExecution(TestContext testContext) throws Exception {
    getIglooTestListener(testContext)
        .forEach(l -> l.perform(IglooTestExecutionPhase.AFTER_EXECUTION, testContext));
  }

  @Override
  public void afterTestMethod(TestContext testContext) throws Exception {
    getIglooTestListener(testContext)
        .forEach(l -> l.perform(IglooTestExecutionPhase.AFTER_METHOD, testContext));
  }

  private List<IIglooTestListener> getIglooTestListener(TestContext testContext) {
    @SuppressWarnings("unchecked")
    List<IIglooTestListener> listeners = (List<IIglooTestListener>) testContext.getAttribute(key());
    if (listeners == null) {
      // for testing purpose
      testContext.getApplicationContext().publishEvent(new IglooTestExecutionListener("loading"));
      // result is an ordered map; we keep beans ordered
      listeners =
          new ArrayList<>(
              BeanFactoryAnnotationUtils.qualifiedBeansOfType(
                      testContext.getApplicationContext(), IIglooTestListener.class, name)
                  .values());
    }
    return listeners;
  }

  public static final class IglooTestListenerEvent extends ApplicationEvent {
    private static final long serialVersionUID = 1L;

    public IglooTestListenerEvent(String value) {
      super(value);
    }

    @Override
    public String getSource() {
      return (String) super.getSource();
    }
  }

  private String key() {
    return "__IGLOO__" + name;
  }
}

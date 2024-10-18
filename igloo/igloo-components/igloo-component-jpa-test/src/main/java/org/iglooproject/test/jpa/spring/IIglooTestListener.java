package org.iglooproject.test.jpa.spring;

import org.springframework.test.context.TestContext;

public interface IIglooTestListener {
  void perform(IglooTestExecutionPhase phase, TestContext context);
}

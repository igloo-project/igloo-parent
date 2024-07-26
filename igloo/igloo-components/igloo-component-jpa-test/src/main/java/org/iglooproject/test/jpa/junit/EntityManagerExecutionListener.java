package org.iglooproject.test.jpa.junit;

import org.iglooproject.jpa.util.EntityManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class EntityManagerExecutionListener extends AbstractTestExecutionListener {

  @Autowired private EntityManagerUtils entityManagerUtils;

  @Override
  public void beforeTestClass(TestContext testContext) throws Exception {
    refreshBean(testContext);
  }

  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {
    entityManagerUtils.openEntityManager();
  }

  @Override
  public void afterTestMethod(TestContext testContext) throws Exception {
    entityManagerUtils.closeEntityManager();
  }

  private void refreshBean(TestContext testContext) {
    testContext.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
  }
}

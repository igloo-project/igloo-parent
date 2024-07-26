package org.iglooproject.test.search.tests;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.search.config.spring.JpaSearchTestConfig;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = JpaSearchTestConfig.class, inheritInitializers = true)
public abstract class AbstractJpaSearchTestCase extends AbstractTestCase {

  @Override
  protected void cleanAll() throws ServiceException, SecurityServiceException {
    // no-op
  }
}

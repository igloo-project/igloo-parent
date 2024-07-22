package test.jpa.more.business;

import static org.assertj.core.api.Assumptions.assumeThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Test;
import test.jpa.more.business.entity.model.TestEntity;
import test.jpa.more.config.spring.SpringBootTestJpaMore;

@SpringBootTestJpaMore
class TestHibernateInterceptor extends AbstractJpaMoreTestCase {

  @Test
  void testHibernateInterceptor() throws ServiceException, SecurityServiceException {
    // TODO igloo-boot - retrieve TestEntityInterceptor and add it to hibernate context and restore
    // this tests
    // and other deleted tests.
    assumeThat(false).isTrue();
    TestEntity entity1 = new TestEntity("entity1");
    testEntityService.create(entity1);

    assertEquals("interceptor", entity1.getClassicInterceptorSave());
    assertEquals(null, entity1.getClassicInterceptorFlushDirty());

    entity1.setSimplePropertyUpdate("test");
    testEntityService.update(entity1);

    assertEquals("interceptor", entity1.getClassicInterceptorFlushDirty());

    assertEquals("interceptor", entity1.getSimplePropertyUpdateInterceptor());

    TestEntity entity2 = new TestEntity("entity2");
    entity2.setSimplePropertyUpdate("test");
    testEntityService.create(entity2);

    entity2.setSimplePropertyUpdate("test");
    entity2.setLabel("entity2-new");
    testEntityService.update(entity2);

    assertNull(entity2.getSimplePropertyUpdateInterceptor());
  }
}

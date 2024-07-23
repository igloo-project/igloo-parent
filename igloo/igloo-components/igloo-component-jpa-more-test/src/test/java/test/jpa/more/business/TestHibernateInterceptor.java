package test.jpa.more.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.jupiter.api.Test;
import test.jpa.more.business.entity.model.TestEntity;

class TestHibernateInterceptor extends AbstractJpaMoreTestCase {

  @Test
  void testHibernateInterceptor() throws ServiceException, SecurityServiceException {
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

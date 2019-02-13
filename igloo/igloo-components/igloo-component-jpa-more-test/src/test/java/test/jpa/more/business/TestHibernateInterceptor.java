package test.jpa.more.business;

import org.junit.Assert;
import org.junit.Test;

import test.jpa.more.business.entity.model.TestEntity;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;

public class TestHibernateInterceptor extends AbstractJpaMoreTestCase {
	
	@Test
	public void testHibernateInterceptor() throws ServiceException, SecurityServiceException {
		TestEntity entity1 = new TestEntity("entity1");
		testEntityService.create(entity1);
		
		Assert.assertEquals("interceptor", entity1.getClassicInterceptorSave());
		Assert.assertEquals(null, entity1.getClassicInterceptorFlushDirty());
		
		entity1.setSimplePropertyUpdate("test");
		testEntityService.update(entity1);
		
		Assert.assertEquals("interceptor", entity1.getClassicInterceptorFlushDirty());
		
		Assert.assertEquals("interceptor", entity1.getSimplePropertyUpdateInterceptor());
		
		TestEntity entity2 = new TestEntity("entity2");
		entity2.setSimplePropertyUpdate("test");
		testEntityService.create(entity2);
		
		entity2.setSimplePropertyUpdate("test");
		entity2.setLabel("entity2-new");
		testEntityService.update(entity2);
		
		Assert.assertNull(entity2.getSimplePropertyUpdateInterceptor());
		
	}

}

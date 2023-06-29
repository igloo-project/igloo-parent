package test.jpa.more.business.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.spring.property.dao.IMutablePropertyDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.jpa.more.business.AbstractJpaMoreTestCase;
import test.jpa.more.config.spring.SpringBootTestJpaMore;

@SpringBootTestJpaMore
class TestMutablePropertyDao extends AbstractJpaMoreTestCase {
	
	private static final String KEY = "property.string.value";

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Test
	void mutableProperty() throws ServiceException, SecurityServiceException {
		assertNull(mutablePropertyDao.getInTransaction(KEY));
		mutablePropertyDao.setInTransaction(KEY, "MyValue");
		assertEquals("MyValue", mutablePropertyDao.getInTransaction(KEY));
		mutablePropertyDao.setInTransaction(KEY, null);
		assertNull(mutablePropertyDao.getInTransaction(KEY));
	}

}

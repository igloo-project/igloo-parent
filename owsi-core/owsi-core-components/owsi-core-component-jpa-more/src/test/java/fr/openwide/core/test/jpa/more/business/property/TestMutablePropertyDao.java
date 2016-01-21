package fr.openwide.core.test.jpa.more.business.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.spring.property.dao.IMutablePropertyDao;
import fr.openwide.core.test.jpa.more.business.AbstractJpaMoreTestCase;

public class TestMutablePropertyDao extends AbstractJpaMoreTestCase {
	
	private static final String KEY = "property.string.value";

	@Autowired
	private IMutablePropertyDao mutablePropertyDao;

	@Test
	public void mutableProperty() throws ServiceException, SecurityServiceException {
		assertNull(mutablePropertyDao.getInTransaction(KEY));
		mutablePropertyDao.setInTransaction(KEY, "MyValue");
		assertEquals("MyValue", mutablePropertyDao.getInTransaction(KEY));
		mutablePropertyDao.setInTransaction(KEY, null);
		assertNull(mutablePropertyDao.getInTransaction(KEY));
	}

}

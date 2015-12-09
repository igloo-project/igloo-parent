package fr.openwide.core.test.jpa.more.business.property;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.more.business.property.dao.IImmutablePropertyDao;
import fr.openwide.core.test.jpa.more.business.AbstractJpaMoreTestCase;

public class TestImmutablePropertyDao extends AbstractJpaMoreTestCase {

	@Autowired
	private IImmutablePropertyDao immutablePropertyDao;

	@Test
	public void immutableProperty() {
		Assert.assertEquals("MyValue", immutablePropertyDao.get("property.string.value"));
		Assert.assertEquals("1", immutablePropertyDao.get("property.long.value"));
		Assert.assertEquals(null, immutablePropertyDao.get("property.long.value2"));
	}

}

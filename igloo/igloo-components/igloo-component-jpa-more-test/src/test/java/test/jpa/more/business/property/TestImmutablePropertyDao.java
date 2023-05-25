package test.jpa.more.business.property;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.iglooproject.spring.property.dao.IImmutablePropertyDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import test.jpa.more.business.AbstractJpaMoreTestCase;
import test.jpa.more.config.spring.SpringBootTestJpaMore;

@SpringBootTestJpaMore
@TestPropertySource(properties = {
	"property.string.value=MyValue",
	"property.long.value=1"
})
class TestImmutablePropertyDao extends AbstractJpaMoreTestCase {

	@Autowired
	private IImmutablePropertyDao immutablePropertyDao;

	@Test
	void immutableProperty() {
		assertEquals("MyValue", immutablePropertyDao.get("property.string.value"));
		assertEquals("1", immutablePropertyDao.get("property.long.value"));
		assertEquals(null, immutablePropertyDao.get("property.long.value2"));
	}

}

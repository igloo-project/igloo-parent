package org.iglooproject.test.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hibernate.internal.SessionImpl;
import org.hibernate.stat.Statistics;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.company.model.Company;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.config.spring.SpringBootTestJpaCache;
import org.junit.jupiter.api.Test;

@SpringBootTestJpaCache
class TestCache extends AbstractJpaCoreTestCase {
	
	@Test
	void testCache() throws ServiceException, SecurityServiceException {
		// cf ehcache-hibernate-company.xml
		
		getStatistics().setStatisticsEnabled(true);
		getStatistics().clear();
		Company company = new Company("Company Test Persist");
		Person person = new Person("Persist", "Numéro");
		companyService.create(company);
		personService.create(person);
		
		entityManagerReset();
		company = companyService.getById(company.getId());
		
		assertEquals(1, getStatistics().getSecondLevelCacheHitCount());
		assertEquals(0, getStatistics().getSecondLevelCacheMissCount());
		
		entityManagerReset();
		company = companyService.getById(company.getId());
		
		assertEquals(2, getStatistics().getSecondLevelCacheHitCount());
		assertEquals(0, getStatistics().getSecondLevelCacheMissCount());
		
		entityManagerReset();
		company = companyService.getById(company.getId());
		
		assertEquals(3, getStatistics().getSecondLevelCacheHitCount());
		assertEquals(0, getStatistics().getSecondLevelCacheMissCount());
		
		personService.getById(person.getId());
		
		// l'entité Person n'a pas l'annotation @Cache donc ne provoque pas de cache hit
		assertEquals(3, getStatistics().getSecondLevelCacheHitCount());
		assertEquals(0, getStatistics().getSecondLevelCacheMissCount());
	}
	
	protected Statistics getStatistics() {
		return ((SessionImpl) getEntityManager().getDelegate()).getSessionFactory().getStatistics();
	}
}

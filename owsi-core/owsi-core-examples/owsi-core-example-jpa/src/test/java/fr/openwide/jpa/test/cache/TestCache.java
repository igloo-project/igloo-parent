package fr.openwide.jpa.test.cache;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.impl.SessionImpl;
import org.hibernate.stat.Statistics;
import org.junit.Test;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.jpa.example.business.company.model.Company;
import fr.openwide.jpa.example.business.person.model.Person;
import fr.openwide.jpa.test.AbstractJpaTestCase;

public class TestCache extends AbstractJpaTestCase {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	@Test
	public void testCache() throws ServiceException, SecurityServiceException {
		// ce test, s'il est activé, fait exploser les tests des cascades.
		// il n'est donc pas annoté, donc pas lancé.
		
		getStatistics().setStatisticsEnabled(true);
		getStatistics().clear();
		Company company = new Company("Company Test Persist");
		Person person = new Person("Persist", "Numéro");
		companyService.create(company);
		personService.create(person);
		
		entityManagerUtils.closeEntityManager();
		entityManagerUtils.openEntityManager();
		company = companyService.getById(company.getId());
		entityManagerUtils.closeEntityManager();
		entityManagerUtils.openEntityManager();
		company = companyService.getById(company.getId());
		
		assertEquals(2, getStatistics().getSecondLevelCacheHitCount());
		assertEquals(0, getStatistics().getSecondLevelCacheMissCount());
		
		entityManagerUtils.closeEntityManager();
		entityManagerUtils.openEntityManager();
		company = companyService.getById(company.getId());
		
		assertEquals(3, getStatistics().getSecondLevelCacheHitCount());
		assertEquals(0, getStatistics().getSecondLevelCacheMissCount());
		
		personService.getById(person.getId());
		
		// l'entité Person n'a pas l'annotation @Cache donc ne provoque pas de cache hit
		assertEquals(3, getStatistics().getSecondLevelCacheHitCount());
		assertEquals(0, getStatistics().getSecondLevelCacheMissCount());
	}
	
	protected Statistics getStatistics() {
		return ((SessionImpl) entityManager.getDelegate()).getSessionFactory().getStatistics();
	}
}

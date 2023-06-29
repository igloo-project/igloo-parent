package org.iglooproject.test.transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.hibernate.LazyInitializationException;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.company.model.Company;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.util.service.ServiceExceptionService;
import org.iglooproject.test.config.spring.SpringBootTestJpaOnly;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Teste la bonne prise en compte des exceptions sur les transactions.
 *
 * Il est important que l'exécution des tests de cette classe soit hors transaction.
 *
 * @author Open Wide
 */
@SpringBootTestJpaOnly
class TestTransactionService extends AbstractJpaCoreTestCase {
	
	@Autowired
	private ServiceExceptionService serviceExceptionService;
	
	@Test
	void testRollbackOnServiceException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwServiceException();
		} catch (ServiceException e) {
		}

		assertEquals(1, serviceExceptionService.size());
	}

	@Test
	void testRollbackOnServiceInheritedException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwServiceInheritedException();
		} catch (ServiceException e) {}

		assertEquals(1, serviceExceptionService.size());
	}
	
	@Test
	void testRollbackOnUncheckedException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwUncheckedException();
		} catch (IllegalStateException e) {
		}

		assertEquals(1, serviceExceptionService.size());
	}
	
	@Test
	void testCommitOnCheckedException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwCheckedException();
			fail(String.format("Exception {} obligatoire", CheckedException.class.getName()));
		} catch (CheckedException e) {
		}

		assertEquals(2, serviceExceptionService.size());
	}

	@Test
	void testReloadOnRollback() throws ServiceException, SecurityServiceException {
		Company company = new Company("test");
		Person person = createPerson("Person", "Test");
		company.addEmployee1(person);
		companyService.create(company);
		
		//On ouvre une nouvelle session pour que les objets ne soient plus liés à la session
		entityManagerClear();
		
		//On recharge seulement la Company
		company = companyService.getById(company.getId());
		
		try {
			serviceExceptionService.throwServiceInheritedException();
			fail("La méthode précédente se finit en exception");
		}
		catch (ServiceException e) {}
		
		try {
			company.getEmployees1().get(0);
			fail("Faire une opération sur un objet après un rollback lève une LazyInitializationException " +
			"car l'objet n'est plus lié à la session");
		} catch (LazyInitializationException e) {}
		
		//Il faut recharger l'objet après l'exception pour pouvoir agir dessus
		company = companyService.getById(company.getId());
		company.getEmployees1().get(0);
	}
}

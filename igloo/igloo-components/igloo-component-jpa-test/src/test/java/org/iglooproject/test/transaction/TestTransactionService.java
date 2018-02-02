package org.iglooproject.test.transaction;

import org.hibernate.LazyInitializationException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.test.AbstractJpaCoreTestCase;
import org.iglooproject.test.business.company.model.Company;
import org.iglooproject.test.business.person.model.Person;
import org.iglooproject.test.business.util.service.ServiceExceptionService;


/**
 * Teste la bonne prise en compte des exceptions sur les transactions.
 *
 * Il est important que l'exécution des tests de cette classe soit hors transaction.
 *
 * @author Open Wide
 */
public class TestTransactionService extends AbstractJpaCoreTestCase {
	
	@Autowired
	private ServiceExceptionService serviceExceptionService;
	
	@Test
	public void testRollbackOnServiceException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		Assert.assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwServiceException();
		} catch (ServiceException e) {
		}

		Assert.assertEquals(1, serviceExceptionService.size());
	}

	@Test
	public void testRollbackOnServiceInheritedException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		Assert.assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwServiceInheritedException();
		} catch (ServiceException e) {}

		Assert.assertEquals(1, serviceExceptionService.size());
	}
	
	@Test
	public void testRollbackOnUncheckedException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		Assert.assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwUncheckedException();
		} catch (IllegalStateException e) {
		}

		Assert.assertEquals(1, serviceExceptionService.size());
	}

	@Test
	public void testReloadOnRollback() throws ServiceException, SecurityServiceException {
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
			Assert.fail("La méthode précédente se finit en exception");
		}
		catch (ServiceException e) {}
		
		try {
			company.getEmployees1().get(0);
			Assert.fail("Faire une opération sur un objet après un rollback lève une LazyInitializationException " +
			"car l'objet n'est plus lié à la session");
		} catch (LazyInitializationException e) {}
		
		//Il faut recharger l'objet après l'exception pour pouvoir agir dessus
		company = companyService.getById(company.getId());
		company.getEmployees1().get(0);
	}
}

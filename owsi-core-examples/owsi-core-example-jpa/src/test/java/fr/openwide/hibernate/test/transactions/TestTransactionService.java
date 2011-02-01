package fr.openwide.hibernate.test.transactions;

import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.hibernate.exception.SecurityServiceException;
import fr.openwide.core.hibernate.exception.ServiceException;
import fr.openwide.hibernate.example.business.company.model.Company;
import fr.openwide.hibernate.example.business.person.model.Person;
import fr.openwide.hibernate.example.business.util.service.ServiceExceptionService;
import fr.openwide.hibernate.test.AbstractHibernateTestCase;


/**
 * Teste la bonne prise en compte des exceptions sur les transactions.
 *
 * Il est important que l'exécution des tests de cette classe soit hors transaction.
 *
 * @author Open Wide
 */
public class TestTransactionService extends AbstractHibernateTestCase {
	
	@Autowired
	private ServiceExceptionService serviceExceptionService;
	
	@Test
	public void testRollbackOnServiceException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwServiceException();
		} catch (ServiceException e) {
		}

		assertEquals(1, serviceExceptionService.size());
	}

	@Test
	public void testRollbackOnServiceInheritedException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwServiceInheritedException();
		} catch (ServiceException e) {}

		assertEquals(1, serviceExceptionService.size());
	}
	
	@Test
	public void testRollbackOnUncheckedException() throws ServiceException, SecurityServiceException {
		serviceExceptionService.dontThrow();

		assertEquals(1, serviceExceptionService.size());

		try {
			serviceExceptionService.throwUncheckedException();
		} catch (IllegalStateException e) {
		}

		assertEquals(1, serviceExceptionService.size());
	}

	@Test
	public void testReloadOnRollback() throws ServiceException, SecurityServiceException {
		Company company = new Company("test");
		Person person = createPerson("Person", "Test");
		company.addEmployee1(person);
		companyService.create(company);
		
		//On ouvre une nouvelle session pour que les objets ne soient plus liés à la session
		hibernateSessionUtils.closeSession();
		hibernateSessionUtils.initSession();
		
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
	
	@Before
	public void init() throws ServiceException, SecurityServiceException {
		super.init();
	}
	
	@After
	public void close() throws ServiceException, SecurityServiceException {
		super.close();
	}
}

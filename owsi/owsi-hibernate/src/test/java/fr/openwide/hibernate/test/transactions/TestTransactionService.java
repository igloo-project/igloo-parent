package fr.openwide.hibernate.test.transactions;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.hibernate.business.util.service.ServiceExceptionServiceImpl;
import fr.openwide.hibernate.exception.SecurityServiceException;
import fr.openwide.hibernate.exception.ServiceException;
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
	ServiceExceptionServiceImpl serviceExceptionService;
	
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
	@Ignore
	public void testRollbackOnTCPServerException() throws ServiceException, SecurityServiceException {
	    serviceExceptionService.dontThrow();
	   
	    assertEquals(1, serviceExceptionService.size());
	   
	    try {
	        serviceExceptionService.throwTCPServerException();
	    } catch (ServiceException e) {
	    }
	   
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
	
	@Before
	public void init() throws ServiceException, SecurityServiceException {
	    super.init();
	}
	
	@After
	public void close() {
	    super.close();
	}
}

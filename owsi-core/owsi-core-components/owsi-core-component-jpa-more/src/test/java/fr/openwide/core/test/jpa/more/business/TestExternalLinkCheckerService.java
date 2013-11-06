package fr.openwide.core.test.jpa.more.business;

import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkStatus;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;
import fr.openwide.core.jpa.more.business.link.service.IExternalLinkCheckerService;
import fr.openwide.core.jpa.util.EntityManagerUtils;

public class TestExternalLinkCheckerService extends AbstractJpaMoreTestCase {
	
	@Autowired
	private IExternalLinkCheckerService externalLinkCheckerService;
	
	@Autowired
	private EntityManagerUtils entityManagerUtils;
	
	@Test
	public void testExternalLinkCheckerService() throws ServiceException, SecurityServiceException {
		Calendar beforeCalendar = Calendar.getInstance();
		beforeCalendar.add(Calendar.SECOND, -10);
		Date beforeDate = beforeCalendar.getTime();
		
		Long id1 = null;
		Long id2 = null;
		Long id3 = null;
		
		{
			ExternalLinkWrapper externalLink1 = new ExternalLinkWrapper("http://www.google.fr/");
			externalLinkWrapperService.create(externalLink1);
			id1 = externalLink1.getId();
			
			ExternalLinkWrapper externalLink2 = new ExternalLinkWrapper("http://zzz.totototototo.zzz/totoz/");
			externalLinkWrapperService.create(externalLink2);
			id2 = externalLink2.getId();
			
			ExternalLinkWrapper externalLink3 = new ExternalLinkWrapper("http://www.google.fr/tititototata");
			externalLinkWrapperService.create(externalLink3);
			id3 = externalLink3.getId();
		}
		
		{
			externalLinkCheckerService.checkAllLinks();
		}
		
		entityManagerUtils.getEntityManager().clear();
		
		{
			ExternalLinkWrapper externalLink1 = externalLinkWrapperService.getById(id1);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink1.getStatus());
			Assert.assertEquals(0, externalLink1.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink1.getLastStatusCode());
			Assert.assertTrue(externalLink1.getLastCheckDate().after(beforeDate));
			
			ExternalLinkWrapper externalLink2 = externalLinkWrapperService.getById(id2);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink2.getStatus());
			Assert.assertEquals(1, externalLink2.getConsecutiveFailures());
			Assert.assertNull(externalLink2.getLastStatusCode());
			Assert.assertTrue(externalLink2.getLastCheckDate().after(beforeDate));
			
			ExternalLinkWrapper externalLink3 = externalLinkWrapperService.getById(id3);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink3.getStatus());
			Assert.assertEquals(1, externalLink3.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_NOT_FOUND), externalLink3.getLastStatusCode());
			Assert.assertTrue(externalLink3.getLastCheckDate().after(beforeDate));
		}
	}

}

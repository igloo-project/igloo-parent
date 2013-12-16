package fr.openwide.core.test.jpa.more.business;

import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.jpa.more.business.link.model.ExternalLinkStatus;
import fr.openwide.core.jpa.more.business.link.model.ExternalLinkWrapper;
import fr.openwide.core.jpa.more.business.link.service.IExternalLinkCheckerService;
import fr.openwide.core.jpa.util.EntityManagerUtils;
import fr.openwide.core.test.jpa.more.config.spring.ExternalLinkCheckerTestConfig;

@ContextConfiguration(classes = ExternalLinkCheckerTestConfig.class)
public class TestExternalLinkCheckerService extends AbstractJpaMoreTestCase {
	
	@Autowired
	private IExternalLinkCheckerService externalLinkCheckerService;
	
	@Autowired
	private EntityManagerUtils entityManagerUtils;
	
	@Test
	public void testExternalLinkCheckerService() throws Exception {
		
		Long id1 = null;
		Long id2 = null;
		Long id3 = null;
		Long id4 = null;
		
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
			
			ExternalLinkWrapper externalLink4 = new ExternalLinkWrapper("http://zzz.totototototo.zzz/totoz/");
			externalLinkWrapperService.create(externalLink4);
			id4 = externalLink4.getId();
		}
		
		Calendar beforeFirstBatchCalendar = Calendar.getInstance();
		beforeFirstBatchCalendar.add(Calendar.SECOND, -10);
		Date beforeFirstBatchDate = beforeFirstBatchCalendar.getTime();
		
		{
			externalLinkCheckerService.checkBatch();
		}
		
		entityManagerUtils.getEntityManager().clear();
		
		{
			ExternalLinkWrapper externalLink1 = externalLinkWrapperService.getById(id1);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink1.getStatus());
			Assert.assertEquals(0, externalLink1.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink1.getLastStatusCode());
			Assert.assertTrue(externalLink1.getLastCheckDate().after(beforeFirstBatchDate));

			// Should not have been checked yet: not part of the first batch of only two URLs
			ExternalLinkWrapper externalLink2 = externalLinkWrapperService.getById(id2);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink2.getStatus());
			Assert.assertEquals(0, externalLink2.getConsecutiveFailures());
			Assert.assertNull(externalLink2.getLastStatusCode());
			Assert.assertNull(externalLink2.getLastCheckDate());
			
			ExternalLinkWrapper externalLink3 = externalLinkWrapperService.getById(id3);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink3.getStatus());
			Assert.assertEquals(1, externalLink3.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_NOT_FOUND), externalLink3.getLastStatusCode());
			Assert.assertTrue(externalLink3.getLastCheckDate().after(beforeFirstBatchDate));
			
			// Same URL as externalLink2, so this link should carry the same data
			ExternalLinkWrapper externalLink4 = externalLinkWrapperService.getById(id4);
			Assert.assertEquals(externalLink2.getStatus(), externalLink4.getStatus());
			Assert.assertEquals(externalLink2.getConsecutiveFailures(), externalLink4.getConsecutiveFailures());
			Assert.assertEquals(externalLink2.getLastStatusCode(), externalLink4.getLastStatusCode());
			Assert.assertEquals(externalLink2.getLastCheckDate(), externalLink2.getLastCheckDate());
		}
		
		Date beforeSecondBatchDate = Calendar.getInstance().getTime();
		
		{
			externalLinkCheckerService.checkBatch();
		}
		
		entityManagerUtils.getEntityManager().clear();
		
		{
			// Should have been checked for the second time
			ExternalLinkWrapper externalLink1 = externalLinkWrapperService.getById(id1);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink1.getStatus());
			Assert.assertEquals(0, externalLink1.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink1.getLastStatusCode());
			Assert.assertTrue(externalLink1.getLastCheckDate().after(beforeSecondBatchDate));

			// Should have been checked for the first time
			ExternalLinkWrapper externalLink2 = externalLinkWrapperService.getById(id2);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink2.getStatus());
			Assert.assertEquals(1, externalLink2.getConsecutiveFailures());
			Assert.assertNull(externalLink2.getLastStatusCode());
			Assert.assertTrue(externalLink2.getLastCheckDate().after(beforeSecondBatchDate));

			// Should have been left as-is (not part of the second batch)
			ExternalLinkWrapper externalLink3 = externalLinkWrapperService.getById(id3);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink3.getStatus());
			Assert.assertEquals(1, externalLink3.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_NOT_FOUND), externalLink3.getLastStatusCode());
			Assert.assertTrue(externalLink3.getLastCheckDate().before(beforeSecondBatchDate));
			
			// Same URL as externalLink2, so this link should carry the same data
			ExternalLinkWrapper externalLink4 = externalLinkWrapperService.getById(id4);
			Assert.assertEquals(externalLink2.getStatus(), externalLink4.getStatus());
			Assert.assertEquals(externalLink2.getConsecutiveFailures(), externalLink4.getConsecutiveFailures());
			Assert.assertEquals(externalLink2.getLastStatusCode(), externalLink4.getLastStatusCode());
			Assert.assertEquals(externalLink2.getLastCheckDate(), externalLink2.getLastCheckDate());
		}
	}

}

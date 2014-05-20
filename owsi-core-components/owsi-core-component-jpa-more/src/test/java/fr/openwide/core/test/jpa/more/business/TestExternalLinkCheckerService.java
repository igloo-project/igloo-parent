package fr.openwide.core.test.jpa.more.business;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.jpa.more.business.link.model.ExternalLinkErrorType;
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
		Long id5 = null;
		Long id6 = null;
		Long id7 = null;
		Long id8 = null;
		Long id9 = null;
		
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
			
			ExternalLinkWrapper externalLink5 = new ExternalLinkWrapper("http://location-gîte-ardeche.fr/index.html");
			externalLinkWrapperService.create(externalLink5);
			id5 = externalLink5.getId();
			
			ExternalLinkWrapper externalLink6 = new ExternalLinkWrapper("http: http://example.com/");
			externalLinkWrapperService.create(externalLink6);
			id6 = externalLink6.getId();
			
			ExternalLinkWrapper externalLink7 = new ExternalLinkWrapper("http://62.210.184.140/V2/Partenaires/00043/Images/Chalet 95/dheilly003.JPG");
			externalLinkWrapperService.create(externalLink7);
			id7 = externalLink7.getId();
			
			ExternalLinkWrapper externalLink8 = new ExternalLinkWrapper("http://hotel.reservit.com/reservit/avail-info.php?hotelid=104461&userid=4340d8abb651c8ca20e6cd57a844f5708354&__utma=1.804870725.1361370840.1361370840.1361370840.1&__utmc=1&__utmz=1.1361370840.1.1.utmcsr=%28direct%29|utmccn=%28direct%29|utmcmd=%28none%29");
			externalLinkWrapperService.create(externalLink8);
			id8 = externalLink8.getId();
			
			ExternalLinkWrapper externalLink9 = new ExternalLinkWrapper("http://translate.googleusercontent.com/translate_c?client=tmpg&depth=1&hl=en&langpair=fr|en&rurl=translate.google.com&u=http://www.agence-bellemontagne.com/index.php%3Foption%3Dcom_content%26view%3Darticle%26id%3D49%26Itemid%3D51&usg=ALkJrhgFkVHObqq4-hABxmDFkFE00p369A");
			externalLinkWrapperService.create(externalLink9);
			id9 = externalLink9.getId();
		}
		
		Date beforeFirstBatchDate = new Date();
		Thread.sleep(1000); // Make sure the checkDate will not be exactly the same
		
		{
			externalLinkCheckerService.checkBatch();
		}
		
		entityManagerUtils.getEntityManager().clear();
		
		{
			ExternalLinkWrapper externalLink1 = externalLinkWrapperService.getById(id1);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink1.getStatus());
			Assert.assertEquals(0, externalLink1.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink1.getLastStatusCode());
			Assert.assertNull(externalLink1.getLastErrorType());
			Assert.assertTrue(externalLink1.getLastCheckDate().after(beforeFirstBatchDate));

			ExternalLinkWrapper externalLink2 = externalLinkWrapperService.getById(id2);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink2.getStatus());
			Assert.assertEquals(1, externalLink2.getConsecutiveFailures());
			Assert.assertNull(externalLink2.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.UNKNOWN_HTTPCLIENT_ERROR, externalLink2.getLastErrorType());
			Assert.assertTrue(externalLink2.getLastCheckDate().after(beforeFirstBatchDate));
			
			ExternalLinkWrapper externalLink3 = externalLinkWrapperService.getById(id3);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink3.getStatus());
			Assert.assertEquals(1, externalLink3.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_NOT_FOUND), externalLink3.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.HTTP, externalLink3.getLastErrorType());
			Assert.assertTrue(externalLink3.getLastCheckDate().after(beforeFirstBatchDate));
			
			// Same URL as externalLink2, so this link should carry the same data
			ExternalLinkWrapper externalLink4 = externalLinkWrapperService.getById(id4);
			Assert.assertEquals(externalLink2.getStatus(), externalLink4.getStatus());
			Assert.assertEquals(externalLink2.getConsecutiveFailures(), externalLink4.getConsecutiveFailures());
			Assert.assertEquals(externalLink2.getLastStatusCode(), externalLink4.getLastStatusCode());
			Assert.assertEquals(externalLink2.getLastErrorType(), externalLink4.getLastErrorType());
			Assert.assertEquals(externalLink2.getLastCheckDate(), externalLink2.getLastCheckDate());
			
			// IDN
			ExternalLinkWrapper externalLink5 = externalLinkWrapperService.getById(id5);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink5.getStatus());
			Assert.assertEquals(0, externalLink5.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink5.getLastStatusCode());
			Assert.assertNull(externalLink5.getLastErrorType());
			Assert.assertTrue(externalLink5.getLastCheckDate().after(beforeFirstBatchDate));
			
			// Invalid URL
			ExternalLinkWrapper externalLink6 = externalLinkWrapperService.getById(id6);
			Assert.assertEquals(ExternalLinkStatus.DEAD_LINK, externalLink6.getStatus());
			Assert.assertEquals(0, externalLink6.getConsecutiveFailures());
			Assert.assertNull(externalLink6.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.URI_SYNTAX, externalLink6.getLastErrorType());
			Assert.assertTrue(externalLink5.getLastCheckDate().after(beforeFirstBatchDate));
			
			// Link with non escaped space
			ExternalLinkWrapper externalLink7 = externalLinkWrapperService.getById(id7);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink7.getStatus());
			Assert.assertEquals(0, externalLink7.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink7.getLastStatusCode());
			Assert.assertNull(externalLink7.getLastErrorType());
			Assert.assertTrue(externalLink7.getLastCheckDate().after(beforeFirstBatchDate));
			
			ExternalLinkWrapper externalLink8 = externalLinkWrapperService.getById(id8);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink8.getStatus());
			Assert.assertEquals(0, externalLink8.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink8.getLastStatusCode());
			Assert.assertNull(externalLink8.getLastErrorType());
			Assert.assertTrue(externalLink8.getLastCheckDate().after(beforeFirstBatchDate));
			
			ExternalLinkWrapper externalLink9 = externalLinkWrapperService.getById(id9);
			Assert.assertEquals(ExternalLinkStatus.IGNORED, externalLink9.getStatus());
			Assert.assertEquals(0, externalLink9.getConsecutiveFailures());
			Assert.assertNull(externalLink9.getLastErrorType());
			Assert.assertTrue(externalLink9.getLastCheckDate().after(beforeFirstBatchDate));
		}
		
		Date beforeSecondBatchDate = new Date();
		Thread.sleep(1000); // Make sure the checkDate will not be exactly the same
		
		{
			externalLinkCheckerService.checkBatch();
		}
		
		entityManagerUtils.getEntityManager().clear();
		
		{
			ExternalLinkWrapper externalLink1 = externalLinkWrapperService.getById(id1);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink1.getStatus());
			Assert.assertEquals(0, externalLink1.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink1.getLastStatusCode());
			Assert.assertNull(externalLink1.getLastErrorType());
			Assert.assertTrue(externalLink1.getLastCheckDate().after(beforeSecondBatchDate));

			ExternalLinkWrapper externalLink2 = externalLinkWrapperService.getById(id2);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink2.getStatus());
			Assert.assertEquals(2, externalLink2.getConsecutiveFailures());
			Assert.assertNull(externalLink2.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.UNKNOWN_HTTPCLIENT_ERROR, externalLink2.getLastErrorType());
			Assert.assertTrue(externalLink2.getLastCheckDate().after(beforeSecondBatchDate));
			
			ExternalLinkWrapper externalLink3 = externalLinkWrapperService.getById(id3);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink3.getStatus());
			Assert.assertEquals(2, externalLink3.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_NOT_FOUND), externalLink3.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.HTTP, externalLink3.getLastErrorType());
			Assert.assertTrue(externalLink3.getLastCheckDate().after(beforeSecondBatchDate));
			
			// Same URL as externalLink2, so this link should carry the same data
			ExternalLinkWrapper externalLink4 = externalLinkWrapperService.getById(id4);
			Assert.assertEquals(externalLink2.getStatus(), externalLink4.getStatus());
			Assert.assertEquals(externalLink2.getConsecutiveFailures(), externalLink4.getConsecutiveFailures());
			Assert.assertEquals(externalLink2.getLastStatusCode(), externalLink4.getLastStatusCode());
			Assert.assertEquals(externalLink2.getLastErrorType(), externalLink4.getLastErrorType());
			Assert.assertEquals(externalLink2.getLastCheckDate(), externalLink2.getLastCheckDate());
			
			// IDN
			ExternalLinkWrapper externalLink5 = externalLinkWrapperService.getById(id5);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink5.getStatus());
			Assert.assertEquals(0, externalLink5.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink5.getLastStatusCode());
			Assert.assertNull(externalLink5.getLastErrorType());
			Assert.assertTrue(externalLink5.getLastCheckDate().after(beforeSecondBatchDate));
			
			// Invalid URL
			ExternalLinkWrapper externalLink6 = externalLinkWrapperService.getById(id6);
			Assert.assertEquals(ExternalLinkStatus.DEAD_LINK, externalLink6.getStatus());
			Assert.assertEquals(0, externalLink6.getConsecutiveFailures());
			Assert.assertNull(externalLink6.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.URI_SYNTAX, externalLink6.getLastErrorType());
			Assert.assertTrue(externalLink5.getLastCheckDate().after(beforeFirstBatchDate));
			
			// Link with non escaped space
			ExternalLinkWrapper externalLink7 = externalLinkWrapperService.getById(id7);
			Assert.assertEquals(ExternalLinkStatus.ONLINE, externalLink7.getStatus());
			Assert.assertEquals(0, externalLink7.getConsecutiveFailures());
			Assert.assertEquals(Integer.valueOf(HttpStatus.SC_OK), externalLink7.getLastStatusCode());
			Assert.assertNull(externalLink7.getLastErrorType());
			Assert.assertTrue(externalLink7.getLastCheckDate().after(beforeSecondBatchDate));
			
			// This link should have been ignored
			ExternalLinkWrapper externalLink9 = externalLinkWrapperService.getById(id9);
			Assert.assertEquals(ExternalLinkStatus.IGNORED, externalLink9.getStatus());
			Assert.assertEquals(0, externalLink9.getConsecutiveFailures());
			Assert.assertNull(externalLink9.getLastErrorType());
			Assert.assertTrue(externalLink9.getLastCheckDate().after(beforeFirstBatchDate));
		}
	}

}

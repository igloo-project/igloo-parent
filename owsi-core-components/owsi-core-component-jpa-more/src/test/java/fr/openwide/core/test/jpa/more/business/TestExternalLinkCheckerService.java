package fr.openwide.core.test.jpa.more.business;

import java.util.Date;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import fr.openwide.core.jpa.exception.SecurityServiceException;
import fr.openwide.core.jpa.exception.ServiceException;
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
		Long id10 = null;
		Long id11 = null;
		Long id12 = null;
		Long id13 = null;
		
		{
			id1 = createLink("http://www.google.fr/");
			
			id2 = createLink("http://zzz.totototototo.zzz/totoz/");
			
			id3 = createLink("http://www.google.fr/tititototata");
			
			id4 = createLink("http://zzz.totototototo.zzz/totoz/");
			
			id5 = createLink("http://location-gîte-ardeche.fr/index.html");
			
			id6 = createLink("http: http://example.com/");
			
			id7 = createLink("http://62.210.184.140/V2/Partenaires/00043/Images/Chalet 95/dheilly003.JPG");
			
			id8 = createLink("http://hotel.reservit.com/reservit/avail-info.php?hotelid=104461&userid=4340d8abb651c8ca20e6cd57a844f5708354&__utma=1.804870725.1361370840.1361370840.1361370840.1&__utmc=1&__utmz=1.1361370840.1.1.utmcsr=%28direct%29|utmccn=%28direct%29|utmcmd=%28none%29");
			
			id9 = createLink("http://translate.googleusercontent.com/translate_c?client=tmpg&depth=1&hl=en&langpair=fr|en&rurl=translate.google.com&u=http://www.agence-bellemontagne.com/index.php%3Foption%3Dcom_content%26view%3Darticle%26id%3D49%26Itemid%3D51&usg=ALkJrhgFkVHObqq4-hABxmDFkFE00p369A");
			
			id10 = createLink("http://drome-hotel.for-system.com/index.aspx?Globales/ListeIdFournisseur=20716");
			
			id11 = createLink("http://cyclosyennois.free.fr/");
			
			id12 = createLink("http://lacroisee26.com/");
			
			id13 = createLink("http://ledauphine.com/");
		}
		
		Date beforeFirstBatchDate = new Date();
		Thread.sleep(1000); // Make sure the checkDate will not be exactly the same
		
		{
			externalLinkCheckerService.checkBatch();
		}
		
		entityManagerUtils.getEntityManager().clear();
		
		{
			checkStatusOK(id1, beforeFirstBatchDate);

			ExternalLinkWrapper externalLink2 = externalLinkWrapperService.getById(id2);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink2.getStatus());
			Assert.assertEquals(1, externalLink2.getConsecutiveFailures());
			Assert.assertNull(externalLink2.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.IO, externalLink2.getLastErrorType());
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
			
			checkStatusOK(id5, beforeFirstBatchDate);
			
			// Invalid URL
			ExternalLinkWrapper externalLink6 = externalLinkWrapperService.getById(id6);
			Assert.assertEquals(ExternalLinkStatus.DEAD_LINK, externalLink6.getStatus());
			Assert.assertEquals(0, externalLink6.getConsecutiveFailures());
			Assert.assertNull(externalLink6.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.URI_SYNTAX, externalLink6.getLastErrorType());
			Assert.assertTrue(externalLink6.getLastCheckDate().after(beforeFirstBatchDate));
			
			checkStatusOK(id7, beforeFirstBatchDate);
			checkStatusOK(id8, beforeFirstBatchDate);
			checkStatusOK(id9, beforeFirstBatchDate);
			checkStatusOK(id10, beforeFirstBatchDate);
			checkStatusOK(id11, beforeFirstBatchDate);
			checkStatusOK(id12, beforeFirstBatchDate);
			checkStatusOK(id13, beforeFirstBatchDate);
		}
		
		Date beforeSecondBatchDate = new Date();
		Thread.sleep(1000); // Make sure the checkDate will not be exactly the same
		
		{
			externalLinkCheckerService.checkBatch();
		}
		
		entityManagerUtils.getEntityManager().clear();
		
		{
			checkStatusOK(id1, beforeSecondBatchDate);

			ExternalLinkWrapper externalLink2 = externalLinkWrapperService.getById(id2);
			Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink2.getStatus());
			Assert.assertEquals(2, externalLink2.getConsecutiveFailures());
			Assert.assertNull(externalLink2.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.IO, externalLink2.getLastErrorType());
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
			
			checkStatusOK(id5, beforeSecondBatchDate);
			
			// Invalid URL
			ExternalLinkWrapper externalLink6 = externalLinkWrapperService.getById(id6);
			Assert.assertEquals(ExternalLinkStatus.DEAD_LINK, externalLink6.getStatus());
			Assert.assertEquals(0, externalLink6.getConsecutiveFailures());
			Assert.assertNull(externalLink6.getLastStatusCode());
			Assert.assertEquals(ExternalLinkErrorType.URI_SYNTAX, externalLink6.getLastErrorType());
			Assert.assertTrue(externalLink6.getLastCheckDate().after(beforeFirstBatchDate));
			
			checkStatusOK(id7, beforeSecondBatchDate);
		}
	}
	
	private Long createLink(String url) throws ServiceException, SecurityServiceException {
		ExternalLinkWrapper externalLink = new ExternalLinkWrapper(url);
		externalLinkWrapperService.create(externalLink);
		return externalLink.getId();
	}
	
	private void checkStatusOK(Long id8, Date beforeFirstBatchDate) {
		checkStatus(id8, beforeFirstBatchDate,
				ExternalLinkStatus.ONLINE, 0, Integer.valueOf(HttpStatus.SC_OK), null);
	}

	private void checkStatus(Long id8, Date beforeFirstBatchDate,
			ExternalLinkStatus externalLinkStatus,
			int consecutiveFailures,
			Integer httpStatus,
			ExternalLinkErrorType errorType) {
		ExternalLinkWrapper externalLink8 = externalLinkWrapperService.getById(id8);
		Assert.assertEquals(externalLinkStatus, externalLink8.getStatus());
		Assert.assertEquals(consecutiveFailures, externalLink8.getConsecutiveFailures());
		Assert.assertEquals(httpStatus, externalLink8.getLastStatusCode());
		Assert.assertEquals(errorType, externalLink8.getLastErrorType());
		Assert.assertTrue(externalLink8.getLastCheckDate().after(beforeFirstBatchDate));
	}

}

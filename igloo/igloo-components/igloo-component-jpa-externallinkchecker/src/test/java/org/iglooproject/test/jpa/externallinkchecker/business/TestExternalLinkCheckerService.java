package org.iglooproject.test.jpa.externallinkchecker.business;

import java.util.Date;

import javax.ws.rs.core.UriBuilder;

import org.apache.http.HttpStatus;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkErrorType;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkStatus;
import org.iglooproject.jpa.externallinkchecker.business.model.ExternalLinkWrapper;
import org.iglooproject.jpa.externallinkchecker.business.service.IExternalLinkCheckerService;
import org.iglooproject.jpa.externallinkchecker.business.service.IExternalLinkWrapperService;
import org.iglooproject.jpa.util.EntityManagerUtils;
import org.iglooproject.test.jpa.externallinkchecker.business.rest.server.MockServlet;
import org.iglooproject.test.jpa.externallinkchecker.config.spring.JpaExternalLinkCheckerTestConfig;
import org.iglooproject.test.jpa.junit.AbstractTestCase;
import org.iglooproject.test.web.context.MockRestServer;
import org.iglooproject.test.web.context.MockServletTestExecutionListener;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

@TestExecutionListeners(listeners = MockServletTestExecutionListener.class)
@MockRestServer
@ContextConfiguration(classes = JpaExternalLinkCheckerTestConfig.class)
public class TestExternalLinkCheckerService extends AbstractTestCase {

	@Autowired
	private IExternalLinkCheckerService externalLinkCheckerService;

	@Autowired
	private EntityManagerUtils entityManagerUtils;

	@Autowired
	protected IExternalLinkWrapperService externalLinkWrapperService;
	
	@Autowired
	private MockServlet server;
	
	private UriBuilder getUriBuilder() {
		return UriBuilder.fromUri(server.getRestUri());
	}
	
	private String toMockUrl(String path) {
		toMockUrl(path, null);
		return getUriBuilder().path(path).toString();
	}
	
	private String toMockUrl(String path, String query) {
		UriBuilder builder = getUriBuilder().path(path);
		if (query != null) {
			builder.replaceQuery(query);
		}
		return builder.toString();
	}
	
	@Test
	public void testOK() throws Exception {
		Long id1 = createLink(toMockUrl("/test/ok/"));
		Long id5 = createLink(toMockUrl("/test/ok/location-gîte-ardeche.fr/index.html"));
		Long id7 = createLink(getUriBuilder().host("127.0.0.1").path("/test/ok/V2/Partenaires/00043/Images/Chalet 95/dheilly003.JPG").toString());
		Long id8 = createLink(toMockUrl("/test/ok/reservit/avail-info.php", "hotelid=104461&userid=4340d8abb651c8ca20e6cd57a844f5708354&__utma=1.804870725.1361370840.1361370840.1361370840.1&__utmc=1&__utmz=1.1361370840.1.1.utmcsr=%28direct%29|utmccn=%28direct%29|utmcmd=%28none%29"));
		Long id9 = createLink(toMockUrl("/test/ok/translate_c", "client=tmpg&depth=1&hl=en&langpair=fr|en&rurl=translate.google.com&u=http://www.agence-bellemontagne.com/index.php%3Foption%3Dcom_content%26view%3Darticle%26id%3D49%26Itemid%3D51&usg=ALkJrhgFkVHObqq4-hABxmDFkFE00p369A"));
		Long id10 = createLink(toMockUrl("/test/ok/get"));
		Long id11 = createLink(getUriBuilder().toString() + "/test/ok " /* Watch the trailing space! */);
		Long id12 = createLink(toMockUrl("/test/301toOK"));
	
		Date beforeFirstBatchDate = waitAndCheck();
		
		checkStatusOK(id1, beforeFirstBatchDate);
		checkStatusOK(id5, beforeFirstBatchDate);
		checkStatusOK(id7, beforeFirstBatchDate);
		checkStatusOK(id8, beforeFirstBatchDate);
		checkStatusOK(id9, beforeFirstBatchDate);
		checkStatusOK(id10, beforeFirstBatchDate);
		checkStatusOK(id11, beforeFirstBatchDate);
		checkStatusOK(id12, beforeFirstBatchDate);
	}

	@Test
	public void testNOK() throws Exception {
		Long id2 = createLink("http://zzz.totototototo.zzz/totoz/");
		Long id3 = createLink(toMockUrl("/test/ko/not-found/"));
		Long id6 = createLink("http: http://example.com/");
		
		Date beforeFirstBatchDate = waitAndCheck();
		
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
		
		ExternalLinkWrapper externalLink6 = externalLinkWrapperService.getById(id6);
		Assert.assertEquals(ExternalLinkStatus.DEAD_LINK, externalLink6.getStatus());
		Assert.assertEquals(0, externalLink6.getConsecutiveFailures());
		Assert.assertNull(externalLink6.getLastStatusCode());
		Assert.assertEquals(ExternalLinkErrorType.URI_SYNTAX, externalLink6.getLastErrorType());
		Assert.assertTrue(externalLink6.getLastCheckDate().after(beforeFirstBatchDate));
	}

	/**
	 * Test batch features
	 */
	@Test
	public void testBatch() throws Exception {
		Long id1 = createLink(toMockUrl("/test/ok/"));
		Long id2 = createLink("http://zzz.totototototo.zzz/totoz/");
		Long id4 = createLink("http://zzz.totototototo.zzz/totoz/");
		
		Date beforeFirstBatchDate = waitAndCheck();
		
		checkStatusOK(id1, beforeFirstBatchDate);
		
		ExternalLinkWrapper externalLink2 = externalLinkWrapperService.getById(id2);
		Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink2.getStatus());
		Assert.assertEquals(1, externalLink2.getConsecutiveFailures());
		Assert.assertNull(externalLink2.getLastStatusCode());
		Assert.assertEquals(ExternalLinkErrorType.IO, externalLink2.getLastErrorType());
		Assert.assertTrue(externalLink2.getLastCheckDate().after(beforeFirstBatchDate));
		
		// Test that both ExternalLinkWrappers, having the same URL, have the same results 
		ExternalLinkWrapper externalLink4 = externalLinkWrapperService.getById(id4);
		Assert.assertEquals(externalLink2.getStatus(), externalLink4.getStatus());
		Assert.assertEquals(externalLink2.getConsecutiveFailures(), externalLink4.getConsecutiveFailures());
		Assert.assertEquals(externalLink2.getLastStatusCode(), externalLink4.getLastStatusCode());
		Assert.assertEquals(externalLink2.getLastErrorType(), externalLink4.getLastErrorType());
		Assert.assertEquals(externalLink2.getLastCheckDate(), externalLink2.getLastCheckDate());
		
		Date beforeSecondBatchDate = waitAndCheck();
		
		checkStatusOK(id1, beforeSecondBatchDate);
		
		externalLink2 = externalLinkWrapperService.getById(id2);
		Assert.assertEquals(ExternalLinkStatus.OFFLINE, externalLink2.getStatus());
		Assert.assertEquals(2, externalLink2.getConsecutiveFailures());
		Assert.assertNull(externalLink2.getLastStatusCode());
		Assert.assertEquals(ExternalLinkErrorType.IO, externalLink2.getLastErrorType());
		Assert.assertTrue(externalLink2.getLastCheckDate().after(beforeSecondBatchDate));
		
		// Test that both ExternalLinkWrappers, having the same URL, have the same results 
		externalLink4 = externalLinkWrapperService.getById(id4);
		Assert.assertEquals(externalLink2.getStatus(), externalLink4.getStatus());
		Assert.assertEquals(externalLink2.getConsecutiveFailures(), externalLink4.getConsecutiveFailures());
		Assert.assertEquals(externalLink2.getLastStatusCode(), externalLink4.getLastStatusCode());
		Assert.assertEquals(externalLink2.getLastErrorType(), externalLink4.getLastErrorType());
		Assert.assertEquals(externalLink2.getLastCheckDate(), externalLink2.getLastCheckDate());
	}
	
	private Date waitAndCheck() throws InterruptedException, ServiceException, SecurityServiceException {
		Date dateBefore = new Date();
		Thread.sleep(1000); // Make sure the checkDate will not be exactly the same as the date we return
		
		externalLinkCheckerService.checkBatch();
		
		entityManagerUtils.getEntityManager().clear();
		return dateBefore;
	}

	private Long createLink(String url) throws ServiceException, SecurityServiceException {
		ExternalLinkWrapper externalLink = new ExternalLinkWrapper(url);
		externalLinkWrapperService.create(externalLink);
		return externalLink.getId();
	}

	private void checkStatusOK(Long id, Date beforeFirstBatchDate) {
		checkStatus(id, beforeFirstBatchDate,
				ExternalLinkStatus.ONLINE, 0, Integer.valueOf(HttpStatus.SC_OK), null);
	}

	private void checkStatus(Long id, Date beforeFirstBatchDate,
			ExternalLinkStatus externalLinkStatus,
			int consecutiveFailures,
			Integer httpStatus,
			ExternalLinkErrorType errorType) {
		ExternalLinkWrapper externalLink = externalLinkWrapperService.getById(id);
		Assert.assertEquals(externalLinkStatus, externalLink.getStatus());
		Assert.assertEquals(consecutiveFailures, externalLink.getConsecutiveFailures());
		Assert.assertEquals(httpStatus, externalLink.getLastStatusCode());
		Assert.assertEquals(errorType, externalLink.getLastErrorType());
		Assert.assertTrue(externalLink.getLastCheckDate().after(beforeFirstBatchDate));
	}

	@Override
	protected void cleanAll() throws ServiceException, SecurityServiceException {
		cleanEntities(externalLinkWrapperService);
	}

}

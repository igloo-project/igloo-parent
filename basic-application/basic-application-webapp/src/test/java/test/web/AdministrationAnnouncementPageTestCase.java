package test.web;

import org.iglooproject.basicapp.web.application.administration.page.AdministrationAnnouncementListPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class AdministrationAnnouncementPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void initPage() throws ServiceException, SecurityServiceException {
		authenticateUser(administrator);
		
		tester.startPage(AdministrationAnnouncementListPage.class);
		tester.assertRenderedPage(AdministrationAnnouncementListPage.class);
	}

	@Test(expected = LinkInvalidTargetRuntimeException.class)
	public void accessUnauthorizedLinkDescriptor() throws ServiceException, SecurityServiceException {
		authenticateUser(basicUser);
		
		tester.executeUrl(AdministrationAnnouncementListPage.linkDescriptor().fullUrl());
	}

}

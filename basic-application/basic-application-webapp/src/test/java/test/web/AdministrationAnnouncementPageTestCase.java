package test.web;

import org.iglooproject.basicapp.web.application.administration.page.AdministrationAnnouncementListPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.junit.Test;

public class AdministrationAnnouncementPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void administrationAnnouncementPage() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		tester.startPage(AdministrationAnnouncementListPage.class);
		tester.assertRenderedPage(AdministrationAnnouncementListPage.class);
	}

	@Test(expected = LinkInvalidTargetRuntimeException.class)
	public void administrationAnnouncementPageUnauthorizedLinkDescriptor() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		tester.executeUrl(AdministrationAnnouncementListPage.linkDescriptor().fullUrl());
	}

}

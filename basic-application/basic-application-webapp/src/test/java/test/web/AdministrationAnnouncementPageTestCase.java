package test.web;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.iglooproject.basicapp.web.application.administration.page.AdministrationAnnouncementListPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.junit.jupiter.api.Test;

class AdministrationAnnouncementPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	void initPage() throws ServiceException, SecurityServiceException {
		authenticateUser(administrator);
		
		tester.startPage(AdministrationAnnouncementListPage.class);
		tester.assertRenderedPage(AdministrationAnnouncementListPage.class);
	}

	@Test
	void accessUnauthorizedLinkDescriptor() throws ServiceException, SecurityServiceException {
		authenticateUser(basicUser);
		assertThrows(
			LinkInvalidTargetRuntimeException.class,
			() -> tester.executeUrl(AdministrationAnnouncementListPage.linkDescriptor().fullUrl())
		);
	}

}

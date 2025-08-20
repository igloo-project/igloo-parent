package test.web;

import static basicapp.back.security.model.BasicApplicationPermissionConstants.GLOBAL_ANNOUNCEMENT_READ;
import static org.junit.jupiter.api.Assertions.assertThrows;

import basicapp.front.announcement.page.AnnouncementListPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.junit.jupiter.api.Test;
import test.web.config.BasicApplicationFrontSpringBootTest;

@BasicApplicationFrontSpringBootTest
class AnnouncementPageTestCase extends AbstractBasicApplicationFrontTestCase {

  @Test
  void initPage() throws ServiceException, SecurityServiceException {
    addPermissions(administrator, GLOBAL_ANNOUNCEMENT_READ);

    authenticateUser(administrator);

    tester.startPage(AnnouncementListPage.class);
    tester.assertRenderedPage(AnnouncementListPage.class);
  }

  @Test
  void accessUnauthorizedLinkDescriptor() throws ServiceException, SecurityServiceException {
    authenticateUser(basicUser);
    assertThrows(
        LinkInvalidTargetRuntimeException.class,
        () -> tester.executeUrl(AnnouncementListPage.linkDescriptor().fullUrl()));
  }
}

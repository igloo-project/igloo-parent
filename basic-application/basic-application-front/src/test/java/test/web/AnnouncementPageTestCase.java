package test.web;

import static org.junit.jupiter.api.Assertions.assertThrows;

import basicapp.front.announcement.page.AnnouncementListPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.wicket.more.link.descriptor.LinkInvalidTargetRuntimeException;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import test.core.PSQLTestContainerConfiguration;
import test.web.config.spring.SpringBootTestBasicApplicationWebapp;

@SpringBootTestBasicApplicationWebapp
@Import(PSQLTestContainerConfiguration.class)
class AnnouncementPageTestCase extends AbstractBasicApplicationWebappTestCase {

  @Test
  void initPage() throws ServiceException, SecurityServiceException {
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

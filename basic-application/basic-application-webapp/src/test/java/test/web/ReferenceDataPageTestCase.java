package test.web;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.iglooproject.basicapp.web.application.referencedata.page.ReferenceDataPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
public class ReferenceDataPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void initPage() throws ServiceException, SecurityServiceException {
		authenticateUser(administrator);
		
		tester.startPage(ReferenceDataPage.class);
		tester.assertRenderedPage(ReferenceDataPage.class);
	}

	/*
	 * WicketTester does not pass through Spring Security, so accessibility test is not relevant
	 * except when we use an @AuthorizeInstantiation annotation (for example on ReferenceDataTemplate)
	 */
	@Test(expected = UnauthorizedInstantiationException.class)
	public void accessUserUnauthorized() throws ServiceException, SecurityServiceException {
		authenticateUser(basicUser);
		
		tester.executeUrl("./reference-data/"); // equals to startPage(ReferenceDataPage.class)
	}

}

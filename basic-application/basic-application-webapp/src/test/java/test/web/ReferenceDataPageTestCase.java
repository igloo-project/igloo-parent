package test.web;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.iglooproject.basicapp.web.application.referencedata.page.ReferenceDataPage;
import org.iglooproject.jpa.exception.SecurityServiceException;
import org.iglooproject.jpa.exception.ServiceException;
import org.iglooproject.jpa.security.business.authority.util.CoreAuthorityConstants;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
public class ReferenceDataPageTestCase extends AbstractBasicApplicationWebappTestCase {

	@Test
	public void referenceDataPage() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_ADMIN);
		
		getWicketTester().startPage(ReferenceDataPage.class);
		
		getWicketTester().assertRenderedPage(ReferenceDataPage.class);
	}

	/*
	 * WicketTester does not passes through Spring Security, so the accessibility test for most pages is not relevant
	 * except when we use an @AuthorizeInstantiation annotation (here on ReferenceDataTemplate)
	 */
	@Test
	public void referenceDataPageAccessibilityAuthenticated() throws ServiceException, SecurityServiceException {
		createAndAuthenticateUser(CoreAuthorityConstants.ROLE_AUTHENTICATED);
		
		boolean unauthorizedInstantiation = false;
		try {
			getWicketTester().executeUrl("./reference-data/"); // equals to startPage(ReferenceDataPage.class)
		} catch (UnauthorizedInstantiationException e) {
			unauthorizedInstantiation = true;
		}
		Assert.assertTrue(unauthorizedInstantiation);
	}
}
